# Build notes

- Android Gradle Plugin: 8.5.2
- Gradle: 8.7 (installed by the GitHub Actions setup-gradle step, no wrapper needed)
- Java: 17
- compileSdk / targetSdk: 34, minSdk: 26
- APK task: assembleDebug
- UI: Jetpack Compose (Material 3) with androidx.glance for the home screen widget

Every push to main runs the Build Android APK workflow. It uploads the debug APK as the artifact tend-debug-apk, and on a tag it also attaches the APK to a GitHub release.
