plugins {
    id("java-gradle-plugin")
}

group = "ovh.neziw.sftp"
version = "1.0.0"

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation")
    options.encoding = "UTF-8"
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins {
        create("sftpPlugin") {
            id = "ovh.neziw.sftp"
            implementationClass = "ovh.neziw.sftp.SftpPlugin"
        }
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.github.mwiede:jsch:2.27.6")
}