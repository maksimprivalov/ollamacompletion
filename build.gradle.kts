plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "com.github.maksimprivalov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.ollama4j:ollama4j:1.0.93")
    implementation("dev.langchain4j:langchain4j-ollama:0.25.0")
    implementation("org.projectlombok:lombok:1.18.28")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("io.mockk:mockk:1.13.8")
}

intellij {
    version.set("2023.2.8")
    type.set("IC")
}

sourceSets {
    main {
        java.srcDirs("src/main/java")
        kotlin.srcDirs("src/main/kotlin")
    }
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("233.*")
    }

    named("buildSearchableOptions") {
        enabled = false
    }

    test {
            useJUnitPlatform()
    }
}
