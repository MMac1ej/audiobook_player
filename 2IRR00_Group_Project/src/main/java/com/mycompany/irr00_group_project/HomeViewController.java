package com.mycompany.irr00_group_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;

/**
 * This class handles the stuff that happens on the home view screen to communicate to HomeView.
 * @author Floortje Lusthof
 */
public class HomeViewController {

    App app;
    Stage stage;
    TimestampStore tsStore;
    private static HomeViewController instance;
    private Library library = Library.getInstance();
    Audiobook audi; //for testing

    /**
     * The constructor.
     * @param stage is the object of the main method.
     */
    private HomeViewController() {
        //Commented code is for debugging purposes.
        //System.out.println("New HomeViewController instance created!");
        audi = new Audiobook("Ba", null); //for testing
    }
    
    /**
     * Sets the stage, so we can pass it along from App.
     * @param stage
     */
    void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the app, so we can pass it along from App.
     * @param app
     */
    void setApp(App app) {
        this.app = app;
    }

    /**
     * Sets the TimestampStore, so we can pass it along from App.
     * @param stage
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
    private Button libraryButton;

    public static HomeViewController getInstance() {
        if (instance == null) {
            instance = new HomeViewController();
        }
        return instance;
    }

    @FXML
    /**
     * This will run once, to load the recently listened to audiobooks.
     */
    public void initialize() {
        checkRecentlyListened(); //add the items to the list
        recentListView.setItems(FXCollections.observableArrayList(recentList)); //and show them
    }

    private void checkRecentlyListened() {
        //TODO: turn string list into an audiobook list and pass that along
        //aka: get the audiobooks that are recently listened to
        //and then add them to recentList
        //addToList(recentList, audi);
        recentListString = tsStore.getRecentlyPlayed();
        turnStringsIntoAudiobooks(recentListString);
        addToList(recentList, audi);
    }

    /**
     * This method turns a String list into the corresponding Audiobook list.
     * 
     * @param list of file paths in string from
     * @return list of audiobooks corresponding to the file paths
     */
    private void turnStringsIntoAudiobooks(List<String> list) {
        //TODO: implement method
        Audiobook audiobook;
        for (String pathstring : list) {
            audiobook = library.findAudioBookByPathString(pathstring);
            if (audiobook != null) {
                addToList(recentList, audiobook);
            }
        }
    }

    /**
     * This method adds an object to a list if it's not in there already. If it already is, we do nothing.
     * 
     * @param list is the list where to add the audiobook
     * @param obj is the object we want in the list
     */
    private void addToList(ArrayList<Audiobook> list, Audiobook audi) {
        if (!list.contains(audi) && audi != null) {
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
        //System.out.println("library");
        //open the library
    }

}

