package com.example.lethal_corporation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingController {

    @FXML
    private Slider adjustBrightness;

    @FXML
    private Slider adjustVolume;

    public void initialize() {
        // Initialize brightness slider
        adjustBrightness.setValue(AppState.getInstance().getBrightness() * 100);
        adjustBrightness.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                double brightnessValue = newValue.doubleValue();
                AppState.getInstance().setBrightness(brightnessValue / 100.0);
                adjustSceneBrightness();
            }
        });

        // Initialize volume slider
        adjustVolume.setValue(AppState.getInstance().getVolume() * 100);
        adjustVolume.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                double volumeValue = newValue.doubleValue();
                AppState.getInstance().setVolume(volumeValue / 100.0);
            }
        });
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) adjustBrightness.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getRoot().setOpacity(AppState.getInstance().getBrightness());
        stage.setTitle("Lethal Corporation");
        stage.setScene(scene);
        stage.show();
        adjustSceneBrightness();
    }

    private void adjustSceneBrightness() {
        Scene scene = adjustBrightness.getScene();
        if (scene != null) {
            scene.getRoot().setOpacity(AppState.getInstance().getBrightness());
        }
    }
}
