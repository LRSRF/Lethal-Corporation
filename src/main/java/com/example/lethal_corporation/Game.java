package com.example.lethal_corporation;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Game {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private JFXButton backButton;
    @FXML
    private Text Gameover;
    @FXML
    private Text Report;

    private GraphicsContext gc;
    private MediaPlayer mediaPlayer;
    private Timeline animationTimeline;
    private boolean isGameOver = false;
    private boolean isMapSetupTriggered = false;
    private int totalValue;
    private int collectedItems;
    private Player player;
    private Map<String, GameMap> maps;
    private GameMap currentMap;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();

        // Initialize maps
        maps = new HashMap<>();
        loadMaps();
        setCurrentMap("map0");

        // Initialize player
        player = new Player(this,600, 300, 10.0, currentMap.getBarriers(), "/Employee1.png", "/Employee2.png", "/Employee3.png", "/Employee4.png");

        // Setup player and mobs
        for (Bracken bracken : currentMap.getMobs()) {
            bracken.setupMovement(player, currentMap.getBarriers());
        }

        playmusic();

        drawGame();

        gameCanvas.setOnKeyPressed(this::handleKeyPressed);
        gameCanvas.setOnKeyReleased(this::handleKeyReleased);
        gameCanvas.setFocusTraversable(true);
        gameCanvas.requestFocus();

        setupAnimation();

        totalValue = calculateTotalItems();
        collectedItems = 0;

        backButton.setDisable(true);
    }

    private void loadMaps() {
        // Create and add maps
        GameMap map0 = new GameMap("/Map0.png");
        map0.addBarrier(new Barrier(0, 0, 1280, 140));
        map0.addBarrier(new Barrier(0, 14, 13, 214));
        map0.addBarrier(new Barrier(1267, 14, 13, 214));
        map0.addBarrier(new Barrier(0, 477, 13, 214));
        map0.addBarrier(new Barrier(1266, 477, 13, 214));
        map0.addBarrier(new Barrier(0, 707, 540, 13));
        map0.addBarrier(new Barrier(720, 707, 540, 13));
        map0.addItem(new Item(990, 584, 70, "/Bottles.png"));

        GameMap map1 = new GameMap("/Map1.png");
        map1.addBarrier(new Barrier(735, 0, 540, 215));
        map1.addBarrier(new Barrier(730, 488, 540, 230));
        map1.addBarrier(new Barrier(555, 10, 175, 120));
        map1.addBarrier(new Barrier(0, 0, 550, 215));
        map1.addBarrier(new Barrier(0, 0, 370, 710));
        map1.addBarrier(new Barrier(0, 488, 550, 230));

        GameMap map2 = new GameMap("/Map2.png");
        map2.addBarrier(new Barrier(0, 0, 725, 215));
        map2.addBarrier(new Barrier(0, 480, 535, 235));
        map2.addBarrier(new Barrier(725, 0, 550, 715));

        GameMap map3 = new GameMap("/Map3.png");
        map3.addBarrier(new Barrier(0, 0, 550, 720));
        map3.addBarrier(new Barrier(734, 0, 542, 217));
        map3.addBarrier(new Barrier(734, 490, 542, 217));
        map3.addBarrier(new Barrier(547, 0, 175, 120));
        map3.addBarrier(new Barrier(0, 707, 1280, 13));
        map3.addItem(new Item(764, 360, 100, "/Axel.png"));

        GameMap map4 = new GameMap("/Map4.png");
        map4.addBarrier(new Barrier(0, 0, 542, 217));
        map4.addBarrier(new Barrier(726, 0, 542, 217));
        map4.addBarrier(new Barrier(0, 490, 1274, 227));
        map4.addBarrier(new Barrier(547, 0, 175, 120));

        GameMap map5 = new GameMap("/Map5.png");
        map5.addBarrier(new Barrier(0, 0, 542, 217));
        map5.addBarrier(new Barrier(726, 0, 542, 217));
        map5.addBarrier(new Barrier(547, 0, 175, 120));
        map5.addBarrier(new Barrier(726, 490, 550, 227));
        map5.addBarrier(new Barrier(0, 707, 1280, 13));
        map5.addItem(new Item(590, 580, 200, "/Engine.png"));
        map5.addMob(new Bracken(1091, 287, 20.0, "/Bracken1.png", "/Bracken2.png", "/Bracken3.png", "/Bracken4.png"));

        GameMap map6 = new GameMap("/PerformanceReport.png");

        map0.setUpMapKey("map6");
        map0.setLeftMapKey("map1");
        map0.setRightMapKey("map2");
        map0.setDownMapKey("map4");

        map1.setRightMapKey("map0");
        map1.setDownMapKey("map3");

        map2.setLeftMapKey("map0");
        map2.setDownMapKey("map5");

        map3.setRightMapKey("map4");
        map3.setUpMapKey("map1");

        map4.setUpMapKey("map0");
        map4.setLeftMapKey("map3");
        map4.setRightMapKey("map5");

        map5.setUpMapKey("map2");
        map5.setLeftMapKey("map4");

        maps.put("map0", map0);
        maps.put("map1", map1);
        maps.put("map2", map2);
        maps.put("map3", map3);
        maps.put("map4", map4);
        maps.put("map5", map5);
        maps.put("map6", map6);
    }

    private void setCurrentMap(String mapKey) {
        currentMap = maps.get(mapKey);
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    private void setupAnimation() {
        animationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event -> {
            player.animate();
            for (Bracken bracken : currentMap.getMobs()) {
                bracken.animate();
            }
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
            AppState.getInstance().setMediaPlayer(mediaPlayer);
            mediaPlayer.setVolume(AppState.getInstance().getVolume());
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

        // Draw the current map
        currentMap.draw(gc);

        // Draw the player
        player.draw(gc);

        // Check for collision between player and mobs
        for (Bracken bracken : currentMap.getMobs()) {
            if (player.checkCollision(bracken.getX(), bracken.getY(), bracken.getWidth(), bracken.getHeight())) {
                gameOver();
                return;
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
        gc.setGlobalAlpha(0.3);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.restore();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (isGameOver) return;

        player.handleKeyPress(event);

        // Check for map transitions
        double playerX = player.getX();
        double playerY = player.getY();

        if (playerX <= 0 && currentMap.getLeftMapKey() != null) {
            switchMap(currentMap.getLeftMapKey(), gameCanvas.getWidth() - player.getWidth(), playerY);
        } else if (playerX + player.getWidth() >= gameCanvas.getWidth() && currentMap.getRightMapKey() != null) {
            switchMap(currentMap.getRightMapKey(), 0, playerY);
        } else if (playerY <= 150 && currentMap.getUpMapKey() != null) {
            switchMap(currentMap.getUpMapKey(), playerX, gameCanvas.getHeight() - player.getHeight());
        } else if (playerY + player.getHeight() >= gameCanvas.getHeight() && currentMap.getDownMapKey() != null) {
            switchMap(currentMap.getDownMapKey(), playerX, 0+200);
        } else {
            drawGame();
            startAnimation();
        }
    }

    private void switchMap(String newMapKey, double newX, double newY) {
        // Switch to the new map
        setCurrentMap(newMapKey);

        // Update player position
        player.setX(newX);
        player.setY(newY);
        player.setBarriers(getCurrentMap().getBarriers());

        // Update mobs to target the player in the new map
        for (Bracken bracken : currentMap.getMobs()) {
            bracken.setupMovement(player, currentMap.getBarriers());
        }

        // Redraw the game
        drawGame();

        if (newMapKey.equals("map6")) {
            isMapSetupTriggered = true; // Trigger the map setup
            updateCollectedItems(); // Check the win condition
        }
    }

    private int calculateTotalItems() {
        int totalValue = 0;
        for (GameMap map : maps.values()) {
            for (Item item : map.getItems()) {
                totalValue += (int) item.getValue(); // Sum up the values of all items
            }
        }
        return totalValue;
    }

    private void updateCollectedItems() {
        if (!isMapSetupTriggered) {
            return; // Do not update if map0.setUpMapKey() has not been triggered
        }

        collectedItems = (int) player.getTotalCollectedValue();
        displayWinMessage(collectedItems, totalValue);
    }

    private String calculateGrade() {
        double percentage = (double) collectedItems / totalValue * 100;

        if (percentage >= 81) {
            return "A";
        } else if (percentage >= 71) {
            return "B";
        } else if (percentage >= 61) {
            return "C";
        } else if (percentage >= 0) {
            return "D";
        } else {
            return "F"; // Default to "F" if none of the conditions are met
        }
    }

    private void displayWinMessage(int collectedItems, int totalValue) {
        // Handle game over
        animationTimeline.stop();

        // Show win message and stop the game
        System.out.println("Performance Report.");
        System.out.println("Collected: "+collectedItems+"/"+totalValue+"    Grade: "+calculateGrade());
        isGameOver = true;
        stopmusic();

        Report.setText("Collected: "+collectedItems+"/"+totalValue+"    Grade: "+calculateGrade());
        Report.setOpacity(1.0);

        gameCanvas.setOnKeyPressed(null);
        gameCanvas.setOnKeyReleased(null);

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        Image resultImage = new Image(getClass().getResourceAsStream("/PerformanceReport.png"));
        gc.drawImage(resultImage, 0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        backButton.setDisable(false);

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
        for (Bracken bracken : currentMap.getMobs()) {
            bracken.stopMovement();
        }

        Image resultImage = new Image(getClass().getResourceAsStream("/GameOver.png"));
        gc.drawImage(resultImage, 0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        System.out.println("Game Over!");
        isGameOver = true;
        stopmusic();

        gameCanvas.setOnKeyPressed(null);
        gameCanvas.setOnKeyReleased(null);

        backButton.setDisable(false);
    }

    @FXML
    private void ExitButton(ActionEvent event) throws IOException {
        // Load the main menu scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1280, 720);
        scene.getRoot().setOpacity(AppState.getInstance().getBrightness());
        stage.setScene(scene);
        stage.show();
        stopmusic();
    }
}