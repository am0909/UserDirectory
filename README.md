# User Directory App

A Kotlin-based Android application for managing a user directory. The app uses modern Android development practices, including Jetpack Compose, Hilt for dependency injection, Room for local database management, and Retrofit for network operations. It uses data from the [Random User Generator API](https://randomuser.me/).

## Features

- Fetch user data from a remote API and store it in a local database.
- Search users by name.
- Refresh user data from the remote API.
- Offline support using Room as the local database.
- Clean architecture with separation of concerns.

## Tech Stack

- **Programming Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Local Database**: Room
- **Networking**: Retrofit with Gson converter
- **Coroutines**: For asynchronous programming
- **Flow**: For reactive streams

## Project Structure

- `data`: Contains data sources, including local database and remote API, along with data mappers.
- `domain`: Contains business logic and models.
- `ui`: Contains ViewModels and UI components built with Jetpack Compose.

## Setup Instructions

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or physical device.

## Future Scope

- Add favorites functionality.
- Implement pagination.