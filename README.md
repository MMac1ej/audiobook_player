# Audiobook Player - 2IRR00 Group 24

A desktop audiobook player built with Java 21 and JavaFX 21, developed as a group assignment for the 2IRR00 Software Design course at TU/e.

## Group Members

| Name | Student ID | Email |
|------|-----------|-------|
| Ignacy Świderski | 2272563 | ignacyswiderski@wp.pl |
| Maciej Małek | ? | m.w.malek@student.tue.nl |
| Tymon Szczepański | ? | tymon.mindstorms@gmail.com |
| Floortje Lusthof | ? | f.n.lusthof@student.tue.nl |
| A.A.E. Slob | ? | a.a.e.slob@student.tue.nl |
| Vincent van Roosmalen | ? | ? |

## Use Case

**UC-Audio-001: Playing an audiobook**

The main use case covers a user selecting an audiobook from their library and listening to an episode.

1. The user opens the app and sees the home screen with recently played audiobooks.
2. The user browses the library and selects an audiobook — the episode list is shown.
3. The user selects an episode — the player opens and playback begins.
4. The user can pause, resume, stop, seek, adjust volume, change playback speed, or set a sleep timer.
5. Progress is automatically saved and restored on next launch.

**Alternative flows:**
- The user navigates between episodes using Previous / Next buttons.
- The user searches for an audiobook by title in the library view.
- The user configures the audiobook folder, theme, and font size in Settings.

**Exception flows:**
- If an audio file cannot be read, an `IOException` is thrown and the player shows an error message instead of stopping silently.

## Design Patterns

### Singleton
**Class:** `Settings.java`

Ensures a single global instance of application settings (audiobook folder path, font size, theme). Implemented with a `private static Settings instance` and a `synchronized getInstance()` method.

### Strategy (Interface)
**Interface:** `PlaybackController.java` | **Implementation:** `AudioPlayer.java`

Abstracts the audio playback backend. `PlayerViewController` depends only on the `PlaybackController` interface, making the playback implementation replaceable without changing the controller.

### Template Method
**Base class:** `PropertiesStore.java` | **Subclasses:** `ProgressStore.java`, `TimestampStore.java`

Defines the common logic for reading and writing `.properties` files. Subclasses extend it to store domain-specific data: episode progress (seconds) and last-played timestamps respectively.

### Dependency Injection
**Example:** `PlayerViewController(AudioPlayer, ProgressStore, TimestampStore)`

Dependencies are passed through constructors and setters rather than created inside the class. `PlayerViewController` receives its `AudioPlayer`, `ProgressStore`, and `TimestampStore` from `App`, making it independent of their concrete creation. This also enables substituting a `StubAudioPlayer` in tests without touching production code.

### MVC (Model-View-Controller)
- **Model:** `Audiobook`, `Episode`, `Library`, `Settings`, `ProgressStore`, `TimestampStore`
- **View:** FXML files (`PlayerView.fxml`, `homeview.fxml`, `EpisodesView.fxml`, `settings.fxml`, `AudiobookOverview.fxml`) with accompanying CSS
- **Controller:** `PlayerViewController`, `HomeViewController`, `EpisodesViewController`, `SettingsController`, `AudiobookOverview`

## Features

- Play, pause, and stop audiobooks (.mp3, .wav, .m4a)
- Browse your audiobook library and search by title
- Episode list per audiobook with automatic duration probing
- Save and restore playback progress across sessions
- Playback speed control (0.5x – 2.0x)
- Sleep timer
- Recently played list on the home screen
- Light, dark, and high-contrast themes
- Configurable font size and audiobook folder path

## Building and Running

Requirements: Java 21, Maven 3.6+

```bash
mvn javafx:run
```

Run tests:

```bash
mvn test
```

Check code style:

```bash
mvn checkstyle:check
```
