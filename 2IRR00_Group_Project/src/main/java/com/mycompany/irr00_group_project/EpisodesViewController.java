package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;

public class EpisodesViewController {

    private EpisodesView episodesView;

    EpisodesViewController(Path folderPath) {
        episodesView = new EpisodesView(folderPath);
    }

    @FXML
    private ListView<Episode> listView;

    @FXML
    void handleMouseClicked(MouseEvent event) {

        ObservableList<Episode> selectedEpisodes = listView.getSelectionModel().getSelectedItems();

        //edge case nothing selected
        if (selectedEpisodes.isEmpty()) {
            return;
        }

        Episode episode = selectedEpisodes.get(0);
        Path filePath = episode.getFilePath();
        episodesView.selectEpisode(filePath);
    }

    @FXML
    //run once when set up
    public void initialize() {
        listView.setItems(episodesView.getEpisodes());

    }
}
