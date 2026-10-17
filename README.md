# GlassNote Todo

A minimal white-glassmorphism Android app with Notes, To-do list, local persistence, and an Android home-screen To-do widget.

## Features
- Premium white minimal glass-style UI (soft gradients, translucent cards, rounded corners)
- Notes: add, edit, delete
- To-do: add, complete/uncomplete, delete
- Local data persistence using SharedPreferences + JSON
- Android home-screen widget showing the latest tasks
- GitHub Actions workflow to build a debug APK and publish it as an artifact

## Build locally
1. Install Android Studio (with Android SDK).
2. Open this project folder.
3. Let Gradle sync, then run the `app` configuration.
4. Or run: `./gradlew assembleDebug` (after adding the Gradle wrapper).

## Build using GitHub Actions
1. Create a GitHub repository and upload all project files.
2. Go to **Actions** and enable workflows if asked.
3. Run **Android APK Build** from the Actions tab, or push to `main`.
4. Open the workflow run and download the `glassnote-todo-debug-apk` artifact.
5. Extract the artifact ZIP to get `app-debug.apk`.

## Notes
- The workflow uses a hosted Ubuntu runner and Android SDK setup.
- Debug APKs are for testing. For Play Store distribution, configure a release signing key securely with GitHub Secrets.
- The home-screen widget refreshes when tasks change in the app and when Android updates the widget. Add it by long-pressing the home screen, opening Widgets, then selecting GlassNote Todo.
