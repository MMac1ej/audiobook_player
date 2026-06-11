package com.mycompany.irr00_group_project;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** GUI controller for the player screen. */
public class PlayerViewController {

    private final AudioPlayer audioPlayer;
    private final ProgressStore progressStore;
    private final TimestampStore timestampStore;
    private final SleepTimer sleepTimer;
    private Audiobook currentBook;
    private int currentEpisodeIndex = -1;
    private boolean isSeeking = false;
    private boolean episodeEnded = false;
    private boolean wasSleepTimerRunning = false;
    private Timeline progressTimeline;
    private long sleepStartTime;
    private long sleepTotalSeconds;
    private final Settings settings = Settings.getInstance();
    private Runnable onBack;

    @FXML private Button backButton;
    @FXML private Label episodeTitleLabel;
    @FXML private Slider seekSlider;
    @FXML private Label timeLabel;
    @FXML private Button playPauseButton;
    @FXML private Button stopButton;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Slider volumeSlider;
    @FXML private Button sleepToggleButton;
    @FXML private Spinner<Integer> sleepSpinner;
    @FXML private Label sleepCounterLabel;
    @FXML private ToggleButton speed05Button;
    @FXML private ToggleButton speed10Button;
    @FXML private ToggleButton speed15Button;
    @FXML private ToggleButton speed20Button;
    @FXML private ToggleGroup speedGroup;

    /**
     * Creates a PlayerViewController.
     *
     * @param audioPlayer the audio playback engine
     * @param progressStore the store for persisting playback progress
     * @param timestampStore the store for persisting audiobook timestamp
     */
    public PlayerViewController(
            AudioPlayer audioPlayer, ProgressStore progressStore, TimestampStore timestampStore) {
        this.audioPlayer = audioPlayer;
        this.progressStore = progressStore;
        this.timestampStore = timestampStore;
        this.sleepTimer = new SleepTimer(() -> Platform.runLater(() -> {
            audioPlayer.pause();
            saveProgress();
        }));
    }

    /**
     * Sets the callback invoked when the user navigates back to the main view.
     *
     * @param onBack the runnable to call when the back button is pressed
     */
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    /**
     * Loads the first audiobook from the default library folder.
     *
     * @throws IOException if the folder cannot be read
     */
    public void loadDefaultBook() throws IOException {
        Library library = new Library();
        library.loadFromFolder(settings.getAudiobookFolderPath());
        if (!library.getAudiobooks().isEmpty()) {
            setBook(library.getAudiobooks().get(0));
        }
    }

    /**
     * Sets the audiobook to display and play.
     *
     * @param book the audiobook to load
     */
    public void setBook(Audiobook book) {
        this.currentBook = book;
        this.currentEpisodeIndex = (book != null && book.getEpisodeCount() > 0) ? 0 : -1;

        if (book != null && book.getPath() != null) {
            this.timestampStore.saveTimestamp(book.getPath().toString());
        }
    }

    /**
     * Builds and returns the player scene.
     *
     * @return the constructed JavaFX scene
     * @throws IOException if the FXML file cannot be loaded
     */
    public Scene buildScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerView.fxml"));
        loader.setController(this);
        VBox root = loader.load();
        if (currentBook != null && currentEpisodeIndex >= 0) {
            loadEpisode(currentEpisodeIndex);
        }
        updateNavButtons();
        return new Scene(root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT);
    }

    @FXML
    private void initialize() {
        wireEvents();
        setupTimeline();
        audioPlayer.setOnEndOfMedia(() -> Platform.runLater(() -> {
            int next = currentEpisodeIndex + 1;
            if (currentBook != null && next < currentBook.getEpisodeCount()) {
                navigateEpisode(1);
            } else {
                episodeEnded = true;
            }
        }));
    }

    private void wireEvents() {
        backButton.setOnAction(e -> {
            audioPlayer.stop();
            saveProgress();
            if (onBack != null) {
                onBack.run();
            }
        });
        seekSlider.setOnMousePressed(e -> isSeeking = true);
        seekSlider.setOnMouseReleased(e -> {
            audioPlayer.seek((long) seekSlider.getValue());
            isSeeking = false;
        });
        playPauseButton.setOnAction(e -> {
            if (audioPlayer.isPlaying()) {
                audioPlayer.pause();
                saveProgress();
            } else {
                long pos = audioPlayer.getCurrentPosition();
                long dur = audioPlayer.getDuration();
                if (episodeEnded || (dur > 0 && pos >= dur)) {
                    episodeEnded = false;
                    replayFromStart();
                } else {
                    audioPlayer.play();
                }
            }
        });
        stopButton.setOnAction(e -> {
            audioPlayer.stop();
            saveProgress();
        });
        prevButton.setOnAction(e -> navigateEpisode(-1));
        nextButton.setOnAction(e -> navigateEpisode(1));
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            audioPlayer.setVolume(newVal.doubleValue()));
        sleepToggleButton.setOnAction(e -> {
            if (sleepTimer.isRunning()) {
                sleepTimer.cancel();
                sleepToggleButton.setText("Start");
                sleepCounterLabel.setText("");
            } else {
                sleepTotalSeconds = (long) sleepSpinner.getValue() * 60L;
                sleepStartTime = System.currentTimeMillis();
                sleepTimer.start(sleepTotalSeconds);
                sleepToggleButton.setText("Cancel");
            }
        });
        wireSpeedButtons();
    }

    private void wireSpeedButtons() {
        speed05Button.setOnAction(e -> audioPlayer.setSpeed(0.5));
        speed10Button.setOnAction(e -> audioPlayer.setSpeed(1.0));
        speed15Button.setOnAction(e -> audioPlayer.setSpeed(1.5));
        speed20Button.setOnAction(e -> audioPlayer.setSpeed(2.0));
        speedGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null) {
                oldT.setSelected(true);
            }
        });
    }

    private void setupTimeline() {
        progressTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        progressTimeline.setCycleCount(Timeline.INDEFINITE);
        progressTimeline.play();
    }

    private void tick() {
        long pos = audioPlayer.getCurrentPosition();
        long dur = audioPlayer.getDuration();
        if (!isSeeking) {
            if (dur > 0) {
                seekSlider.setMax(dur);
            }
            seekSlider.setValue(pos);
            timeLabel.setText(formatTime(pos) + " / " + formatTime(dur));
        }
        playPauseButton.setText(audioPlayer.isPlaying() ? "Pause" : "Play");
        if (sleepTimer.isRunning()) {
            long elapsed = (System.currentTimeMillis() - sleepStartTime) / 1000;
            sleepCounterLabel.setText(formatTime(elapsed) + " / " + formatTime(sleepTotalSeconds));
            wasSleepTimerRunning = true;
        } else if (wasSleepTimerRunning) {
            sleepCounterLabel.setText("");
            sleepToggleButton.setText("Start");
            wasSleepTimerRunning = false;
        }
    }

    private void navigateEpisode(int delta) {
        if (currentBook == null) {
            return;
        }
        int next = currentEpisodeIndex + delta;
        if (next < 0 || next >= currentBook.getEpisodeCount()) {
            return;
        }
        saveProgress();
        loadEpisode(next);
        audioPlayer.play();
    }

    private void loadEpisode(int index) {
        episodeEnded = false;
        Episode episode = currentBook.getEpisode(index);
        try {
            audioPlayer.loadEpisode(episode);
            currentEpisodeIndex = index;
            long saved = progressStore.loadProgress(episode.getFilePath().toString());
            if (saved > 0) {
                audioPlayer.seek(saved);
            }
            seekSlider.setValue(saved);
            episodeTitleLabel.setText(episode.getTitle());
        } catch (IOException | RuntimeException ex) {
            episodeTitleLabel.setText("Error loading: " + episode.getTitle());
        }
        updateNavButtons();
    }

    private void updateNavButtons() {
        prevButton.setDisable(currentEpisodeIndex <= 0);
        nextButton.setDisable(currentBook == null
            || currentEpisodeIndex >= currentBook.getEpisodeCount() - 1);
    }

    private void replayFromStart() {
        if (currentBook == null || currentEpisodeIndex < 0) {
            return;
        }
        episodeEnded = false;
        Episode episode = currentBook.getEpisode(currentEpisodeIndex);
        try {
            audioPlayer.loadEpisode(episode);
            seekSlider.setValue(0);
            audioPlayer.play();
        } catch (IOException | RuntimeException ex) {
            episodeTitleLabel.setText("Error loading: " + episode.getTitle());
        }
    }

    private void saveProgress() {
        if (currentBook == null || currentEpisodeIndex < 0) {
            return;
        }
        Episode episode = currentBook.getEpisode(currentEpisodeIndex);
        progressStore.saveProgress(
            episode.getFilePath().toString(),
            audioPlayer.getCurrentPosition()
        );
    }

    private static String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

}
