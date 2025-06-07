plugins {
    id("java-library")
}

dependencies {
    compileOnlyApi("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}