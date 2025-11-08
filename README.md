<div align="center">

# 🚀 Gradle SFTP Plugin

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Gradle](https://img.shields.io/badge/Gradle-8.0+-green.svg)](https://gradle.org/)
[![Java](https://img.shields.io/badge/Java-11+-blue.svg)](https://openjdk.java.net/)

</div>

> Simple, lightweight, and modern Gradle plugin for uploading files via SFTP protocol

---

## 📋 Table of Contents

- [Features](#-features)
- [Requirements](#-requirements)
- [Installation](#-installation)
- [Quick Start](#-quick-start)
- [Configuration](#-configuration)
- [Usage Examples](#-usage-examples)
- [Task Properties](#-task-properties)
- [Best Practices](#-best-practices)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

- 🔐 **Secure File Transfer** - Upload files securely using SFTP protocol
- 🎯 **Simple Configuration** - Easy-to-use DSL for quick setup
- 🚀 **Lightweight** - Minimal dependencies, fast execution
- 🔧 **Gradle Integration** - Seamlessly integrates with Gradle build lifecycle
- 📦 **Flexible** - Support for custom ports, paths, and file locations
- 🛡️ **Error Handling** - Comprehensive error messages and exception handling
- 📝 **Logging** - Built-in logging for upload status and debugging

---

## 📦 Requirements

- **Gradle**: 8.0 or higher
- **Java**: 11 or higher
- **SFTP Server**: Accessible SFTP server with valid credentials

---

## 🔧 Installation

### Using the Plugin DSL

Add the plugin to your `build.gradle.kts`:

```kotlin
plugins {
    id("ovh.neziw.sftp") version "1.0.0"
}
```

### Using Legacy Plugin Application

If you're using the older plugin application method:

```kotlin
buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("ovh.neziw.sftp:gradle-sftp-plugin:1.0.0")
    }
}

apply(plugin = "ovh.neziw.sftp")
```

---

## 🚀 Quick Start

### Basic Example

```kotlin
plugins {
    id("ovh.neziw.sftp") version "1.0.0"
}

tasks.register<SftpTask>("upload") {
    host.set("sftp.example.com")
    port.set(22)
    username.set("myusername")
    password.set("mypassword")
    
    localFile.set(file("build/libs/my-app.jar"))
    remotePath.set("/home/user/applications/my-app.jar")
}
```

Run the task:

```bash
./gradlew upload
```

---

## ⚙️ Configuration

### Task Registration

The plugin automatically registers a task named `sftpUpload` with default configuration. You can also create custom tasks:

```kotlin
tasks.register<SftpTask>("uploadToProduction") {
    // Configuration here
}

tasks.register<SftpTask>("uploadToStaging") {
    // Configuration here
}
```

---

## 📖 Usage Examples

### Example 1: Upload JAR Artifact

```kotlin
tasks.register<SftpTask>("uploadJar") {
    host.set("deploy.example.com")
    port.set(22)
    username.set("deployer")
    password.set(project.findProperty("sftp.password") as String? ?: "")
    
    val jarName = "my-application-${project.version}.jar"
    localFile.set(file("build/libs/${jarName}"))
    remotePath.set("/opt/applications/${jarName}")
}
```

### Example 2: Upload with Dynamic Version

```kotlin
tasks.register<SftpTask>("uploadRelease") {
    host.set("releases.example.com")
    port.set(2222) // Custom port
    username.set("releaser")
    password.set(System.getenv("SFTP_PASSWORD") ?: "")
    
    val artifactName = "${project.name}-${project.version}.jar"
    localFile.set(file("build/libs/${artifactName}"))
    remotePath.set("/releases/${project.version}/${artifactName}")
    
    // Make upload depend on build
    dependsOn("build")
}
```

### Example 3: Upload Multiple Files

```kotlin
tasks.register<SftpTask>("uploadDistributions") {
    host.set("dist.example.com")
    port.set(22)
    username.set("distuser")
    password.set(project.findProperty("sftp.password") as String? ?: "")
    
    // Upload main JAR
    localFile.set(file("build/libs/${project.name}-${project.version}.jar"))
    remotePath.set("/distributions/jar/${project.name}-${project.version}.jar")
}

tasks.register<SftpTask>("uploadSources") {
    host.set("dist.example.com")
    port.set(22)
    username.set("distuser")
    password.set(project.findProperty("sftp.password") as String? ?: "")
    
    // Upload sources JAR
    localFile.set(file("build/libs/${project.name}-${project.version}-sources.jar"))
    remotePath.set("/distributions/sources/${project.name}-${project.version}-sources.jar")
}

// Create a task that uploads both
tasks.register("uploadAll") {
    dependsOn("uploadDistributions", "uploadSources")
}
```

### Example 4: Environment-Specific Configuration

```kotlin
val environment = project.findProperty("env") as String? ?: "development"

tasks.register<SftpTask>("uploadToEnvironment") {
    when (environment) {
        "production" -> {
            host.set("prod-sftp.example.com")
            username.set("prod-user")
            remotePath.set("/prod/applications/")
        }
        "staging" -> {
            host.set("staging-sftp.example.com")
            username.set("staging-user")
            remotePath.set("/staging/applications/")
        }
        else -> {
            host.set("dev-sftp.example.com")
            username.set("dev-user")
            remotePath.set("/dev/applications/")
        }
    }
    
    port.set(22)
    password.set(project.findProperty("sftp.password") as String? ?: "")
    localFile.set(file("build/libs/${project.name}-${project.version}.jar"))
}
```

### Example 5: Integration with Build Lifecycle

```kotlin
tasks.register<SftpTask>("deploy") {
    host.set("deploy.example.com")
    port.set(22)
    username.set("deployer")
    password.set(System.getenv("DEPLOY_PASSWORD") ?: "")
    
    val artifactName = "${project.name}-${project.version}.jar"
    localFile.set(file("build/libs/${artifactName}"))
    remotePath.set("/var/applications/${artifactName}")
    
    // Ensure build completes before deployment
    dependsOn("build")
    
    // Only run if build was successful
    mustRunAfter("build")
}
```

---

## 🔍 Task Properties

| Property | Type | Required | Description | Default |
|----------|------|----------|-------------|---------|
| `host` | `String` | ✅ Yes | SFTP server hostname or IP address | `"your-host.com"` |
| `port` | `Integer` | ✅ Yes | SFTP server port number | `22` |
| `username` | `String` | ✅ Yes | SFTP server username | `"your-username"` |
| `password` | `String` | ✅ Yes | SFTP server password | `"your-password"` |
| `localFile` | `File` | ✅ Yes | Local file to upload | `file("build/libs/your-library.jar")` |
| `remotePath` | `String` | ✅ Yes | Remote path where file will be uploaded | `"/path/to/remote/your-library.jar"` |

---

## 💡 Best Practices

### 1. **Secure Credential Management**

Never hardcode passwords in your build files. Use Gradle properties or environment variables:

```kotlin
// In gradle.properties (not committed to VCS)
// sftp.password=your-secure-password

// In build.gradle.kts
password.set(project.findProperty("sftp.password") as String? ?: "")
```

Or use environment variables:

```kotlin
password.set(System.getenv("SFTP_PASSWORD") ?: "")
```

### 2. **Use Gradle Properties File**

Create a `gradle.properties` file (and add it to `.gitignore`):

```properties
sftp.host=your-sftp-server.com
sftp.port=22
sftp.username=your-username
sftp.password=your-password
```

Then reference it in your build:

```kotlin
tasks.register<SftpTask>("upload") {
    host.set(project.findProperty("sftp.host") as String? ?: "")
    port.set((project.findProperty("sftp.port") as String?)?.toInt() ?: 22)
    username.set(project.findProperty("sftp.username") as String? ?: "")
    password.set(project.findProperty("sftp.password") as String? ?: "")
    // ...
}
```

### 3. **Task Dependencies**

Always ensure required tasks complete before upload:

```kotlin
tasks.register<SftpTask>("upload") {
    dependsOn("build")
    // ...
}
```

### 4. **Error Handling**

The plugin includes built-in error handling, but you can add custom error handling:

```kotlin
tasks.register<SftpTask>("upload") {
    // ... configuration ...
    
    doLast {
        if (!localFile.get().exists()) {
            throw GradleException("Local file does not exist: ${localFile.get().absolutePath}")
        }
    }
}
```

---

## 🐛 Troubleshooting

### 1. Connection Issues

**Problem**: Cannot connect to SFTP server

**Solutions**:
- Verify host and port are correct
- Check network connectivity: `telnet <host> <port>`
- Ensure firewall allows SFTP connections
- Verify credentials are correct

### 2. Authentication Failures

**Problem**: Authentication failed

**Solutions**:
- Double-check username and password
- Ensure the user has write permissions on the remote path
- Verify the user account is active

### 3. File Not Found

**Problem**: Local file does not exist

**Solutions**:
- Verify the file path is correct
- Ensure the build task has completed successfully
- Check file permissions

### 4. Permission Denied

**Problem**: Permission denied on remote path

**Solutions**:
- Verify the user has write permissions
- Check if the remote directory exists
- Ensure the remote path is accessible

---

## 🔧 Advanced Configuration

### Custom Task Names

```kotlin
tasks.register<SftpTask>("customUploadTask") {
    // Your configuration
}
```

### Conditional Uploads

```kotlin
tasks.register<SftpTask>("conditionalUpload") {
    onlyIf {
        project.hasProperty("upload.enabled") && 
        project.property("upload.enabled") == "true"
    }
    // Configuration
}
```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Built with [JSch](https://github.com/mwiede/jsch) for SFTP functionality
- Inspired by the need for simple SFTP uploads in Gradle builds

---

## 📞 Support

If you encounter any issues or have questions, please open an issue on the GitHub repository.

---

**Made with ❤️ by [neziw](https://github.com/neziw)**
