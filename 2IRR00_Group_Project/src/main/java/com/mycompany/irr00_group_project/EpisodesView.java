package com.mycompany.irr00_group_project;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is the back end for the UI of the episode view page.
 * For a given audio book, it gives and overview of episode contained within.
 * @author Vincent van Roosmalen
 */
public class EpisodesView {

    private Audiobook audiobook;
    
    /**
     * Initializes object.
     * By default displays audio files in main folder
     */
    public EpisodesView() {
        loadFolder(Path.of(""));

    }
    
    /**
     * Set which audio book to display.
     * @param object audio book object to load.
     */
    public void setAudiobook(Audiobook object) {
        audiobook = object;
    }
    
    /**
     * Set which audio book to display based on folder path.
     * @param folderPath file path to folder of the audio book to load.
     */
    public void setAudiobook(Path folderPath) {
        loadFolder(folderPath);
    }

    /**
    * Given a folder path, find all audio files within the current folder
    * and add it to the audio book.
    */
    private void loadFolder(Path folderPath) {
        audiobook = new Audiobook(folderPath.getFileName().toString());

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
                Episode episode = new Episode(fileName, filePath);
                audiobook.addEpisode(episode);
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
        ObservableList<Episode> episodes;
        episodes = FXCollections.observableArrayList();
        episodes.addAll(audiobook.getEpisodes());
        return episodes;
    }

    /**
     * Go to audio player view and pass along what file to play.
     * Feature is not yet implemented.
     */
    public void selectEpisode(Path filePath) {
        //TODO implement behaviour
    }

}
