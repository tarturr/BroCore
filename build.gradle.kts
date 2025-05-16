plugins {
    id("java")
}

allprojects {
    group = "com.github.tarturr"
    version = "v1.0"

    repositories {
        mavenCentral()

        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}