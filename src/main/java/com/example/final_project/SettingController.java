package com.example.final_project;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.IOException;

public class SettingController {

    @FXML
    private Slider adjustBrightness;
    @FXML
    private Slider volumeSlider;

    private int brightnessValue;
    private int volumeValue;
    public double trueval;

    public void initialize() {
        // Initialize brightness slider
        adjustBrightness.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                brightnessValue = newValue.intValue();
                AppState.getInstance().setBrightness(brightnessValue / 100.0);
                adjustBrightness(brightnessValue);
            }
        });

        // Initialize volume slider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                volumeValue = newValue.intValue();
                adjustVolume(volumeValue);
            }
        });
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
        adjustSceneBrightness(scene);
    }

    private void adjustBrightness(double value) {
        Scene scene = adjustBrightness.getScene();
        trueval = ((100 - value) / 100);
        if (scene != null) {
            scene.getRoot().setOpacity(value / 100);
        }
    }

    private void adjustVolume(double value) {

        }


    private void adjustSceneBrightness(Scene scene) {
        double brightness = AppState.getInstance().getBrightness();
        if (scene != null) {
            scene.getRoot().setOpacity(brightness);
        }
    }
}