# EdgeNCG

EdgeNCG is a custom Android SDK designed to collect application logs and device metrics from Android devices and send them to your server for monitoring and analysis. This SDK provides a robust solution for gathering critical data, including app usage events, error and crash logs, network requests, device information, and performance metrics. With EdgeNCG, you can ensure that you have comprehensive insights into your application's behavior and performance, enabling proactive monitoring and troubleshooting.

## Features

- Seamless integration with Android applications.
- Collects comprehensive logs and device metrics.
- Securely transmits data to your server.
- Configurable logging levels and endpoints.
- Efficient log storage and periodic transmission.
- Robust error handling and retry mechanisms.

Stay tuned for more details on how to integrate and use EdgeNCG in your Android applications.

## Installation

To integrate EdgeNCG into your Android project, follow these steps:

1. **Add the Maven repository**

   Ensure that you have added the Maven repository to your project-level `build.gradle` file:

   ```groovy
   allprojects {
       repositories {
           google()
           mavenCentral()
           // Add JitPack repository if needed
           maven { url 'https://jitpack.io' }
       }
   }

2. **Add the Dependency**
   
   Add the EdgeNCG dependency to your app-level build.gradle file:
   
   ```groovy
   dependencies {
    implementation 'com.yourorganization:edgencg:1.0.0'
   }

   ```kotlin
   dependencies {
    implementation("com.yourorganization:edgencg:1.0.0")
   }
   
4. **Initialize EdgeNCG**

   Initialize EdgeNCG in your application's Application class:

   ```kotlin
   class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize EdgeNCG with your server endpoint and configuration
        EdgeNCG.initialize(
            context = this,
            serverUrl = "https://yourserver.com/api/logs",
            config = EdgeNCGConfig(
                logLevel = LogLevel.DEBUG,
                uploadInterval = 15, // in minutes
                maxLogSize = 1024 * 1024 // 1 MB
            )
        )
      }
    }

6. **Add Permissions**

   Ensure that your AndroidManifest.xml includes the necessary permissions:

   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   


