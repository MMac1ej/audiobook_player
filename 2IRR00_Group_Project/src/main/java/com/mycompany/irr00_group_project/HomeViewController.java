package com.mycompany.irr00_group_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * This class handles the stuff that happens on the home view screen.
 */
public class HomeViewController {

    @FXML
    private ListView<Audiobook> recentListView;

    @FXML
    private Button settingsButton;
    private Button libraryButton;

    @FXML
    public void initialize() {
        //this will run once, and show the audiobooks i suppose
        //i am way too tired and will leave this for later
        //bc
        //AAAAAAAAAAAAAAAAAAAAAA
    } 

    /**
     * This opens the settings when the settingsButton has been pressed.
     * @param event is when the button has been pressed.
     */
    @FXML
    void handleOpenSettings(ActionEvent event) {
        //System.out.println("settings");
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
