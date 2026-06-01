package com.mycompany.irr00_group_project;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Shows and overview of the available audiobooks. */
public class AudiobookOverview {
    private Library library;
    private List<Audiobook> audiobooks;

    /** Initialize library instance & load the available current books. */
    public AudiobookOverview() throws IOException {
        library = new Library();
        library.loadFromFolder(Library.DEFAULT_FOLDER);
        audiobooks = library.getAudiobooks();
    }
    
    public void start(Stage stage) {
        // create new stackpane
        StackPane root = new StackPane();
        // create the search bar
        searchBar(root);
        // show the audiobooks
        createButtons(root);
        // set the stage
        stage.setScene(new Scene(root, 900, 650));
        stage.setTitle("Audiobook Overview");
        stage.setMaximized(true);
        stage.show();
    }
    
    public void searchBar(StackPane root) {
        TextField searchBar = new TextField("Search your prefered audiobook");
        root.getChildren().add(searchBar);
        Button search = new Button("Search");
        root.getChildren().add(search);
        Button reset = new Button("See all audiobooks");
        root.getChildren().add(reset);
    }
    
    public void search(TextField searchBar, ActionEvent event) {
        String searchValue = searchBar.getText();
        audiobooks = library.search(searchValue);
    }
    
    private void createButtons(StackPane root) {
        for (Audiobook audiobook : audiobooks) {
            Button audiobookButton = new Button(audiobook.getTitle());
            root.getChildren().add(audiobookButton);
        }
    }   
}


