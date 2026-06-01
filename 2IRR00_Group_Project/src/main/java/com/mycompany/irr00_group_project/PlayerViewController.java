package com.mycompany.irr00_group_project;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** GUI controller for the player screen. */
public class PlayerViewController {

    private final AudioPlayer audioPlayer;
    private final ProgressStore progressStore;
    private final SleepTimer sleepTimer;
    private Audiobook currentBook;
    private int currentEpisodeIndex = -1;
    private boolean isSeeking = false;
    private Timeline progressTimeline;
    private Label episodeTitleLabel;
    private Slider seekSlider;
    private Label timeLabel;
    private Button playPauseButton;
    private Button stopButton;
    private Button prevButton;
    private Button nextButton;
    private Slider volumeSlider;
    private Button sleepToggleButton;
    private Spinner<Integer> sleepSpinner;

    /**
     * Creates a PlayerViewController.
     *
     * @param audioPlayer the audio playback engine
     * @param progressStore the store for persisting playback progress
     */
    public PlayerViewController(AudioPlayer audioPlayer, ProgressStore progressStore) {
        this.audioPlayer = audioPlayer;
        this.progressStore = progressStore;
        this.sleepTimer = new SleepTimer(() -> Platform.runLater(audioPlayer::pause));
    }

    /**
     * Sets the audiobook to display and play.
     *
     * @param book the audiobook to load
     */
    public void setBook(Audiobook book) {
        this.currentBook = book;
        this.currentEpisodeIndex = (book != null && book.getEpisodeCount() > 0) ? 0 : -1;
    }

    /**
     * Builds and returns the player scene.
     *
     * @return the constructed JavaFX scene
     */
    public Scene buildScene() {
        VBox root = buildLayout();
        wireEvents();
        setupTimeline();
        if (currentBook != null && currentEpisodeIndex >= 0) {
            loadEpisode(currentEpisodeIndex);
        }
        updateNavButtons();
        return new Scene(root, 900, 650);
    }

    private VBox buildLayout() {
        episodeTitleLabel = new Label("No episode loaded");
        episodeTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        seekSlider = new Slider(0, 1, 0);
        seekSlider.setMaxWidth(Double.MAX_VALUE);
        timeLabel = new Label("0:00 / 0:00");
        HBox seekRow = new HBox(8, seekSlider, timeLabel);
        seekRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(seekSlider, Priority.ALWAYS);
        prevButton = new Button("|<");
        playPauseButton = new Button("Play");
        stopButton = new Button("Stop");
        nextButton = new Button(">|");
        HBox transportRow = new HBox(12, prevButton, playPauseButton, stopButton, nextButton);
        transportRow.setAlignment(Pos.CENTER);
        VBox root = new VBox(16,
            episodeTitleLabel, seekRow, transportRow,
            buildSpeedRow(), buildVolumeRow(), buildSleepRow()
        );
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);
        return root;
    }

    private HBox buildSpeedRow() {
        ToggleGroup speedGroup = new ToggleGroup();
        double[] speeds = {0.5, 1.0, 1.5, 2.0};
        HBox row = new HBox(8, new Label("Speed:"));
        row.setAlignment(Pos.CENTER);
        for (double speed : speeds) {
            ToggleButton btn = new ToggleButton(speed + "x");
            btn.setToggleGroup(speedGroup);
            if (speed == 1.0) {
                btn.setSelected(true);
            }
            double s = speed;
            btn.setOnAction(e -> audioPlayer.setSpeed(s));
            row.getChildren().add(btn);
        }
        speedGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null) {
                oldT.setSelected(true);
            }
        });
        return row;
    }

    private HBox buildVolumeRow() {
        volumeSlider = new Slider(0, 1, 1);
        volumeSlider.setPrefWidth(200);
        HBox row = new HBox(8, new Label("Volume:"), volumeSlider);
        row.setAlignment(Pos.CENTER);
        return row;
    }

    private HBox buildSleepRow() {
        sleepSpinner = new Spinner<>(1, 120, 15);
        sleepSpinner.setPrefWidth(70);
        sleepToggleButton = new Button("Start");
        HBox row = new HBox(8, new Label("Sleep timer:"), sleepSpinner,
            new Label("min"), sleepToggleButton);
        row.setAlignment(Pos.CENTER);
        return row;
    }

    private void wireEvents() {
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
                audioPlayer.play();
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
            } else {
                sleepTimer.start((long) sleepSpinner.getValue() * 60L);
                sleepToggleButton.setText("Cancel");
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
        if (!sleepTimer.isRunning()) {
            sleepToggleButton.setText("Start");
        }
        saveProgress();
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
        Episode episode = currentBook.getEpisode(index);
        currentEpisodeIndex = index;
        try {
            audioPlayer.loadEpisode(episode);
            long saved = progressStore.loadProgress(episode.getFilePath().toString());
            if (saved > 0) {
                audioPlayer.seek(saved);
            }
            seekSlider.setValue(saved);
            episodeTitleLabel.setText(episode.getTitle());
        } catch (IOException ex) {
            episodeTitleLabel.setText("Error loading: " + episode.getTitle());
        }
        updateNavButtons();
    }

    private void updateNavButtons() {
        prevButton.setDisable(currentEpisodeIndex <= 0);
        nextButton.setDisable(currentBook == null
            || currentEpisodeIndex >= currentBook.getEpisodeCount() - 1);
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
