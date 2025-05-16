plugins { 
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("com.zaxxer:HikariCP:6.3.0")
    compileOnly("net.kyori:adventure-api:4.21.0")
    implementation(project(":api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}