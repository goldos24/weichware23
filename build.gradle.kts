plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

sourceSets.main.get().java.srcDir("src/main")
sourceSets.main.get().resources.srcDir("src/resources")

application {
    mainClassName = "sc.player2023.Starter"
}

repositories {
    mavenCentral()
    maven("https://maven.wso2.org/nexus/content/groups/wso2-public/")
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    if (gradle.startParameter.isOffline) {
        implementation(fileTree("lib"))
    } else {
        implementation("com.github.software-challenge.backend", "penguins_2023", "23.0.2")
    }
}

tasks.shadowJar {
    archiveBaseName.set("penguins_2023_client")
    archiveClassifier.set("")
    destinationDirectory.set(rootDir)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
