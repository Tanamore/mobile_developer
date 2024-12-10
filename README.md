# Tanamore | Mobile Developer Documentation

This is the Android application for Tanamore, a plant care app built using Kotlin. The app helps users with plant disease detection, plant identification, and provides detailed information about various plants in its encyclopedia.  

## Project Overview
The Tanamore Android app provides features for plant care, including:

1. Predicting plant diseases using image recognition.
2. Identifying plants from images.
3. Searching and retrieving plant information from the encyclopedia.

This app is built with Kotlin and follows modern Android development practices.

## Features
1. **User Registration** - Allows users to create an account, enabling personalized recommendations.
2. **Plant Disease Detection** - Identifies plant diseases via photo upload, providing insights into the plant’s health.
3. **Treatment Recommendation** - Provides tailored advice based on plant type and disease, including watering, nutrient, and light needs.
4. **Plant Detection** - Identify plants through photo analysis.
5. **Plant Encyclopedia** - A comprehensive guide containing descriptions, habitats, common diseases, and preventive measures.
6. **Plant Watering & Harvest Reminder** - Sends notifications to remind users when to water their plants and when to harvest them, ensuring timely care.
7. **KebunKu** - Personal garden tracker to monitor and manage user’s own plants.

## Tech Stack
- Kotlin: Primary programming language for Android development.
- Android SDK: Used for Android development and UI components.
- Retrofit: For making network requests to the backend API.
- CameraX: For capture images.
- Firebase: For user authentication.
- Glide: For image loading and caching.

## Installation
To run the project locally, follow these steps:

1. Clone the repository:

```
git clone https://github.com/yourusername/tanamore-android.git
```
2. Open the project in Android Studio:
   - Open Android Studio and click on Open an existing project.
   - - Select the folder where the repository was cloned.
    
3. Install dependencies:
   - Make sure all dependencies are installed by syncing the project with Gradle:
      - Click on File > Sync Project with Gradle Files.
        
5. Run the app:
   - Connect an Android device or start an emulator.
   - Click on the Run button (green play icon) in Android Studio.

## Usage  
1. Launch the app and navigate through the main screen.
2. Upload a plant image for disease detection or plant identification.
3. Browse the plant encyclopedia by using the search feature or viewing the list of plants.

## Contributing
1. Fork the repository to your own GitHub account.
2. Create a new branch for your changes.
```
git checkout -b feature-branch
```
3. Make changes and commit them.
```
git commit -m "Added new feature"
```
4. Push your changes to your fork.
```
git push origin feature-branch
```
5. Open a Pull Request to the main repository.

We welcome contributions to enhance the Tanamore app. Please make sure to write tests and follow the coding standards.
