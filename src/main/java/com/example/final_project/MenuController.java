package com.example.final_project;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class MenuController {

    @FXML
    private JFXButton StartButton;
    @FXML
    private JFXButton SettingsButton;
    @FXML
    private JFXButton QuitButton;

    private MediaPlayer mediaPlayer;
    private FXMLLoader scene;

    //Initialize with brightness and music
    @FXML
    public void initialize() {
        stopmusic();
        adjustSceneBrightness();
        playmusic();
    }

    //Game start button
    @FXML
    private void GameButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getRoot().setOpacity(AppState.getInstance().getBrightness());
        AppState.getInstance().setMediaPlayer(mediaPlayer);
        stage.setTitle("Lethal Corporation");
        stage.setScene(scene);
        stage.show();
        stopmusic();
    }

    //Settings button
    @FXML
    private void SettingsButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getRoot().setOpacity(AppState.getInstance().getBrightness());
        AppState.getInstance().setMediaPlayer(mediaPlayer);
        stage.setTitle("Lethal Corporation");
        stage.setScene(scene);
        stage.show();
    }

    //Exit button
    @FXML
    private void ExitButton(ActionEvent event) {
        Platform.exit();
    }

    //Update brightness
    private void adjustSceneBrightness() {
        double brightness = AppState.getInstance().getBrightness();
        double adjustedBrightness = (100 - brightness * 100) / 100.0; // Reversed brightness value
        Scene scene = StartButton.getScene(); // Assuming you want to apply brightness to the current scene
        if (scene != null) {
            scene.getRoot().setOpacity(adjustedBrightness);
        }
    }

    //Play background music
    private void playmusic(){
        String musicFile = "/Library/Music/BGM.mp3";
        URL resource = getClass().getResource(musicFile);
        if (resource != null) {
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            AppState.getInstance().setMediaPlayer(mediaPlayer);
            mediaPlayer.setVolume(AppState.getInstance().getVolume());
            mediaPlayer.play();
        } else {
            System.err.println("Music file not found: " + musicFile);
        }
    }

    //Stop background music
    private void stopmusic(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

}