package com.mycompany.irr00_group_project;

import java.io.IOException;
import java.nio.file.Files;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/** JavaFX implementation of {@link PlaybackController}. */
public class AudioPlayer implements PlaybackController {

    private MediaPlayer player;
    private Runnable onEndOfMedia;

    /** Creates an AudioPlayer with no episode loaded. */
    public AudioPlayer() {
    }

    @Override
    public void loadEpisode(Episode episode) throws IOException {
        if (!Files.exists(episode.getFilePath())) {
            throw new IOException("File not found: " + episode.getFilePath());
        }
        if (player != null) {
            player.dispose();
        }
        Media media = new Media(episode.getFilePath().toUri().toString());
        player = new MediaPlayer(media);
        player.setOnEndOfMedia(onEndOfMedia);
    }

    @Override
    public void play() {
        if (player == null) {
            return;
        }
        player.play();
    }

    @Override
    public void pause() {
        if (player == null) {
            return;
        }
        player.pause();
    }

    @Override
    public void stop() {
        if (player == null) {
            return;
        }
        player.stop();
    }

    @Override
    public void seek(long seconds) {
        if (player == null) {
            return;
        }
        player.seek(Duration.seconds(seconds));
    }

    @Override
    public void setVolume(double volume) {
        if (volume < 0.0 || volume > 1.0) {
            throw new IllegalArgumentException("Volume must be in [0.0, 1.0]: " + volume);
        }
        if (player == null) {
            return;
        }
        player.setVolume(volume);
    }

    @Override
    public void setSpeed(double rate) {
        if (rate < 0.5 || rate > 2.0) {
            throw new IllegalArgumentException("Speed must be in [0.5, 2.0]: " + rate);
        }
        if (player == null) {
            return;
        }
        player.setRate(rate);
    }

    @Override
    public long getCurrentPosition() {
        if (player == null) {
            return 0;
        }
        return (long) player.getCurrentTime().toSeconds();
    }

    @Override
    public boolean isPlaying() {
        return player != null && player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Registers a callback to invoke when the current episode reaches its end.
     *
     * @param callback the runnable to call on end of media
     */
    public void setOnEndOfMedia(Runnable callback) {
        this.onEndOfMedia = callback;
    }

    /**
     * Returns the total duration of the current episode in seconds, or 0 if unknown.
     *
     * @return total duration in seconds
     */
    public long getDuration() {
        if (player == null) {
            return 0;
        }
        Duration total = player.getTotalDuration();
        if (total == null || total.isUnknown() || total.isIndefinite()) {
            return 0;
        }
        return (long) total.toSeconds();
    }

}
