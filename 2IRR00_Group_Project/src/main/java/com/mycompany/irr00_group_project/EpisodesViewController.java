package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;

/**
 * This class is the UI controller for the episode view page.
 * @author Vincent van Roosmalen
 */
public class EpisodesViewController {

    @FXML
    private ListView<Episode> listView;
    
    private EpisodesView episodesView;

    public EpisodesViewController() {
        episodesView = new EpisodesView();
    }
    
    
    /**
     * handles UI events,
     * Used to select what episode to play.
     */
    @FXML
    public void handleMouseClicked(MouseEvent event) {

        ObservableList<Episode> selectedEpisodes = listView.getSelectionModel().getSelectedItems();

        //edge case nothing selected
        if (selectedEpisodes.isEmpty()) {
            return;
        }

        Episode episode = selectedEpisodes.get(0);
        Path filePath = episode.getFilePath();
        episodesView.selectEpisode(filePath);
    }

    /**
    * Initialize the EpisodesViewController.
    */
    @FXML
    public void initialize() {
        listView.setItems(episodesView.getEpisodes());

    }
}
