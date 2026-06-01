package com.mycompany.irr00_group_project;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Entry point for the Audiobook Player application. */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane(new Label("Audiobook Player"));
        stage.setScene(new Scene(
            root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT));
        stage.setTitle("Audiobook Player");
        stage.show();

        root.setOnMouseClicked(e -> {
            startPlayer(stage);
        });
    }

    private void startPlayer(Stage stage) {
        try {
            AudioPlayer audioPlayer = new AudioPlayer();
            ProgressStore progressStore = new ProgressStore();
            PlayerViewController controller = new PlayerViewController(audioPlayer, progressStore);

            Library library = new Library();
            library.loadFromFolder(Library.DEFAULT_FOLDER);
            if (!library.getAudiobooks().isEmpty()) {
                controller.setBook(library.getAudiobooks().get(0));
            }

            stage.setScene(controller.buildScene());
            stage.setTitle("Audiobook Player");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startSettings(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/mycompany/irr00_group_project/settings.fxml"));
            Parent root = loader.load();

            stage.setScene(new Scene(
                root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT));
            // 2. Set up the scene
            Scene scene = new Scene(
                root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT);
            
            // 3. Display the window
            stage.setScene(scene);
            stage.setTitle("Audiobook Player - Settings");
            stage.setMinWidth(Constants.MIN_WINDOW_WIDTH);
            stage.setMinHeight(Constants.MIN_WINDOW_HEIGHT);
            stage.setMinWidth(Constants.MIN_WINDOW_WIDTH);
            stage.setMinHeight(Constants.MIN_WINDOW_HEIGHT);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
