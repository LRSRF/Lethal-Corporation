package com.example.final_project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class Game {


    @FXML
    private Canvas gameCanvas;
    @FXML
    private Button backButton;

    private GraphicsContext gc;
    private Image spriteImage;
    private double spriteX = 100;
    private double spriteY = 350;
    private double spriteSpeed = 5.0;
    private double spriteWidth;
    private double spriteHeight;

    // Define obstacles
    private double obstacle1X = 0;
    private double obstacle1Y = 0;
    private double obstacle1Width = 382;
    private double obstacle1Height = 331;

    private double obstacle2X = 590;
    private double obstacle2Y = 0;
    private double obstacle2Width = 690;
    private double obstacle2Height = 311;

    private double obstacle3X = 0;
    private double obstacle3Y = 429;
    private double obstacle3Width = 1280;
    private double obstacle3Height = 293;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        spriteImage = new Image(getClass().getResourceAsStream("/sprite.png")); // Ensure this path is correct
        spriteWidth = spriteImage.getWidth();
        spriteHeight = spriteImage.getHeight();
        drawGame();

        gameCanvas.setOnKeyPressed(this::handleKeyPressed);
        gameCanvas.setFocusTraversable(true); // Ensure the canvas can receive key events
        gameCanvas.requestFocus(); // Request focus to ensure it receives key events
    }

    private void drawGame() {
        // Clear the canvas to avoid after images
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Set opacity to 0 for obstacles
        gc.setGlobalAlpha(0.0);
        gc.setFill(Color.RED);
        gc.fillRect(obstacle1X, obstacle1Y, obstacle1Width, obstacle1Height);
        gc.fillRect(obstacle2X, obstacle2Y, obstacle2Width, obstacle2Height);
        gc.fillRect(obstacle3X, obstacle3Y, obstacle3Width, obstacle3Height);

        // Reset opacity to default (1.0) for other drawings
        gc.setGlobalAlpha(1.0);

        // Draw sprite
        gc.drawImage(spriteImage, spriteX, spriteY);
    }

    private void handleKeyPressed(KeyEvent event) {
        double newSpriteX = spriteX;
        double newSpriteY = spriteY;

        if (event.getCode() == KeyCode.W) {
            newSpriteY -= spriteSpeed;
        } else if (event.getCode() == KeyCode.S) {
            newSpriteY += spriteSpeed;
        } else if (event.getCode() == KeyCode.A) {
            newSpriteX -= spriteSpeed;
        } else if (event.getCode() == KeyCode.D) {
            newSpriteX += spriteSpeed;
        }

        // Boundary checks and collision detection
        if (isValidMove(newSpriteX, newSpriteY)) {
            spriteX = newSpriteX;
            spriteY = newSpriteY;
            drawGame();
        }
    }

    private boolean isValidMove(double newX, double newY) {
        // Check boundaries of the canvas
        if (newX < 0 || newY < 0 || newX > gameCanvas.getWidth() - spriteWidth || newY > gameCanvas.getHeight() - spriteHeight) {
            return false;
        }

        // Check collision with obstacle1
        if (newX < obstacle1X + obstacle1Width &&
                newX + spriteWidth > obstacle1X &&
                newY < obstacle1Y + obstacle1Height &&
                newY + spriteHeight > obstacle1Y) {
            return false;
        }

        // Check collision with obstacle2
        if (newX < obstacle2X + obstacle2Width &&
                newX + spriteWidth > obstacle2X &&
                newY < obstacle2Y + obstacle2Height &&
                newY + spriteHeight > obstacle2Y) {
            return false;
        }

        return true;
    }
}