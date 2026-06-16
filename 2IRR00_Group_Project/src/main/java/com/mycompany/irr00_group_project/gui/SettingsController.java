package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * Controller for the settings.fxml view. Handles user interactions with the settings UI
 * and updates the Settings singleton accordingly. Also responsible for loading the current
 * settings values into the UI when the view is initialized.
 *
 * @author Tymon Szczepański, 2330113
 */
public class SettingsController {

    @FXML
    private VBox rootVBox;

    @FXML
    private Button browseButton;

    @FXML
    private Button exitSettingsButton;

    @FXML
    private TextField filePathTextField;

    @FXML
    private Slider fontSizeSlider;

    @FXML
    private TextField fontSizeTextField;

    @FXML
    private Button resetDefaultsButton;

    @FXML
    private Button saveSettingsButton;

    @FXML
    private ChoiceBox<Theme> themeChoiceBox;

    @FXML
    private Label statusLabel;

    /** The singleton instance of the Settings class. */
    private final Settings settings = Settings.getInstance();

    private Runnable onBack;

    /** Sets the callback invoked when the user exits settings. */
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    /** Timeline for managing the status label display duration. */
    private Timeline statusTimeline;

    /**
     * Initializes the settings controller and populates the UI with current settings.
     */
    @FXML
    public void initialize() {
        // Get current settings and populate UI elements with their values
        themeChoiceBox.getItems().addAll(Theme.values());

        fontSizeSlider.setValue(settings.getFontSize());
        themeChoiceBox.setConverter(new StringConverter<Theme>() {
            @Override
            public String toString(Theme theme) {
                return theme == null ? "" : theme.getDisplayName();
            }

            @Override
            public Theme fromString(String string) {
                return Theme.fromString(string);
            }
        });
        themeChoiceBox.setValue(settings.getTheme());
        filePathTextField.setText(settings.getAudiobookFolderPath().toString());
        fontSizeTextField.setText(Integer.toString(settings.getFontSize()));

        rootVBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                applyThemeToScene(settings.getTheme());
            }
        });

        // Add listeners to update settings when UI elements change
        fontSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                settings.setFontSize(newVal.intValue());
                fontSizeTextField.setText(Integer.toString(newVal.intValue()));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid font size: " + newVal);
            }
        });
        themeChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            settings.setTheme(Theme.valueOf(newVal.name()));
            applyThemeToScene(newVal);
        });
    }

    /**
     * Updates the UI elements with the current settings values.
     */
    private void updateUIElements() {
        fontSizeSlider.setValue(settings.getFontSize());
        fontSizeTextField.setText(Integer.toString(settings.getFontSize()));
        themeChoiceBox.setValue(settings.getTheme());
        filePathTextField.setText(settings.getAudiobookFolderPath().toString());
    }

    /**
     * Helper method that waits for a specified number of seconds
     * and then clears the status label text.
     */
    private void clearStatusLabelAfterDelay(double seconds) {
        if (statusTimeline != null) {
            statusTimeline.stop(); // Stop any existing timeline to reset the timer
        }

        statusTimeline = new Timeline(
            new KeyFrame(Duration.seconds(seconds), event -> {
                statusLabel.setText("");
            })
        );
        statusTimeline.setCycleCount(1); // Ensure it only runs once per save click
        statusTimeline.play();
    }

    /**
     * Handles the action for the browse button.
     * @param event the action event
     */
    @FXML
    void handleBrowseAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Audiobook Folder");

        Stage stage = (Stage) filePathTextField.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            filePathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Handles the action for the reset defaults button.
     * @param event the action event
     */
    @FXML
    void handleResetDefaults(ActionEvent event) {
        settings.setDefaults();
        updateUIElements(); // Reload UI with default values
        statusLabel.setStyle(Constants.SAVE_SUCCESS_MESSAGE_STYLE);
        statusLabel.setText(Constants.RESET_SUCCESS_MESSAGE);
        clearStatusLabelAfterDelay(Constants.STATUS_MESSAGE_DURATION_SECONDS);
    }

    /**
     * Handles the action for the save settings button.
     * @param event the action event
     */
    @FXML
    void handleSaveAction(ActionEvent event) {
        Theme selectedTheme = themeChoiceBox.getValue();
        try {
            Path audiobookFolderPath = Paths.get(filePathTextField.getText());
            settings.setAudiobookFolderPath(audiobookFolderPath);
            settings.setTheme(selectedTheme);
            settings.setFontSize((int) fontSizeSlider.getValue());
            statusLabel.setStyle(Constants.SAVE_SUCCESS_MESSAGE_STYLE);
            statusLabel.setText(Constants.SAVE_SUCCESS_MESSAGE);
        } catch (IllegalArgumentException e) {
            statusLabel.setStyle(Constants.SAVE_FAILED_MESSAGE_STYLE);
            statusLabel.setText(Constants.SAVE_FAILED_MESSAGE);
            System.err.println("Invalid audiobook folder path: " + e.getMessage());
        }
        clearStatusLabelAfterDelay(Constants.STATUS_MESSAGE_DURATION_SECONDS);
    }

    /**
     * Handles the action for the exit settings button.
     * TODO: Returns to the main application window without saving changes.
     * Needs HomeView to be implemented.
     * @param event the action event
     */
    @FXML
    void handleExitSettings(ActionEvent event) {
        if (onBack != null) {
            onBack.run();
        }
    }

    private void applyThemeToScene(Theme theme) {
        javafx.scene.Scene scene = rootVBox.getScene();
        if (scene == null || theme == null) {
            return;
        }
        scene.getStylesheets().setAll(theme.getCssPath());
    }
}
