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

public class MenuController {

    @FXML
    private ImageView welcomeImageView;
    @FXML
    private JFXButton button1;
    @FXML
    private JFXButton button2;
    @FXML
    private JFXButton button3;



    @FXML
    public void initialize() {
        // Play background music


        // Fade transition for the ImageView
        FadeTransition fadeWelcome = new FadeTransition(Duration.seconds(4), welcomeImageView);
        fadeWelcome.setFromValue(1.0);
        fadeWelcome.setToValue(0);

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
        fadeWelcome.play();
        fadeButton1.play();
        fadeButton2.play();
        fadeButton3.play();

        System.out.println("Transitions started");
    }



    @FXML
    private void handleExitButton(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void SettingsButton(ActionEvent event) throws IOException {
        // Get the current stage using the button that was clicked
        Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();

        // Load the new scene from the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);

        // Set the new scene on the stage and show it
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }
}