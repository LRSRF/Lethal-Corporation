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

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Game {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Button backButton;
    @FXML
    private Text Gameover;

    private GraphicsContext gc;
    private MediaPlayer mediaPlayer;
    private Timeline animationTimeline;
    private boolean isGameOver = false;

    private Player player;
    private Bracken bracken;
    private List<Barrier> barriers;
    private static List<Item> items = new ArrayList<>();;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();

        // Initialize barriers
        barriers = new ArrayList<>();
        barriers.add(new Barrier(0, 0, 280, 720));
        barriers.add(new Barrier(1000, 0, 280, 720));
        barriers.add(new Barrier(280, 0, 720, 140));
        barriers.add(new Barrier(0, 715, 1280, 720));

        // Initialize items
        items = new ArrayList<>();
        Item axel = new Item(880, 600, 100, "/Axel.png");
        addItem(axel);

        // Initialize player
        player = new Player(600, 300, 10.0, barriers, "/Employee1.png", "/Employee2.png", "/Employee3.png", "/Employee4.png");
        bracken = new Bracken(600, 500, 10.0, "/Bracken1.png", "/Bracken2.png", "/Bracken3.png", "/Bracken4.png");

        bracken.setupMovement(player);

        playmusic();

        drawGame();

        gameCanvas.setOnKeyPressed(this::handleKeyPressed);
        gameCanvas.setOnKeyReleased(this::handleKeyReleased);
        gameCanvas.setFocusTraversable(true);
        gameCanvas.requestFocus();

        setupAnimation();
    }

    public static List<Item> getItems() {
        return items;
    }

    public static void addItem(Item item) {
        items.add(item);
    }

    public static void removeItem(Item item) {
        items.remove(item);
    }

    private void setupAnimation() {
        animationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event -> {
            player.animate();
            drawGame();
        }));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
        animationTimeline.play();
    }

    //Play background music
    private void playmusic(){
        String musicFile = "/Library/Music/GameMusic.mp3";
        URL resource = getClass().getResource(musicFile);
        if (resource != null) {
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
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
    
    private void drawGame() {
        // Clear the canvas to avoid afterimages
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Draw the barriers
        if (barriers != null) {
            for (Barrier barrier : barriers) {
                barrier.draw(gc);
            }
        }

        // Draw items
        if (items != null) {
            for (Item item : items) {
                item.draw(gc);
            }
        }

        // Draw the game elements (player and bracken)
        player.draw(gc);

        // Draw the bracken only if it exists
        if (bracken != null) {
            bracken.draw(gc);

            // Check for collision between player and bracken
            if (player.checkCollision(bracken.getX(), bracken.getY(), bracken.getWidth(), bracken.getHeight())) {
                gameOver();
            }
        }

        // Dark overlay and flashlight effect for the player
        drawLightingEffects();
    }

    private void drawLightingEffects() {
        // Draw dark overlay and flashlight effect around the player
        gc.setFill(Color.BLACK);
        gc.setGlobalAlpha(0.8);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        double flashlightRadius = 150;
        gc.save();
        gc.beginPath();
        gc.arc(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, flashlightRadius, flashlightRadius, 0, 360);
        gc.closePath();
        gc.clip();

        gc.setFill(Color.WHITE);
        gc.setGlobalAlpha(0.2);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.restore();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (isGameOver) return;

        player.handleKeyPress(event);
        drawGame();
        startAnimation();
    }

    private void handleKeyReleased(KeyEvent event) {
        player.handleKeyRelease(event);
        stopAnimation();
    }

    private void startAnimation() {
        if (!player.isMoving()) {
            animationTimeline.play();
            player.setMoving(true);
        }
    }

    private void stopAnimation() {
        if (player.isMoving()) {
            animationTimeline.stop();
            player.resetImage();
            drawGame();
            player.setMoving(false);
        }
    }

    private void gameOver() {
        // Handle game over
        animationTimeline.stop();
        bracken.stopMovement();
        System.out.println("Game Over!");
        isGameOver = true;
        stopmusic();

        Gameover.setOpacity(1.0);
        gameCanvas.setEffect(new GaussianBlur(10));

        gameCanvas.setOnKeyPressed(null);
        gameCanvas.setOnKeyReleased(null);
    }
}