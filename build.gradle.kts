plugins {
    id("java")
}

group = "fr.tartur.games"
version = "v1.0"

repositories {
    mavenCentral()
    
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots"
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