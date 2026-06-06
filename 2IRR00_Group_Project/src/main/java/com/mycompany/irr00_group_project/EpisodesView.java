package com.mycompany.irr00_group_project;

import java.io.File;
import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is the back end for the UI of the episode view page.
 * For a given audio book, it gives and overview of episode contained within.
 * @author Vincent van Roosmalen
 */
public class EpisodesView {

    Audiobook audiobook;
    
    /**
     * Initializes object.
     * @param folderPath path to the folder with the audio files to be loaded.
     */
    public EpisodesView(Path folderPath) {
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
            Episode episode = new Episode(fileName, filePath);
            audiobook.addEpisode(episode);

        }

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
