package com.mycompany.irr00_group_project;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/** Entry point for the Audiobook Player application. */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
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
    }
}
