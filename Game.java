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
    private Image spriteImage3;
    private Image spriteImage4;
    private Image currentSpriteImage;
    private double spriteX = 13;
    private double spriteY = 275;
    private double spriteSpeed = 10.0;
    private double spriteWidth;
    private double spriteHeight;
    private Timeline animationTimeline;
    private boolean isMoving = false;
    public boolean direction = true; // true = facing right, false = facing left

    // Define obstacles
    private double obstacle1X = 0;
    private double obstacle1Y = 0;
    private double obstacle1Width = 548;
    private double obstacle1Height = 210;

    private double obstacle2X = 716;
    private double obstacle2Y = 0;
    private double obstacle2Width = 564;
    private double obstacle2Height = 210;

    private double obstacle3X = 0;
    private double obstacle3Y = 483;
    private double obstacle3Width = 548;
    private double obstacle3Height = 240;

    private double obstacle4X = 716;
    private double obstacle4Y = 483;
    private double obstacle4Width = 564;
    private double obstacle4Height = 210;

    private double obstacle5X = 540;
    private double obstacle5Y = 0;
    private double obstacle5Width = 200;
    private double obstacle5Height = 131;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        spriteImage1 = new Image(getClass().getResourceAsStream("/Employee1.png")); // facing right
        spriteImage2 = new Image(getClass().getResourceAsStream("/Employee2.png")); // facing right
        spriteImage3 = new Image(getClass().getResourceAsStream("/Employee3.png")); // facing left
        spriteImage4 = new Image(getClass().getResourceAsStream("/Employee4.png")); // facing left
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
            if (direction) { // Facing right
                if (currentSpriteImage == spriteImage1) {
                    currentSpriteImage = spriteImage2;
                } else {
                    currentSpriteImage = spriteImage1;
                }
            } else { // Facing left
                if (currentSpriteImage == spriteImage3) {
                    currentSpriteImage = spriteImage4;
                } else {
                    currentSpriteImage = spriteImage3;
                }
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
        gc.fillRect(obstacle4X, obstacle4Y, obstacle4Width, obstacle4Height);
        gc.fillRect(obstacle5X, obstacle5Y, obstacle5Width, obstacle5Height);
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
        } else if (event.getCode() == KeyCode.S) {
            newSpriteY += spriteSpeed;
        } else if (event.getCode() == KeyCode.A) {
            newSpriteX -= spriteSpeed;
            direction = false; // Facing left
        } else if (event.getCode() == KeyCode.D) {
            newSpriteX += spriteSpeed;
            direction = true; // Facing right
        }

        // Boundary checks and collision detection
        if (isValidMove(newSpriteX, newSpriteY)) {
            spriteX = newSpriteX;
            spriteY = newSpriteY;
            drawGame();
            startAnimation();
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
            // Reset to initial image based on direction
            currentSpriteImage = direction ? spriteImage1 : spriteImage3;
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
        if (newX < obstacle4X + obstacle4Width &&
                newX + spriteWidth > obstacle4X &&
                newY < obstacle4Y + obstacle4Height &&
                newY + spriteHeight > obstacle4Y) {
            return false;
        }
        if (newX < obstacle5X + obstacle5Width &&
                newX + spriteWidth > obstacle5X &&
                newY < obstacle5Y + obstacle5Height &&
                newY + spriteHeight > obstacle5Y) {
            return false;
        } else {
            return true;
        }
    }
}
