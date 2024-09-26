import org.apache.tools.ant.filters.ReplaceTokens
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
// I am using Gradle 8.8
// I am also using bellsoft-jdk21.0.4+9-windows-amd64-full - This is an SDK with JavaFX included, I am using it to simplify this instillation
plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.schneider"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val appVersion: String by project

// Process and copy app.properties with version replacement
// this doesn't work yet, but will be for versioning my app
tasks.register<Copy>("processAppProperties") {
    from("src/main/resources")
    into(layout.buildDirectory.dir("resources/"))
    filter<ReplaceTokens>("tokens" to mapOf("version" to "test"))
    into(layout.buildDirectory.dir("processedResources"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    doLast {
        println("Processed app.properties:")
        println(file(layout.buildDirectory.dir("processedResources").get().asFile).resolve("app.properties").readText())
    }
}

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
    implementation ("io.github.mkpaz:atlantafx-base:2.0.1")
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
    implementation("org.apache.poi:poi:5.3.0")
    // Apache POI - Java API To Access Microsoft Format Files
    implementation("org.apache.poi:poi-ooxml:5.3.0")


    testImplementation("ch.qos.logback:logback-classic:1.5.6")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    // Define the main class for the application
    mainClass.set("com.L2.BaseApplication")  // Replace with your actual main class
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
        "--input", "build/libs",  // Path to the JAR file directory
        "--main-jar", "TSENotes-all.jar",  // Replace with your actual JAR name
        "--main-class", "com.L2.BaseApplication",  // Replace with your actual main class
        "--name", "TSENotes",  // Name of the app
        "--type", "app-image",  // You can also use pkg, dmg, exe, etc.
        "--runtime-image", "C:/Users/sesa91827/.jdks/bellsoft-jdk21.0.4+9-windows-amd64-full/jdk-21.0.4-full",  // Path to Java 21 runtime image
        "--dest", "build/jpackage",  // Output destination
        "--icon", "src/main/resources/images/app-icon-64.png"  // Optional: Path to your app's icon
    )
}

tasks.register<Exec>("packageAppInstallerMac") {
    group = "build"
    description = "Packages the application with a bundled JRE using jpackage for macOS"

    doFirst {
        delete(file("build/jpackage/TSENotesInstaller"))
    }

    commandLine(
        "C:/Users/sesa91827/.jdks/bellsoft-jdk21.0.4+9-windows-amd64-full/jdk-21.0.4-full/bin/jpackage",  // Assuming jpackage is on your PATH
        "--input", "build/libs",
        "--main-jar", "TSENotes-all.jar",
        "--main-class", "com.L2.BaseApplication",
        "--name", "TSENotes",
        "--type", "pkg",  // You can use dmg for macOS disk image installer
        "--runtime-image", "C:/Users/sesa91827/.jdks/bellsoft-jdk21.0.4+9-windows-amd64-full",
        "--dest", "build/jpackage/TSENotesInstaller",
        "--install-dir", System.getProperty("user.home") + "/TSENotes",  // Default to user's home directory
        "--icon", "src/main/resources/images/app-icon-64.png"
    )
}


tasks.test {
    useJUnitPlatform()
}

// so after running
