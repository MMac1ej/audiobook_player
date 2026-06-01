package com.mycompany.irr00_group_project;

import java.io.File;
import java.nio.file.Path;

import java.nio.file.Paths;

import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Controller for the settings.fxml view. Handles user interactions with the settings UI
 * and updates the Settings singleton accordingly. Also responsible for loading the current
 * settings values into the UI when the view is initialized.
 * 
 * @author Tymon Szczepański, 2330113
 * 
 * TODO: add back to home screen button.
 */
public class SettingsController {

    @FXML
    private Button browseButton;

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

    /**
     * Initializes the settings controller and populates the UI with current settings.
     */
    @FXML
    public void initialize() {
        // Get current settings and populate UI elements with their values
        Settings settings = Settings.getInstance();

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
            try {
                settings.setTheme(Theme.valueOf(newVal.name()));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid theme: " + newVal);
            }
        });
    }

    /**
     * Handles the action for the browse button.
     * @param event the action event
     */
    @FXML
    void handleBrowseAction(ActionEvent event) {
        System.out.println("Opening file explorer..."); //debug print
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Audiobook Folder");

        Stage stage = (Stage) filePathTextField.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            filePathTextField.setText(selectedDirectory.getAbsolutePath());
            System.out.println(
                "Selected folder: " + selectedDirectory.getAbsolutePath()); //debug print
        }
    }

    /**
     * Handles the action for the reset defaults button.
     * @param event the action event
     */
    @FXML
    void handleResetDefaults(ActionEvent event) {
        Settings settings = Settings.getInstance();
        settings.setDefaults();
        initialize(); // Reload UI with default values
    }

    /**
     * Handles the action for the save settings button.
     * @param event the action event
     */
    @FXML
    void handleSaveAction(ActionEvent event) {
        Settings settings = Settings.getInstance();
        Path audiobookFolderPath = Paths.get(filePathTextField.getText());
        Theme selectedTheme = themeChoiceBox.getValue();

        settings.setAudiobookFolderPath(audiobookFolderPath);
        settings.setTheme(selectedTheme);
        settings.setFontSize((int) fontSizeSlider.getValue());

        System.out.println("Settings saved!"); //debug print
    }

}
