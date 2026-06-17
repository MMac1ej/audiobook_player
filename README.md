# Audiobook Player

An accessible desktop audiobook player targeting elderly users. The application lets a user load audio files from a local folder, browse their library, select an audiobook and individual episodes, and play them back with full transport controls (play, pause, stop, seek, volume, speed). Listening progress is automatically saved per episode and restored on the next launch. The player also supports a sleep timer and three visual themes (light, dark, high-contrast).

**Group:** 24

---

## Group Members

| Name | Student ID | GitHub username |
|------|-----------|-----------------|
| Ignacy Świderski | 2272563 | Igifigi |
| Maciej Małek | 2294192 | MMac1ej / mmac13k |
| Tymon Szczepański | 2330113 | TheWiseArdoni |
| Floortje Lusthof | 2316137 | Floortje |
| Amber Slob | 2259656 | Slob |
| Vincent van Roosmalen | 1679473 | VincentvRoosmalen |

---

## Use Case

The primary use case is UC-Audio-001 – Playing audiobooks. It includes the main scenario, an alternative scenario (adjusting playback settings), and an exception scenario (unreadable audio file).

Main scenario (step-by-step):
1. The system displays an overview of available audiobooks (FR-01).
2. The user selects an audiobook.
3. The system shows an overview of all episodes (FR-02).
4. The user selects a specific episode.
5. The system begins audio playback (FR-06) and tracks progress (FR-03).

---

## Design Patterns

### 1. Singleton -- `Settings`
`Settings` is a singleton. A single instance holds the user's preferences (audiobook folder path, font size, theme) and persists them to disk. This ensures all parts of the application share the same configuration without passing it through constructors.

### 2. Strategy (Interface) -- `PlaybackController`/`AudioPlayer`
`PlaybackController` is an interface that defines the playback contract: `play()`, `pause()`, `stop()`, `seek()`, `setVolume()`, `setSpeed()`. `AudioPlayer` is the concrete implementation using the JavaFX `MediaPlayer`. Controllers depend only on the interface, so the underlying playback engine can be replaced or mocked in tests without changing any UI code.

### 3. Template Method -- `PropertiesStore`, `ProgressStore`, `TimestampStore`
`PropertiesStore` is an abstract class that handles the mechanics of reading and writing a `.properties` file (load on construction, save after every write). `ProgressStore` and `TimestampStore` extend it and add domain-specific logic (episode progress tracking and recently-played history, respectively) without duplicating file I/O code.

### 4. Model-View-Controller (MVC)
The application is structured around MVC throughout:
- **Model:** `Audiobook`, `Episode`, `Library`, `Settings`, `ProgressStore`, `TimestampStore`, `SleepTimer`.
- **View:** FXML files (`homeview.fxml`, `PlayerView.fxml`, `EpisodesView.fxml`, `settings.fxml`) plus CSS theme files.
- **Controller:** `HomeViewController`, `PlayerViewController`, `EpisodesViewController`, `SettingsController`, `AudiobookOverview` -- each wired to its FXML view and delegating business logic to model classes.

`App` acts as the application entry point and scene router, injecting shared model objects into controllers via explicit setter methods (constructor-style dependency injection).

---

## How to Build and Run

Requirements: Java 21, Maven 3.x.

```
mvn javafx:run
```

Audiobooks are loaded from `~/Audiobooks` by default. The folder can be changed in the Settings screen. Supported formats: `.mp3`, `.wav`, `.m4a`.

To run the tests:

```
mvn test
```
