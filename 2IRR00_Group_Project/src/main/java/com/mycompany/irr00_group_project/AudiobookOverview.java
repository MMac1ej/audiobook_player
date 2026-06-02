package com.mycompany.irr00_group_project;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Shows and overview of the available audiobooks. */
public class AudiobookOverview {
    private Library library;
    private List<Audiobook> audiobooks;
    public static final String DEFAULT_MESSAGE = "Search your prefered audiobook";

    /** Initialize library instance & load the available current books. */
    public AudiobookOverview() throws IOException {
        library = new Library();
        library.loadFromFolder(Library.DEFAULT_FOLDER);
        audiobooks = library.getAudiobooks();
    }
    
    public void loadAudiobookOverview(Stage stage) {
        VBox root = new VBox();
        VBox bookHolder = new VBox();
        HBox searchHolder= new HBox();

        searchSetup(searchHolder, bookHolder);
        booksRefresh(bookHolder);

        root.getChildren().addAll(searchHolder, bookHolder);
        stage.setScene(new Scene(root, 900, 650));
        stage.setTitle("Audiobook Overview");
        stage.setMaximized(true);
        stage.show();
    }
    
    private void searchSetup(HBox searchHolder, VBox bookHolder) {
        TextField searchBar = new TextField(DEFAULT_MESSAGE);
        searchHolder.getChildren().add(searchBar);
        
        // search button setup
        Button search = new Button("Search");
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                search(searchBar, bookHolder);
            }
        });
        searchHolder.getChildren().add(search);
        
        // reset button setup
        Button reset = new Button("See all audiobooks");
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchBar.clear();
                search(searchBar, bookHolder);
            }
        });
        searchHolder.getChildren().add(reset);
    }
    
    private void booksRefresh(VBox root) {
        for (Audiobook audiobook : audiobooks) {
            Button audiobookButton = new Button(audiobook.getTitle());
            audiobookButton.setMaxWidth(Double.MAX_VALUE); // button width = box width
            // set action
            audiobookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {
                    // got to Episode Page.
                }
            });
            root.getChildren().add(audiobookButton);
        }
    }
    
    public void search(TextField searchBar, VBox bookHolder) {
        String searchValue = searchBar.getText();
        if (searchValue.equals(DEFAULT_MESSAGE)) {
            searchValue = "";
        }
        audiobooks = library.search(searchValue);
        booksRefresh(bookHolder);
    } 
}

