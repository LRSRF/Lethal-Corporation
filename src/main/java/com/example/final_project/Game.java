package com.example.final_project;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.image.Image;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Game {
    private TreeTableView<?> Inventory;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Button backButton;
    @FXML
    private Text Gameover;
    @FXML
    private AnchorPane gameRoot;

    private GraphicsContext gc;
    private MediaPlayer mediaPlayer;
    private Timeline animationTimeline;
    private boolean isGameOver = false;

    private Player player;
    private Map<String, GameMap> maps;
    private GameMap currentMap;

    private Canvas inventoryCanvas;

    private Stack<Image> inventoryStack = new Stack<>();
    private GraphicsContext inventoryGC;

    private static Game currentGame;
    private int collectedCount = 0;



    public Game() {
        currentGame = this; // Set the current instance when the game is created
    }
    public static Game getCurrentGame() {
        if (currentGame == null) {
            currentGame = new Game();
        }
        return currentGame;
    }
    public int getCollectedCount() {
        System.out.println(collectedCount);
        return collectedCount;
    }

    public void incrementCollectedCount() {
        collectedCount++;
    }
    public void minusCollectedCount() {
        collectedCount--;
    }
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();

        // Initialize maps
        maps = new HashMap<>();
        loadMaps();
        loadInventory();
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
        map0.addItem(new Item(990, 584, 100, "/Axel.png"));
        map0.addItem(new Item(990, 550, 100, "/Axel.png"));
        map0.addItem(new Item(990, 520, 100, "/Axel.png"));
        map0.addItem(new Item(920, 520, 100, "/Axel.png"));
        map0.addItem(new Item(830, 520, 100, "/Axel.png"));

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
        map5.addItem(new Item(590, 580, 100, "/Axel.png"));
        map5.addMob(new Bracken(1091, 287, 20.0, "/Bracken1.png", "/Bracken2.png", "/Bracken3.png", "/Bracken4.png"));

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
    }
    private void loadInventory() {
        // Create a new canvas for the inventory
        inventoryCanvas = new Canvas(150, 150); // Single cell, adjust size as needed
        inventoryCanvas.setVisible(true); // Make visible

        // Set position for the inventory canvas
        inventoryCanvas.setLayoutX(1125); // Adjust position as needed
        inventoryCanvas.setLayoutY(565); // Adjust position as needed

        // Get the graphics context to draw items
        inventoryGC = inventoryCanvas.getGraphicsContext2D();

        // Skip filling the background with a solid color for transparency

        // Draw a single cell border


        inventoryGC.strokeRect(0, 0, inventoryCanvas.getWidth(), inventoryCanvas.getHeight());

        // Add the canvas to the game root
        gameRoot.getChildren().add(inventoryCanvas);
    }

    public void addInventory(String imagePath) {
        // Load the image
        Image itemImage = new Image(imagePath);

        // Add the image to the stack (could be an ArrayList or Stack)
        inventoryStack.push(itemImage);

        // Redraw the inventory
        redrawInventory();
    }


    public void redrawInventory() {
        // Clear the inventory canvas
        inventoryGC.clearRect(0, 0, inventoryCanvas.getWidth(), inventoryCanvas.getHeight());

        // Draw the border around the inventory
        inventoryGC.setStroke(Color.BLACK);
        inventoryGC.strokeRect(0, 0, inventoryCanvas.getWidth(), inventoryCanvas.getHeight());

        // Stack the items visually
        int index = 0;
        for (Image item : inventoryStack) {
            double offset = index * -20; // Adjust overlap shift (e.g., 10 pixels to the left for each item)
            double x = offset + 55; // Shift each subsequent item
            double y = offset * 0 + 15; // Optional: Add a vertical offset for visual effect

            inventoryGC.drawImage(item, x, y, 100, 100); // Adjust size as needed
            index++;
        }
    }

    public void dropInventory(double playerX, double playerY) {
        if (!inventoryStack.isEmpty()) {
            // Remove the top item from the stack
            Image droppedItem = inventoryStack.pop();

            minusCollectedCount();

            // Redraw the inventory to reflect the removed item
            redrawInventory();

            // Create a new image view for the dropped item
            ImageView droppedItemView = new ImageView(droppedItem);

            // Set the position where the player is currently standing
            droppedItemView.setX(playerX);
            droppedItemView.setY(playerY);

            // Adjust the size of the dropped item if needed
            droppedItemView.setFitWidth(100); // Adjust width as needed
            droppedItemView.setFitHeight(100); // Adjust height as needed

            // Add the dropped item to the game root
            gameRoot.getChildren().add(droppedItemView);

            // Create a FadeTransition for the dropped item
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4), droppedItemView);
            fadeTransition.setFromValue(1.0);  // Fully visible
            fadeTransition.setToValue(0.0);    // Completely transparent

            // Start the fade-out effect
            fadeTransition.play();

            // Optionally, remove the item after the fade effect is complete
            fadeTransition.setOnFinished(event -> {
                gameRoot.getChildren().remove(droppedItemView);  // Remove the item from the game
            });
        }
    }



    public void setCurrentMap(String mapKey) {
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
        if (event.getCode() == KeyCode.G) {
            dropInventory(playerX, playerY);
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
        System.out.println("Game Over!");
        isGameOver = true;
        stopmusic();

        Gameover.setOpacity(1.0);
        gameCanvas.setEffect(new GaussianBlur(50));

        gameCanvas.setOnKeyPressed(null);
        gameCanvas.setOnKeyReleased(null);
    }


}