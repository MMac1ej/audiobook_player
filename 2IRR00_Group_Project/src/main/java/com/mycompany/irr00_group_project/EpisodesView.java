/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.irr00_group_project;

import java.io.File;
import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Vincent van Roosmalen
 */
public class EpisodesView {

    Audiobook audiobook;

    EpisodesView(Path folderPath) {
        //TODO import audibook episodes based on path dir
        loadFolder(folderPath);

    }

    /*Given a folder path, find all audio files within the current folder
    and add it to the audiobook**/
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
     * Get the episodes contained within the audio book, as an observable list
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
     * Go to audio player view and pass along what file to play TODO Implement behavior
     */
    public void selectEpisode(Path filePath) {
        //TODO implement behaviour
    }

}
