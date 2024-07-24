package com.example.final_project;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.util.Duration;

public class Game {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Button backButton;

    private GraphicsContext gc;
    private Image spriteImage1;
    private Image spriteImage2;
    private Image currentSpriteImage;
    private double spriteX = 100;
    private double spriteY = 350;
    private double spriteSpeed = 5.0;
    private double spriteWidth;
    private double spriteHeight;
    private Timeline animationTimeline;
    private boolean isMoving = false;

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
        spriteImage1 = new Image(getClass().getResourceAsStream("/sprite.png")); // Ensure this path is correct
        spriteImage2 = new Image(getClass().getResourceAsStream("/sprite2.png")); //; // Ensure this path is correct
        currentSpriteImage = spriteImage1; // Set initial sprite image
        spriteWidth = currentSpriteImage.getWidth();
        spriteHeight = currentSpriteImage.getHeight();
        drawGame();

        gameCanvas.setOnKeyPressed(this::handleKeyPressed);
        gameCanvas.setOnKeyReleased(this::handleKeyReleased);
        gameCanvas.setFocusTraversable(true); // Ensure the canvas can receive key events
        gameCanvas.requestFocus(); // Request focus to ensure it receives key events

        setupAnimation();
    }

    private void setupAnimation() {
        animationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            if (currentSpriteImage == spriteImage1) {
                currentSpriteImage = spriteImage2;
            } else {
                currentSpriteImage = spriteImage1;
            }
            drawGame();
        }));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
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
        gc.fillOval(652, 438, 62, 62);
        gc.fillOval(1025, 438, 62, 62);
        // Reset opacity to default (1.0) for other drawings
        gc.setGlobalAlpha(1.0);

        // Draw sprite
        gc.drawImage(currentSpriteImage, spriteX, spriteY);
    }

    private void handleKeyPressed(KeyEvent event) {
        double newSpriteX = spriteX;
        double newSpriteY = spriteY;

        if (event.getCode() == KeyCode.W) {
            newSpriteY -= spriteSpeed;
            startAnimation();
        } else if (event.getCode() == KeyCode.S) {
            newSpriteY += spriteSpeed;
            startAnimation();
        } else if (event.getCode() == KeyCode.A) {
            newSpriteX -= spriteSpeed;
            startAnimation();
        } else if (event.getCode() == KeyCode.D) {
            newSpriteX += spriteSpeed;
            startAnimation();
        }

        // Boundary checks and collision detection
        if (isValidMove(newSpriteX, newSpriteY)) {
            spriteX = newSpriteX;
            spriteY = newSpriteY;
            drawGame();
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        stopAnimation();
    }

    private void startAnimation() {
        if (!isMoving) {
            animationTimeline.play();
            isMoving = true;
        }
    }

    private void stopAnimation() {
        if (isMoving) {
            animationTimeline.stop();
            currentSpriteImage = spriteImage1; // Reset to initial image
            drawGame();
            isMoving = false;
        }
    }

    private boolean isValidMove(double newX, double newY) {
        // Check boundaries of the canvas
        if (newX < 0 || newY < 0 || newX > gameCanvas.getWidth() - spriteWidth || newY > gameCanvas.getHeight() - spriteHeight) {
            return false;
        }


        if (newX < obstacle1X + obstacle1Width &&
                newX + spriteWidth > obstacle1X &&
                newY < obstacle1Y + obstacle1Height &&
                newY + spriteHeight > obstacle1Y) {
            return false;
        }

        if (newX < obstacle2X + obstacle2Width &&
                newX + spriteWidth > obstacle2X &&
                newY < obstacle2Y + obstacle2Height &&
                newY + spriteHeight > obstacle2Y) {
            return false;
        }
        if (newX < obstacle3X + obstacle3Width &&
                newX + spriteWidth > obstacle3X &&
                newY < obstacle3Y + obstacle3Height &&
                newY + spriteHeight > obstacle3Y) {
            return false;
        }
        if (newX < 652 + 62 &&
                newX + spriteWidth > 652 &&
                newY < 438 + 62 &&
                newY + spriteHeight > 438) {
            return false;
        }
        if (newX < 1025 + 62 &&
                newX + spriteWidth > 1025 &&
                newY < 438 + 62 &&
                newY + spriteHeight > 438) {
            return false;
        }


        return true;
    }
}