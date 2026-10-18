# Tend - Todo and Notes for Android

A calm, glass-styled todo and notes app built with Kotlin and Jetpack Compose.

## What is inside

- Tasks: add, edit, delete, status (To do / In progress / Done), priorities, notes, due dates, search, filters and live stats
- Notes: create, edit, pin, search and delete
- Home screen widget: shows your open tasks at a glance, tap it to open the app
- Offline first: everything is stored on the device, no account needed
- Glass UI: soft gradient background, translucent cards and serif display type

## Get the APK

1. Open the Actions tab of this repository
2. Pick the latest "Build Android APK" run (every push starts one; you can also press "Run workflow")
3. Download the artifact named tend-debug-apk and unzip it
4. Copy tend-debug.apk to your phone and install it (allow installs from this source when asked)

The debug APK is signed with the standard Android debug key, so it installs directly. Publishing to Google Play needs a release key.

## Build it yourself

JDK 17 and Android SDK 34 are required.

    gradle assembleDebug

The APK appears at app/build/outputs/apk/debug/app-debug.apk

## Add the widget

Long press an empty spot on your home screen, choose Widgets, find Tend and drop the tasks widget anywhere.

## Notes

There are no secrets or keys in this repository.
