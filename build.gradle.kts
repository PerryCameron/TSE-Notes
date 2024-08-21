import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
}

group = "com.schneider"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val appVersion: String by project

// Process and copy app.properties with version replacement
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

tasks.test {
    useJUnitPlatform()
}