# Build notes

- Android Gradle Plugin: 8.7.3
- Gradle: 8.9 (provided by the GitHub Actions setup-gradle action)
- Java: 17
- Android SDK: API 35 / Build Tools 35.0.0
- APK task: `assembleDebug`

The workflow installs named SDK packages rather than requesting the obsolete `tools` package.
A successful build cannot be guaranteed on every runner or network state, but the workflow is configured to surface setup and build errors clearly.
