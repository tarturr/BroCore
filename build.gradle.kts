plugins {
    id("java")
}


group = "com.github.tarturr"
version = "v1.0"

repositories {
    mavenCentral()

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-api:4.21.0")
    implementation("com.zaxxer:HikariCP:6.3.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}