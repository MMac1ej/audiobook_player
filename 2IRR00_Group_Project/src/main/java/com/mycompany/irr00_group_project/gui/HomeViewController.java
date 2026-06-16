package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;

/**
 * This class handles the stuff that happens on the home view screen to communicate to HomeView.
 * @author Floortje Lusthof
 */
public class HomeViewController {

    //declare variables.
    private App app;
    Stage stage;
    private TimestampStore tsStore;
    private Library library;

    /**
     * Sets the stage, so we can pass it along from App.
     * @param stage is the current stage.
     */
    void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the app, so we can pass it along from App.
     * @param app is the app instance.
     */
    void setApp(App app) {
        this.app = app;
    }

    void setLibrary(Library library) {
        this.library = library;
    }

    /**
     * Sets the TimestampStore, so we can pass it along from App.
     * @param tsStore is the TimestampStore instance.
     */
    void setTimestampStore(TimestampStore tsStore) {
        this.tsStore = tsStore;
    }

    @FXML
    private ListView<Audiobook> recentListView;

    private ArrayList<Audiobook> recentList = new ArrayList<>();
    private List<String> recentListString = new ArrayList<>();

    @FXML
    private Button settingsButton;

    @FXML
    private Button libraryButton;

    @FXML
    private Button episodesButton;

    @FXML
    private Button playerButton;

    /**
     * This will run once, to load the recently listened to audiobooks.
     */
    @FXML
    public void initialize() {
        recentList.clear();
        checkRecentlyListened(); //add the items to the list
        recentListView.setItems(FXCollections.observableArrayList(recentList)); //and show them
    }

    /**
     * retrieves the most recently listened to audiobooks and turns them into a useful recentList.
     *
     */
    private void checkRecentlyListened() {
        recentListString = tsStore.getRecentlyPlayed();
        turnStringsIntoAudiobooks(recentListString);
    }

    /**
     * This method turns a String list into the corresponding Audiobook list.
     *
     * @param list of file paths in string from
     * @return list of audiobooks corresponding to the file paths
     */
    private void turnStringsIntoAudiobooks(List<String> list) {
        Audiobook audiobook;
        for (String pathstring : list) {
            audiobook = library.findAudiobookByPathString(pathstring);
            if (audiobook != null) {
                addToList(recentList, audiobook);
            }
        }
    }

    /**
     * This method adds an object to a list if it's not in there already.
     * If it already is, we do nothing.
     *
     * @param list is the list where to add the audiobook
     * @param audi is the object we want in the list
     */
    private void addToList(ArrayList<Audiobook> list, Audiobook audi) {
        if (audi != null && !list.contains(audi)) {
            list.add(audi);
        }
    }

    /**
     * This opens the settings when the settingsButton has been pressed.
     * @param event is when the button has been pressed.
     */
    @FXML
    void handleOpenSettings(ActionEvent event) {
        app.startSettings(stage);
    }

    /**
     * This opens the library when the libraryButton has been pressed.
     * @param event is when the button has been pressed.
     */
    @FXML
    void handleOpenLibrary(ActionEvent event) {
        app.startAudiobookOverview(stage);
    }

     /**
     * This opens the episode view when the episodesButton has been pressed.
     * @param event is when the button has been pressed.
     */
    @FXML
    void handleOpenEpisodes(ActionEvent event) {
        app.startEpisodesView(stage);
    }

    /**
     * This opens the player when the playerButton has been pressed.
     * @param event is when the button has been pressed.
     */
    @FXML
    void handleOpenPlayer(ActionEvent event) {
        app.startPlayer(stage);
    }

    /**
     * This method is called whenever an audiobook is clicked from the recentListView.
     * It opens the Player View with the selected audiobook.
     *
     * @param event is when an audiobook from recentList has been clicked.
     */
    @FXML
    void handleMouseClicked(MouseEvent event) {
        ObservableList<Audiobook> selectedAudiobook =
            recentListView.getSelectionModel().getSelectedItems();
        if (selectedAudiobook.isEmpty()) {
            return; //if nothing is selected, nothing happens.
        }
        Audiobook audi = selectedAudiobook.get(0);
        app.startPlayerWithAudiobook(stage, audi);
    }

}
