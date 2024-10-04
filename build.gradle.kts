import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.text.SimpleDateFormat
import java.util.*

// I am using Gradle 8.8
// I am also using bellsoft-jdk21.0.4+9-windows-amd64-full - This is an SDK with JavaFX included, I am using it to simplify this instillation
plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.palantir.git-version") version "3.1.0"  // latest release on Jun 5, 2024
}

group = "com.schneider"

// Retrieve the Git version directly from the project object
val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion()

application {
    // Define the main class for the application
    mainClass.set("com.L2.BaseApplication")  // Replace with your actual main class
}

tasks.jar {
    manifest {
        attributes["Implementation-Version"] = project.version
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val appVersion: String by project

dependencies {
    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    implementation("org.xerial:sqlite-jdbc:3.46.1.0")
    // https://mvnrepository.com/artifact/org.springframework/spring-jdbc
    implementation("org.springframework:spring-jdbc:6.1.12")
    // Allows use of clipboard API
    implementation("net.java.dev.jna:jna:5.14.0")
    // https://mvnrepository.com/artifact/net.java.dev.jna/jna-platform
    implementation("net.java.dev.jna:jna-platform:5.14.0")
    // theme
    implementation("io.github.mkpaz:atlantafx-base:2.0.1")
    // logging api
    implementation("org.slf4j:slf4j-api:2.0.16")
    // implementation of the SLF4J API for Logback, a reliable, generic, fast and flexible logging framework.
    implementation("ch.qos.logback:logback-classic:1.5.6")
    // Core implementation of Logback, a reliable, generic, fast and flexible logging framework.
    implementation("ch.qos.logback:logback-core:1.5.6")
    // allows apache poi to log
    implementation("org.apache.logging.log4j:log4j-to-slf4j:2.23.1")
    // API for SLF4J (The Simple Logging Facade for Java) which serves as a simple facade or
    // abstraction for various logging frameworks, allowing the end user to plug in the desired
    // logging framework at deployment time.
//    implementation("org.apache.poi:poi:5.3.0")
    // Apache POI - Java API To Access Microsoft Format Files
//    implementation("org.apache.poi:poi-ooxml:5.3.0")
    testImplementation("ch.qos.logback:logback-classic:1.5.6")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}




tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
}

// Create the fat JAR using shadowJar
tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("TSENotes")  // Set the base name for the fat JAR
    archiveVersion.set("")           // Remove the version from the file name
    archiveClassifier.set("all")     // Add "all" as the classifier to indicate a fat JAR
    manifest {
        attributes["Main-Class"] = "com.L2.BaseApplication"  // Ensure the manifest has the correct main class
    }
}

tasks.jar {
    manifest {
        archiveBaseName.set("TSENotes")
        archiveVersion.set("")
        archiveClassifier.set("")
        attributes["Main-Class"] = "com.L2.BaseApplication"
    }
}

// jpackage task to create a self-contained application with a bundled JRE
tasks.register<Exec>("packageApp") {
    group = "build"
    description = "Packages the application with a bundled JRE using jpackage"

    doFirst {
        delete(file("build/jpackage/TSENotes"))
    }

    // Directly point to the jpackage tool in your Java 21 SDK
    commandLine(
        "C:/Users/sesa91827/.jdks/bellsoft-jdk21.0.4+9-windows-amd64-full/jdk-21.0.4-full/bin/jpackage",  // Path to your jpackage
        "--input",
        "build/libs",  // Path to the JAR file directory
        "--main-jar",
        "TSENotes-all.jar",  // Replace with your actual JAR name
        "--main-class",
        "com.L2.BaseApplication",  // Replace with your actual main class
        "--name",
        "TSENotes",  // Name of the app
        "--type",
        "app-image",  // You can also use pkg, dmg, exe, etc.
        "--runtime-image",
        "C:/Users/sesa91827/.jdks/bellsoft-jdk21.0.4+9-windows-amd64-full/jdk-21.0.4-full",  // Path to Java 21 runtime image
        "--dest",
        "build/jpackage",  // Output destination
        "--icon",
        "src/main/resources/images/TSELogo.ico"  // Optional: Path to your app's icon
    )
}

tasks.register<Exec>("packageAppInstallerWindows") {
    group = "build"
    description = "Packages the application with a bundled JRE using jpackage for Windows"

    doFirst {
        delete(file("build/jpackage/TSENotesInstaller"))
    }

    commandLine(
        "C:/Users/sesa91827/.jdks/bellsoft-jdk21.0.4+9-windows-amd64-full/jdk-21.0.4-full/bin/jpackage",
        "--input", "build/libs",
        "--main-jar", "TSENotes-all.jar",
        "--main-class", "com.L2.BaseApplication",
        "--name", "TSENotes",
        "--type", "exe",  // You can also use "msi" for a Windows installer
        "--runtime-image", "C:/Users/sesa91827/.jdks/bellsoft-jdk21.0.4+9-windows-amd64-full",
        "--dest", "build/jpackage/TSENotesInstaller",
        "--install-dir", System.getenv("UserProfile") + "/TSENotes",  // Install in user's home directory
        "--icon", "src/main/resources/images/TSELogo.ico",  // Path to your ICO file for Windows
        "--win-menu",  // Adds an entry to the Start Menu
        "--win-shortcut",  // Creates a desktop shortcut
        "--win-console"  // Optional: Displays console output
    )
}

//tasks.register("generateVersionProperties") {
//    doLast {
//        val propertiesFile = file("src/main/resources/version.properties")
//        propertiesFile.parentFile.mkdirs()
//        propertiesFile.writeText("version=${project.version}\n")
//    }
//}
//
//tasks.register("generateBuildProperties") {
//    doLast {
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        val buildTimestamp = dateFormat.format(Date())
//        val propertiesFile = file("src/main/resources/build.properties")
//        propertiesFile.parentFile.mkdirs()
//        propertiesFile.writeText("build.timestamp=$buildTimestamp\n")
//    }
//}
//
//tasks.processResources {
//    dependsOn("generateBuildProperties")
//}
//
//tasks.processResources {
//    dependsOn("generateVersionProperties")
//}

tasks.register("generateBuildInfoProperties") {
    doLast {
        // Create timestamp
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val buildTimestamp = dateFormat.format(Date())
        val javaVersion = System.getProperty("java.version") ?: "Unknown"
        // Generate a combined properties file
        val propertiesFile = file("src/main/resources/version.properties")
        propertiesFile.parentFile.mkdirs()
        propertiesFile.writeText("""
            version=${project.version}
            build.timestamp=$buildTimestamp
            java.version=$javaVersion
        """.trimIndent())
    }
}

// Ensure the combined properties file is generated during the build
tasks.processResources {
    dependsOn("generateBuildInfoProperties")
}


tasks.test {
    useJUnitPlatform()
}

// so after running
