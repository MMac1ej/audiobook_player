package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
    private boolean errorMessage = false;
    private VBox error;
    private Runnable onBack;

    /** Initialize library instance & load the available current books. */
    public AudiobookOverview(Runnable onBack, Library library, boolean loadFromFolder) {
        this.onBack = onBack;
        this.library = library;
        if (loadFromFolder) {
            try {
                library.loadFromFolder(Settings.getInstance().getAudiobookFolderPath());
                audiobooks = library.getAudiobooks();
                errorMessage = false;
            } catch (IOException e) {
                System.err.println("Failed to load audiobooks.");
                error = new VBox(new Label(
                        "Failed to load audiobooks, please check if the file path is correct."));
                errorMessage = true;
                audiobooks = List.of();
            }
        }   else {
            audiobooks = library.getAudiobooks();
            errorMessage = false;
        }

    }

    /** Load the audiobook Overview page. */
    public void loadAudiobookOverview(Stage stage) {
        VBox root = new VBox();
        VBox bookHolder = new VBox();
        bookHolder.getStyleClass().add("book-holder");
        HBox searchHolder = new HBox();
        searchHolder.getStyleClass().add("search-holder");

        backSetup(searchHolder);
        searchSetup(searchHolder, bookHolder);
        booksRefresh(bookHolder);

        root.getChildren().addAll(searchHolder, bookHolder);
        if (errorMessage) {
            root.getChildren().add(error);
        }
        Scene scene = new Scene(root,
                Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT);
        Theme theme = Settings.getInstance().getTheme();
        String cssPath = switch (theme) {
            case DARK ->
                "/com/mycompany/irr00_group_project/AudiobookOverview-dark.css";
            case HIGH_CONTRAST ->
                "/com/mycompany/irr00_group_project/AudiobookOverview-high-contrast.css";
            default ->
                "/com/mycompany/irr00_group_project/AudiobookOverview.css";
        };
        if (getClass().getResource(cssPath) != null) {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
        stage.setScene(scene);
        stage.setTitle("Audiobook Overview");
        stage.show();
    }

    /** Create the back button, when pressed, the previous page is loaded. */
    private void backSetup(HBox searchHolder) {
        // search button setup
        Button backButton = new Button("← Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Go back to homepage.
                if (onBack != null) {
                    onBack.run();
                }
            }
        });
        searchHolder.getChildren().add(backButton);
    }

    /** Setup the search Bar. */
    private void searchSetup(HBox searchHolder, VBox bookHolder) {
        TextField searchBar = new TextField();
        searchBar.setPromptText(Constants.SEARCH_PROMPT);
        searchBar.prefWidthProperty().bind(searchHolder.widthProperty().multiply(0.5));
        searchHolder.getChildren().add(searchBar);

        // search button setup
        Button search = new Button("Search");
        search.getStyleClass().add("search-button");
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                search(searchBar, bookHolder);
            }
        });
        searchHolder.getChildren().add(search);

        // reset button setup
        Button reset = new Button("See all audiobooks");
        reset.getStyleClass().add("search-button");
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
            audiobookButton.getStyleClass().add("audiobook-button");
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

    /** Show audiobooks according to Textfield input. */
    public void search(TextField searchBar, VBox bookHolder) {
        String searchValue = searchBar.getText();
        audiobooks = library.search(searchValue);
        booksRefresh(bookHolder);
    }
}
