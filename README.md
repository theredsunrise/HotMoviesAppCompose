# HotM@vies

**HotM@vies** is a sample Android application that displays trending movies from the online movie
database TMDB. The main goal is to demonstrate the integration of components and architecture for **educational and demonstration
purposes**.

The application follows the **Domain-Driven Design (Onion Architecture)** with **MVVM in the
presentation layer**. It utilizes components from **Material3** and **Jetpack Compose libraries**,
including:

- **Jetpack Compose**
- **Material3 UI (light/dark theme)**
- **MotionLayout**
- **Material3 transitions**
- **Navigation Component**
- **Paging3**
- **DataStore**

Additionally, it includes:

- **Flow**
- **Retrofit**
- **Coil**
- **Lottie**
- **Keystore**
- **Downloadable fonts**
- **Encryption of TMDB attributes in the Gradle script and decryption in the code**

<p style="text-align: left;">
  <img width="150" src="appVideo.gif" alt="Video">
</p>

### 🔹 How to Try the Application

To use the application, you need to register as a developer on the **TMDB online movie database**.
The process is simple:  
👉 [TMDB Developer Registration](https://developer.themoviedb.org/docs/getting-started)

After registration, fill in the following attributes in the `tmdb.properties` file:

```
apiKey=...
```
and sign in to the app using your TMDB account credentials.

To build the release version of an app, you need to add your own `keystore.jks` file and fill in the
following properties in the `keystore.properties` file:

```
storePassword=...
keyPassword=...
keyAlias=...
```