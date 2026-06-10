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
    public static final String DEFAULT_MESSAGE = "Search your preferred audiobook";
    boolean errorMessage = false;
    private VBox error;
    
    /** Initialize library instance & load the available current books. */
    public AudiobookOverview() {
        try {
            library = new Library();
            library.loadFromFolder(Library.DEFAULT_FOLDER);
            audiobooks = library.getAudiobooks();
            errorMessage = false;
        } catch (IOException e) {
            System.err.println("Failed to load audiobooks.");
            error = new VBox(new TextField(
                    "Failed to load audiobooksgit add, please check if the file path is correct."));
            errorMessage = true;
            audiobooks = List.of();
        }
    }

    /** Load the audiobook Overview page. */
    public void loadAudiobookOverview(Stage stage) {
        VBox root = new VBox();
        VBox bookHolder = new VBox();
        HBox searchHolder = new HBox();

        searchSetup(searchHolder, bookHolder);
        booksRefresh(bookHolder);
        
        root.getChildren().addAll(searchHolder, bookHolder);
        if (errorMessage) {
            root.getChildren().add(error);
        } 
        stage.setScene(new Scene(root));
        stage.setFullScreenExitHint(""); //removes the message(press esc to exit)
        stage.setFullScreen(true);
        stage.setTitle("Audiobook Overview");
        stage.setMaximized(true);
        stage.show();
    }
    
    /** Setup the search Bar. */
    private void searchSetup(HBox searchHolder, VBox bookHolder) {
        TextField searchBar = new TextField(DEFAULT_MESSAGE);
        searchBar.prefWidthProperty().bind(searchHolder.widthProperty().multiply(0.5));
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
    
    /** Refresh the audiobooks (buttons) that are shown. */
    private void booksRefresh(VBox bookHolder) {
        bookHolder.getChildren().clear();
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
            bookHolder.getChildren().add(audiobookButton);
        }
    }
    
    /** Show audiobooks accoring to Textfield input. */
    public void search(TextField searchBar, VBox bookHolder) {
        String searchValue = searchBar.getText();
        if (searchValue.equals(DEFAULT_MESSAGE)) {
            searchValue = "";
        }
        audiobooks = library.search(searchValue);
        booksRefresh(bookHolder);
    } 
}

