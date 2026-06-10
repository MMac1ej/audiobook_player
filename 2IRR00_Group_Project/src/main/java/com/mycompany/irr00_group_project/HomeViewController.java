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
    private static HomeViewController instance;

    /**
     * The constructor.
     * @param stage is the object of the main method.
     */
    private HomeViewController() {
        //Commented code is for debugging purposes.
        //System.out.println("New HomeViewController instance created!");
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

    @FXML
    private ListView<Audiobook> recentListView;

    private ArrayList<Audiobook> recentList = new ArrayList<>();

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
    public void initialize() {
        //this will run once, and show the audiobooks i suppose
       Audiobook audi = new Audiobook("Ba");
       recentList.add(audi);
       recentListView.setItems(FXCollections.observableArrayList(recentList));
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
