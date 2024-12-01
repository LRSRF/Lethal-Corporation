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
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;

public class Game {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private JFXButton backButton;
    @FXML
    private Text Report;

    private GraphicsContext gc;
    private MediaPlayer mediaPlayer;
    private Timeline animationTimeline;
    private boolean isGameOver = false;
    private boolean isExit = false;
    private int totalValue;
    private int collectedItems;
    private Player player;
    private Map<String, Room> Map;
    private Room currentRoom;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();

        // Initialize Rooms
        Map = new HashMap<>();
        loadMap();
        setCurrentRoom("room0");

        // Initialize player
        player = new Player(this, 600, 300, 10.0, currentRoom.getBarriers(), "/Employee1.png", "/Employee2.png", "/Employee3.png", "/Employee4.png", "room0");

        // Setup BFS for each Bracken in each room
        for (Room room : Map.values()) {
            for (Bracken bracken : room.getMobs()) {
                bracken.setupRoomTraversal(player); // Start BFS for each bracken
            }
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


    private void loadMap() {
        try {
            // Read JSON file
            InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/Facility.json"));
            StringBuilder jsonStr = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                jsonStr.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(jsonStr.toString());
            JSONObject roomsJson = jsonObject.getJSONObject("rooms");

            // Iterate over each room entry in the JSON file
            for (String roomKey : roomsJson.keySet()) {
                JSONObject roomData = roomsJson.getJSONObject(roomKey);
                Room rooms = new Room(roomKey, roomData.getString("backgroundImagePath"));

                // Load barriers
                JSONArray barriers = roomData.optJSONArray("barriers");
                if (barriers != null) {
                    for (int j = 0; j < barriers.length(); j++) {
                        JSONObject barrierJson = barriers.getJSONObject(j);
                        Barrier barrier = new Barrier(
                                barrierJson.getInt("x"),
                                barrierJson.getInt("y"),
                                barrierJson.getInt("width"),
                                barrierJson.getInt("height")
                        );
                        rooms.addBarrier(barrier);
                    }
                }

                // Load items
                JSONArray items = roomData.optJSONArray("items");
                if (items != null) {
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject itemJson = items.getJSONObject(j);
                        Item item = new Item(
                                itemJson.getInt("x"),
                                itemJson.getInt("y"),
                                itemJson.getInt("value"),
                                itemJson.getString("imagePath")
                        );
                        rooms.addItem(item);
                    }
                }

                // Load mobs
                JSONArray mobs = roomData.optJSONArray("mobs");
                if (mobs != null) {
                    for (int j = 0; j < mobs.length(); j++) {
                        JSONObject mobJson = mobs.getJSONObject(j);
                        Bracken bracken = new Bracken(
                                mobJson.getInt("x"),
                                mobJson.getInt("y"),
                                mobJson.getDouble("speed"),
                                mobJson.getJSONArray("imagePaths").getString(0),
                                mobJson.getJSONArray("imagePaths").getString(1),
                                mobJson.getJSONArray("imagePaths").getString(2),
                                mobJson.getJSONArray("imagePaths").getString(3),
                                Map,
                                roomKey
                        );
                        rooms.addMob(bracken);
                    }
                }

                // Set room connections (Left, Right, Up, Down)
                JSONObject connections = roomData.optJSONObject("connections");
                if (connections != null) {
                    for (String direction : connections.keySet()) {
                        rooms.addNeighbor(direction, connections.getString(direction));
                    }
                }

                // Add to rooms
                Map.put(roomKey, rooms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setCurrentRoom(String roomKey) {
        currentRoom = Map.get(roomKey);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    private void setupAnimation() {
        animationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event -> {
            player.animate();
            for (Bracken bracken : currentRoom.getMobs()) {
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

        // Draw the current room
        currentRoom.draw(gc);

        // Draw the player
        player.draw(gc);

        // Check for collision between player and mobs
        for (Bracken bracken : currentRoom.getMobs()) {
            if (player.checkCollision(bracken.getX(), bracken.getY(), bracken.getWidth(), bracken.getHeight())) {
                bracken.stopChasingPlayer();
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

        // Handle player key press
        player.handleKeyPress(event);

        // Get player position
        double playerX = player.getX();
        double playerY = player.getY();

        // Check for room transitions
        String nextRoomKey = null;
        double nextPlayerX = playerX; // Default: keep player's current X
        double nextPlayerY = playerY; // Default: keep player's current Y

        // Transition to the left room
        if (playerX <= 0 && (nextRoomKey = currentRoom.getNeighbor("left")) != null) {
            nextPlayerX = gameCanvas.getWidth() - player.getWidth(); // Place player at the right edge of the new room
        }
        // Transition to the right room
        else if (playerX + player.getWidth() >= gameCanvas.getWidth() && (nextRoomKey = currentRoom.getNeighbor("right")) != null) {
            nextPlayerX = 0; // Place player at the left edge of the new room
        }
        // Transition to the up room
        else if (playerY <= 150 && (nextRoomKey = currentRoom.getNeighbor("up")) != null) {
            nextPlayerY = gameCanvas.getHeight() - player.getHeight(); // Place player at the bottom edge of the new room
        }
        // Transition to the down room
        else if (playerY + player.getHeight() >= gameCanvas.getHeight() && (nextRoomKey = currentRoom.getNeighbor("down")) != null) {
            nextPlayerY = 200; // Place player slightly below the top edge of the new room
        }

        // Perform room transition if a valid neighbor room exists
        if (nextRoomKey != null) {
            switchRoom(nextRoomKey, nextPlayerX, nextPlayerY);
        } else {
            // No room transition, just draw the game and start animation
            drawGame();
            startAnimation();
        }
    }

    private void switchRoom(String newRoomKey, double newX, double newY) {
        // Switch to the new room
        setCurrentRoom(newRoomKey);
        player.setCurrentRoom(newRoomKey);

        // Update player position
        player.setX(newX);
        player.setY(newY);
        player.setBarriers(getCurrentRoom().getBarriers());

        // Update mobs to target the player in the new room
        for (Bracken bracken : currentRoom.getMobs()) {
            bracken.setupPlayerChase(player, currentRoom.getBarriers());
        }

        // Redraw the game
        drawGame();

        if (newRoomKey.equals("exit")) {
            isExit = true; // Trigger the room setup
            updateCollectedItems(); // Check the win condition
        }
    }

    public Room getRoomById(String roomId) {
        return Map.get(roomId);
    }

    public Player getPlayer() {
        return player;
    }

    private int calculateTotalItems() {
        int totalValue = 0;
        for (Room rooms : Map.values()) {
            for (Item item : rooms.getItems()) {
                totalValue += (int) item.getValue(); // Sum up the values of all items
            }
        }
        return totalValue;
    }

    private void updateCollectedItems() {
        if (!isExit) {
            return;
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
        for (Bracken bracken : currentRoom.getMobs()) {
            bracken.stopChasingPlayer();
            bracken.stopRoomTraversal();
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
