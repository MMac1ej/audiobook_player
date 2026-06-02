package com.mycompany.irr00_group_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class HomeViewController {

    @FXML
    private ListView<Audiobook> recentListView;

    @FXML
    private Button settingsButton;

    @FXML
    public void initialize() {
        //this will run once, and show the audiobooks i suppose
        //i am way too tired and will leave this for later
        //bc
        //AAAAAAAAAAAAAAAAAAAAAA
    } 

    @FXML
    void handleOpenSettings(ActionEvent event) {
        //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA");
    }

}
