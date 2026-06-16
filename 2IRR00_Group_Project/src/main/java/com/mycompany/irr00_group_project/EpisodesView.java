package com.mycompany.irr00_group_project;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

/**
 * This class is the back end for the UI of the episode view page.
 * For a given audio book, it gives and overview of episode contained within.
 * @author Vincent van Roosmalen
 */
public class EpisodesView {

    private Audiobook audiobook;
    private final List<MediaPlayer> playerRefs = new ArrayList<>();
    private final ObservableList<Episode> episodes = FXCollections.observableArrayList(
        (Episode e) -> new Observable[]{e.durationProperty()}
    );

    /**
     * Initializes object.
     * By default displays audio files in main folder
     */
    public EpisodesView() {
        loadFolder(Settings.getInstance().getAudiobookFolderPath());
    }

    /**
     * Initializes object with audio book to display.
     * @param object audio book object to load.
     */
    public EpisodesView(Audiobook object) {
        audiobook = object;
        loadDurations();
    }

    /**
     * Initializes object with audio book to display based on folder path.
     * @param folderPath file path to folder of the audio book to load.
     */
    public EpisodesView(Path folderPath) {
        loadFolder(folderPath);

    }

    /**
     * Set which audio book to display.
     * @param object audio book object to load.
     */
    public void setAudiobook(Audiobook object) {
        audiobook = object;
        loadDurations();
    }

    /**
     * Set which audio book to display based on folder path.
     * @param folderPath file path to folder of the audio book to load.
     */
    public void setAudiobook(Path folderPath) {
        loadFolder(folderPath);
    }

    /** Load durations asynchronously for all episodes already in the audiobook. */
    private void loadDurations() {
        playerRefs.forEach(MediaPlayer::dispose);
        playerRefs.clear();
        episodes.setAll(audiobook.getEpisodes());
        for (Episode episode : audiobook.getEpisodes()) {
            String uri = episode.getFilePath().toUri().toString();
            try {
                MediaPlayer player = new MediaPlayer(new Media(uri));
                playerRefs.add(player);
                player.setOnReady(
                    () -> episode.setDuration((long) player.getTotalDuration().toSeconds()));
            } catch (MediaException e) {
                System.err.println("Could not probe duration for: " + uri);
            }
        }
    }

    /**
    * Given a folder path, find all audio files within the current folder
    * and add it to the audio book.
    */
    private void loadFolder(Path folderPath) {
        playerRefs.forEach(MediaPlayer::dispose);
        playerRefs.clear();
        audiobook = new Audiobook(folderPath.getFileName().toString(), folderPath);

        File dir = folderPath.toFile();
        File[] filesInFolder = dir.listFiles();
        if (filesInFolder == null) {
            //TODO implement edge case path is not a folder
            return;
        }
        for (File file : filesInFolder) {
            if (!file.isFile()) {
                continue;
            }

            String fileName = file.getName();
            Path filePath = file.toPath();
            if (isAudioFile(filePath)) {
                String title = fileName.substring(0, fileName.lastIndexOf('.'));
                Episode episode = new Episode(title, filePath);
                audiobook.addEpisode(episode);
            }

        }

        episodes.setAll(audiobook.getEpisodes());
        for (Episode episode : audiobook.getEpisodes()) {
            String uri = episode.getFilePath().toUri().toString();
            try {
                MediaPlayer player = new MediaPlayer(new Media(uri));
                playerRefs.add(player);
                player.setOnReady(
                    () -> episode.setDuration((long) player.getTotalDuration().toSeconds()));
            } catch (MediaException e) {
                System.err.println("Could not probe duration for: " + uri);
            }
        }
    }

    private static boolean isAudioFile(Path path) {
        if (!Files.isRegularFile(path)) {
            return false;
        }
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".mp3") || name.endsWith(".wav");
    }

    /**
     * Get the episodes contained within the audio book, as an observable list.
     *
     * @return ObservableList Containing the episodes of the audio book
     */
    public ObservableList<Episode> getEpisodes() {
        return episodes;
    }

    /** Returns the currently loaded audiobook. */
    public Audiobook getAudiobook() {
        return audiobook;
    }

    /**
     * Go to audio player view and pass along what file to play.
     * Feature is not yet implemented.
     */
    public void selectEpisode(Path filePath) {
        //TODO implement behaviour
    }

}
