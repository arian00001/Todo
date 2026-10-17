# GlassNote Todo — Premium Glassmorphism

A native Android notes + todo starter app built with Kotlin and Jetpack Compose. It includes a soft white glass-style interface, local storage, and an Android home-screen todo widget.

## Included
- Notes: create, edit, delete, locally stored
- Todo tasks: add, complete, delete
- Home-screen widget displaying pending tasks
- GitHub Actions workflow to build and upload a debug APK artifact
- No `android-actions/setup-android` step and no invalid SDK package named `tools`

## Build APK with GitHub Actions
1. Upload the **contents** of this ZIP to the root of your GitHub repository (keep the `.github` folder).
2. Make sure `.github/workflows/android-apk.yml` is present in the repository.
3. If an older workflow file still exists that uses `android-actions/setup-android` or installs a package called `tools`, delete or update that old workflow too. GitHub runs every enabled workflow matching a push.
4. Open **Actions → Android APK Build** and run the workflow, or push to `main`/`master`.
5. Download `glassnote-todo-debug-apk` from the successful run's Artifacts section, then extract `app-debug.apk`.

## Important
This source ZIP is supplied as a starter project; the APK has not been built or tested in this environment. The workflow avoids the exact invalid `sdkmanager tools` call shown in the reported log, but no build can be guaranteed until GitHub runs it successfully. If a new error appears, share the first actual Gradle `FAILURE` section from the run log.

The widget is a native Android home-screen widget. Add it by long-pressing your launcher home screen, selecting Widgets, and choosing GlassNote Todo.


## Build APK on GitHub

1. Upload the **contents** of this folder to the root of a GitHub repository.
2. Open **Actions** and select **Android APK Build**.
3. Click **Run workflow** (or push to `main`/`master`).
4. When the run succeeds, open the run, scroll to **Artifacts**, and download `glassnote-todo-debug-apk`.
5. Extract the artifact ZIP to get `app-debug.apk`.

If a run fails, expand the **Build debug APK** step and read the first `e:` compiler line or the first `What went wrong:` section. The final stack-trace lines alone usually don't show the root cause.
