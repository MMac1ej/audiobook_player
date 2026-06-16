package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.collections.ObservableList;

/**
 * This class is the UI controller for the episode view page.
 * @author Vincent van Roosmalen
 */
public class EpisodesViewController {

    private static final int CELL_HEIGHT = 48;

    @FXML
    private Button backButton;

    @FXML
    private ListView<Episode> listView;

    private EpisodesView episodesView;
    private Runnable onBack;
    private java.util.function.BiConsumer<Audiobook, Integer> onEpisodeSelected;

    public EpisodesViewController() {
        episodesView = new EpisodesView();
    }
   
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    public void setOnEpisodeSelected(java.util.function.BiConsumer<Audiobook, Integer> callback) {
        this.onEpisodeSelected = callback;
    }

    /** Navigates back to the main view. */
    @FXML
    public void handleBack(ActionEvent event) {
        if (onBack != null) {
            onBack.run();
        }
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
        if (onEpisodeSelected != null) {
            Audiobook audiobook = episodesView.getAudiobook();
            int index = audiobook.getEpisodes().indexOf(episode);
            onEpisodeSelected.accept(audiobook, index);
        }
    }

    /**
    * Initialize the EpisodesViewController.
    */
    @FXML
    public void initialize() {
        listView.setItems(episodesView.getEpisodes());
        Label placeholder = new Label("No audio files found in the selected folder.");
        placeholder.getStyleClass().add("episodes-placeholder");
        listView.setPlaceholder(placeholder);
        listView.setFixedCellSize(CELL_HEIGHT);
        listView.maxHeightProperty().bind(
            Bindings.size(listView.getItems()).multiply(CELL_HEIGHT).add(2)
        );
        listView.setCellFactory(lv -> new ListCell<Episode>() {
            private final Label titleLabel = new Label();
            private final Label durationLabel = new Label();
            private final HBox content = new HBox(titleLabel, durationLabel);

            {
                HBox.setHgrow(titleLabel, Priority.ALWAYS);
                titleLabel.getStyleClass().add("episode-title");
                durationLabel.getStyleClass().add("episode-duration");
                content.getStyleClass().add("episode-cell-content");
                content.setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(Episode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(item.getTitle());
                    durationLabel.setText(item.getFormattedDuration());
                    setGraphic(content);
                }
            }
        });
    }
}
