package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.*;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** FXML controller for the audiobook library overview screen. */
public class AudiobookOverview {

    @FXML private TextField searchBar;
    @FXML private VBox bookHolder;

    private Library library;
    private Runnable onBack;
    private Consumer<Audiobook> onAudiobookSelected;
    private List<Audiobook> currentAudiobooks = List.of();

    /** Called automatically by FXMLLoader after @FXML fields are injected. */
    @FXML
    public void initialize() {
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> search(newVal));
    }

    /** Sets the callback invoked when the user presses the back button. */
    public void setOnBack(Runnable callback) {
        this.onBack = callback;
    }

    /**
     * Sets the callback invoked when the user selects an audiobook.
     * @param callback receives the selected audiobook
     */
    public void setOnAudiobookSelected(Consumer<Audiobook> callback) {
        this.onAudiobookSelected = callback;
    }

    /**
     * Loads audiobooks from the folder configured in Settings and refreshes the view.
     * @param lib the library instance to populate
     */
    public void setLibrary(Library lib) {
        this.library = lib;
        try {
            library.loadFromFolder(Settings.getInstance().getAudiobookFolderPath());
        } catch (IOException e) {
            System.err.println("Failed to load audiobooks.");
        }
        currentAudiobooks = library.getAudiobooks();
        booksRefresh();
    }

    /**
     * Filters the visible audiobooks by the given query string.
     * An empty query shows all audiobooks.
     * @param query the search string
     */
    public void search(String query) {
        currentAudiobooks = library != null ? library.search(query) : List.of();
        booksRefresh();
    }

    /** Returns the audiobooks currently shown in the list (for testing). */
    public List<Audiobook> getCurrentAudiobooks() {
        return currentAudiobooks;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        if (onBack != null) {
            onBack.run();
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        search(searchBar.getText());
    }

    @FXML
    private void handleReset(ActionEvent event) {
        searchBar.clear();
        search("");
    }

    private void booksRefresh() {
        if (bookHolder == null) {
            return;
        }
        bookHolder.getChildren().clear();
        for (Audiobook audiobook : currentAudiobooks) {
            Button btn = new Button(audiobook.getTitle());
            btn.getStyleClass().add("audiobook-button");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                if (onAudiobookSelected != null) {
                    onAudiobookSelected.accept(audiobook);
                }
            });
            bookHolder.getChildren().add(btn);
        }
    }
}
