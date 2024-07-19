package com.example.final_project;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SettingController {

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
}