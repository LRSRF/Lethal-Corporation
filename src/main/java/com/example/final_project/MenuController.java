package com.example.final_project;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;


public class MenuController {

    @FXML
    private JFXButton button1;
    @FXML
    private JFXButton button2;
    @FXML
    private JFXButton button3;

    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        // Play background music

        // Fade transition for the ImageView

        // Fade transitions for the buttons
        FadeTransition fadeButton1 = new FadeTransition(Duration.seconds(5), button1);
        fadeButton1.setFromValue(0.0);
        fadeButton1.setToValue(1.0);

        FadeTransition fadeButton2 = new FadeTransition(Duration.seconds(5), button2);
        fadeButton2.setFromValue(0.0);
        fadeButton2.setToValue(1.0);

        FadeTransition fadeButton3 = new FadeTransition(Duration.seconds(5), button3);
        fadeButton3.setFromValue(0.0);
        fadeButton3.setToValue(1.0);

        // Start the transitions

        fadeButton1.play();
        fadeButton2.play();
        fadeButton3.play();

        System.out.println("Transitions started");

        // Adjust brightness for this scene
        adjustSceneBrightness();
        playmusic();
    }

    @FXML
    private void handleExitButton(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void SettingsButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void GameButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.show();
        stopmusic();
    }

    private void adjustSceneBrightness() {
        double brightness = AppState.getInstance().getBrightness();

    }
    private void playmusic(){
        String musicFile = "/Library/Music/BGM.mp3"; // Update this path as needed
        URL resource = getClass().getResource(musicFile);
        if (resource != null) {
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
            mediaPlayer.play();
        } else {
            System.err.println("Music file not found: " + musicFile);
        }
    }
    private void stopmusic(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

}