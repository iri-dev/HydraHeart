# HydraHeart – your water tracker

A gentle hydration companion designed to make consistency feel effortless and rewarding.

HydraHeart is an Android app built around Hydra, an animated glass heart that fills up with
every drink you log. Hydra reacts to how your day is going, talks to you in the personality
you choose, and grows with you as you level up.

## Features

- Water and drink logging, with 70 drinks in 7 categories and their caffeine, sugar, calcium,
  alcohol, sodium, potassium and magnesium content
- Daily goals worked out from your profile, activity level and climate
- Hydra's heart, which fills with the colours of the drinks you've had, with moods, faces,
  shapes and accessories
- 8 personalities for Hydra's speech, plus speech bubble styles
- Levels, HP, a 60-trophy collection and 30 themes, some unlocked by level or trophy
- History with streaks and insights, mood tracking with a mood calendar, and Hydra's diary
- 41 Learn topics about hydration and the body
- Reminders that keep working after a reboot
- English (US and UK), German and Spanish

## Project layout

| Folder | What it is |
| --- | --- |
| `hydraheart2/` | The HydraHeart app (`com.hydraheart2.app`) |
| `app/` | An earlier version of HydraHeart with a navy theme and serif fonts, kept for reference |
| `branding/` | App icon artwork |

Inside `hydraheart2/src/main/java/com/hydraheart2/app/`:

- `data/` drinks, themes, trophies, Learn topics and the data models
- `viewmodel/` the app state, goals, levels, trophies and Hydra's speech
- `ui/screens/` Home, Log Drink, History, Diary, Mood, Learn, Trophies, Settings, About and onboarding
- `ui/components/` Hydra's heart, frosted glass cards, speech bubbles, buttons, the time picker and the nav bar
- `util/` storage, reminders, haptics and goal calculation

## Building

Requirements: a recent Android Studio (it includes the JDK the build uses) and an Android
device or emulator running Android 8.0 (API 26) or later.

1. Open the project folder in Android Studio and let Gradle sync.
2. Pick the **HydraHeart** run configuration and press Run.

From a terminal:

```
./gradlew :hydraheart2:assembleDebug
```

The APK is written to `hydraheart2/build/outputs/apk/debug/`.

### Tech stack

Kotlin 2.2.10, Jetpack Compose (BOM 2024.12.01, Material 3 1.3.1), DataStore Preferences
with Gson, Core SplashScreen and downloadable Google Fonts (Playfair Display and DM Sans).
minSdk 26, targetSdk 36.

## Privacy

The app has no internet permission and no accounts. Everything it stores stays on the
device, in the app's private DataStore.

---

Private project. Not for distribution.
