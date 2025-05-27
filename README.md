# 🌦️ Weather App - Android Kotlin

This project is an Android-based **Weather Application** developed using Kotlin. Users can get real-time weather information either by entering a city name manually or using their device's **current location**.

## 🚀 Features

-📍 Get weather using device GPS location
-🔍 Search weather by city name
-🌡️ Show temperature (°C) and weather description
-🛠️ Home screen widget for quick weather info
-⚙️ Connects to OpenWeatherMap API with Retrofit
-📱 Clean and modern UI using ViewBinding and Material Design

## 🛠️ Technologies Used

- **Kotlin** – Main programming language
- **Retrofit** – For making HTTP requests to REST APIs, with API key management
- **ViewBinding** – Type-safe XML view references
- **FusedLocationProviderClient** – Location services
- **OpenWeatherMap API** – Weather data provider requiring API key
- **Material Design** – For modern UI components
- **Android App Widgets** -  Implemented a home screen widget with RemoteViews to display weather updates even when the app is closed.

📝 Project Overview and Development Process  
-**Setup & Permissions** - Configured AndroidManifest with Internet and location permissions; enabled cleartext traffic for API calls.  
-**Location Handling** - Used FusedLocationProviderClient with runtime permission checks to get device location.   
-**API Integration** - Connected to OpenWeatherMap via Retrofit; handled API key securely and parsed JSON responses.   
-**UI Development** - Built clean XML layouts for main activity and widget; used ViewBinding for safe UI access.     
-**Widget Implementation** - Created a simple widget using LinearLayout (due to RemoteViews limits); supports periodic and manual updates.      
-**Error Handling & Testing** - Added null safety and default values; fixed layout inflation issues; tested widget on different launchers for compatibility.
