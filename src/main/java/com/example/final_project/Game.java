package com.example.final_project;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;

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
    private boolean isGameOver = false;

    @FXML
    private Text Gameover;
    private Image spiderImage1;
    private Image spiderImage2;
    private Image monsterImage1;
    private Image monsterImage2;
    private Image batImage1;
    private Image batImage2;
    private Image currentSpiderImage;
    private double spiderX = 800; // Initial position of the spider
    private double spiderY = 300;
    private double monsterX = 550;
    private double monsterY = 75;
    private double spiderSpeed = 50; // 100 pixels per second
    private Timeline spiderMovementTimeline;
    private Timeline monsterMovementTimeline;

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

    private MediaPlayer mediaPlayer;

    private Image currentMonsterImage;
    private boolean isMonster = false; // Track whether the current NPC is a monster or spider

    private Image currentNPCImage;
    private double monsterSpeed = 50;


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

        spiderImage1 = new Image(getClass().getResourceAsStream("/spider1.png"));
        spiderImage2 = new Image(getClass().getResourceAsStream("/spider2.png"));

        monsterImage1 = new Image(getClass().getResourceAsStream("/monster1.png"));
        monsterImage2 = new Image(getClass().getResourceAsStream("/monster2.png"));

        currentSpriteImage = spriteImage1; // Set initial sprite image
        currentSpiderImage = spiderImage1;
        currentMonsterImage = monsterImage1;
        currentNPCImage = currentSpiderImage;
        drawGame();

        gameCanvas.setOnKeyPressed(this::handleKeyPressed);
        gameCanvas.setOnKeyReleased(this::handleKeyReleased);
        gameCanvas.setFocusTraversable(true); // Ensure the canvas can receive key events
        gameCanvas.requestFocus(); // Request focus to ensure it receives key events


        setupAnimation();
        setupSpiderMovement();
        playmusic();

    }

    private void setupSpiderMovement() {
        spiderMovementTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            double newSpiderX = spiderX;
            double newSpiderY = spiderY;

            // Calculate direction towards the player's position
            double deltaX = spriteX - spiderX;
            double deltaY = spriteY - spiderY;

            // Normalize the direction vector
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (distance != 0) {
                deltaX /= distance;
                deltaY /= distance;
            }

            // Move the spider towards the player
            newSpiderX += deltaX * spiderSpeed;
            newSpiderY += deltaY * spiderSpeed;

            // Check if the new position is valid before updating the spider's position
            if (isValidMove(newSpiderX, newSpiderY, 100, 100)) { // Pass the spider's dimensions
                spiderX = newSpiderX;
                spiderY = newSpiderY;
            }

            // Update spider animation
            if (currentNPCImage == spiderImage1) {
                currentNPCImage = spiderImage2;
            } else {
                currentNPCImage = spiderImage1;
            }

            // Draw updated game state
            drawGame();

            // Check for collision with the player
            if (checkCollision(spriteX, spriteY, spriteWidth, spriteHeight, spiderX, spiderY, 300, 200)) {
                gameOver();
            }
        }));
        spiderMovementTimeline.setCycleCount(Animation.INDEFINITE);
        spiderMovementTimeline.play();
    }

    private void setupMonsterMovement() {
        Timeline monsterMovementTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            double newMonsterX = monsterX; // Assuming you have a separate variable for monster position
            double newMonsterY = monsterY; // Assuming you have a separate variable for monster position

            // Calculate direction towards the player's position
            double deltaX = spriteX - newMonsterX;
            double deltaY = spriteY - newMonsterY;

            // Normalize the direction vector
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (distance != 0) {
                deltaX /= distance;
                deltaY /= distance;
            }

            // Move the monster towards the player
            newMonsterX += deltaX * monsterSpeed; // Use a separate speed variable if needed
            newMonsterY += deltaY * monsterSpeed; // Use a separate speed variable if needed

            // Check if the new position is valid before updating the monster's position
            if (isValidMove(newMonsterX, newMonsterY, 150, 150)) { // Adjust dimensions as needed
                monsterX = newMonsterX;
                monsterY = newMonsterY;
            }

            // Update monster animation
            if (currentMonsterImage == monsterImage1) {
                currentMonsterImage = monsterImage2;
            } else {
                currentMonsterImage = monsterImage1;
            }

            // Draw updated game state
            drawGame();

            // Check for collision with the player
            if (checkCollision(spriteX, spriteY, spriteWidth, spriteHeight, monsterX, monsterY, 300, 200)) {
                gameOver();
            }
        }));
        monsterMovementTimeline.setCycleCount(Animation.INDEFINITE);
        monsterMovementTimeline.play();
    }

    private boolean checkCollision(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    private void gameOver() {
        // Handle game over (e.g., stop timelines, show game over screen)
        animationTimeline.stop();
        spiderMovementTimeline.stop();
        System.out.println("Game Over!");
        isGameOver = true; // Set game over flag
        animationTimeline.stop();
        spiderMovementTimeline.stop();

        Gameover.setOpacity(1.0); // Show the Game Over label

        // Apply blur effect to the canvas
        gameCanvas.setEffect(new GaussianBlur(10));

        // Disable further input by clearing key event handlers
        gameCanvas.setOnKeyPressed(null);
        gameCanvas.setOnKeyReleased(null);
    }


    private void setupAnimation() {
        animationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event -> {
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
        // Clear the canvas to avoid afterimages
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Draw all game elements (map, obstacles, etc.)
        drawGameElements();

        // Draw the dark overlay on the entire canvas
        gc.setFill(Color.BLACK);
        gc.setGlobalAlpha(0.8); // Adjust this value to control darkness
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Create a circular clipping region (flashlight effect)
        double flashlightRadius = 150; // Radius of the flashlight circle

        gc.save(); // Save the current state of the canvas
        gc.beginPath();
        gc.arc(spriteX + spriteWidth / 2, spriteY + spriteHeight / 2, flashlightRadius, flashlightRadius, 0, 360);
        gc.closePath();
        gc.clip();

        // Color the arc white to simulate illumination
        gc.setFill(Color.WHITE);
        gc.setGlobalAlpha(0.2); // Adjust opacity for a more realistic light effect
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.restore(); // Restore the canvas state to remove the clipping region

        // Draw the sprite and the current NPC within the flashlight area
        gc.drawImage(currentSpriteImage, spriteX, spriteY);
        gc.drawImage(currentNPCImage, spiderX, spiderY, 300, 200);
    }


    private void handleKeyPressed(KeyEvent event) {
        if (isGameOver) return; // Ignore input if the game is over

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

        if (isValidMove(newSpriteX, newSpriteY, spriteWidth, spriteHeight)) {
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

    private boolean isValidMove(double newX, double newY, double objectWidth, double objectHeight) {
        // Check boundaries of the canvas
        if (newX < 0 || newY < 0 || newX > gameCanvas.getWidth() - objectWidth || newY > gameCanvas.getHeight() - objectHeight) {
            return false;
        }
        if (newX < obstacle5X + obstacle5Width &&
                newX + objectWidth > obstacle5X &&
                newY < obstacle5Y + obstacle5Height &&
                newY + objectHeight > obstacle5Y) {
            switchmap();
            return false; // Prevent further movement
        }

        if (newX < obstacle1X + obstacle1Width &&
                newX + objectWidth > obstacle1X &&
                newY < obstacle1Y + obstacle1Height &&
                newY + objectHeight > obstacle1Y) {
            return false;
        }

        if (newX < obstacle2X + obstacle2Width &&
                newX + objectWidth > obstacle2X &&
                newY < obstacle2Y + obstacle2Height &&
                newY + objectHeight > obstacle2Y) {
            return false;
        }
        if (newX < obstacle3X + obstacle3Width &&
                newX + objectWidth > obstacle3X &&
                newY < obstacle3Y + obstacle3Height &&
                newY + objectHeight > obstacle3Y) {
            return false;
        }
        if (newX < obstacle4X + obstacle4Width &&
                newX + objectWidth > obstacle4X &&
                newY < obstacle4Y + obstacle4Height &&
                newY + objectHeight > obstacle4Y) {
            return false;
        } else {
            return true;
        }
    }

    private void drawGameElements() {
        // Draw all static game elements like obstacles and background here
        gc.setGlobalAlpha(0.0); // Make obstacles invisible
        gc.setFill(Color.RED);
        gc.fillRect(obstacle1X, obstacle1Y, obstacle1Width, obstacle1Height);
        gc.fillRect(obstacle2X, obstacle2Y, obstacle2Width, obstacle2Height);
        gc.fillRect(obstacle3X, obstacle3Y, obstacle3Width, obstacle3Height);
        gc.fillRect(obstacle4X, obstacle4Y, obstacle4Width, obstacle4Height);
        gc.fillRect(obstacle5X, obstacle5Y, obstacle5Width, obstacle5Height);
        gc.setGlobalAlpha(1.0); // Reset opacity to default for other drawings
    }

    private void clearCanvas() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }

    private void playmusic() {
        String musicFile = "/Library/Music/GameMusic.mp3"; // Update this path as needed
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

    private void switchmap() {
        spiderMovementTimeline.stop();
        clearCanvas();

        // Alternate between spider and monster



        // Optionally change the starting position
        spriteX = 600;
        spriteY = 500;

        // Redraw the game elements on the new map
        drawGameElements();
        drawGame();

        // Start the NPC movement with the new NPC

    }
}
