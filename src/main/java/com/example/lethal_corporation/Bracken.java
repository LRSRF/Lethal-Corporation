package com.example.lethal_corporation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.*;

public class Bracken extends Entity {
    private Image brackenImageRight1, brackenImageRight2, brackenImageLeft1, brackenImageLeft2;
    private Timeline movementTimeline;
    private Timeline roomTraversalTimeline;
    private boolean movingRight = true;
    private boolean imageToggle = true;
    private List<Barrier> barriers;
    private Map<String, Room> map;
    private Queue<String> bfsQueue;
    private Set<String> visitedRooms;
    private String currentRoomId;
    private boolean isHunting;

    public Bracken(double x, double y, double speed, String imagePathRight1, String imagePathRight2, String imagePathLeft1, String imagePathLeft2, Map<String, Room> map, String startingRoomId) {
        super(x, y, speed, imagePathRight1, imagePathRight2, imagePathLeft1, imagePathLeft2);
        brackenImageRight1 = new Image(getClass().getResourceAsStream(imagePathRight1));
        brackenImageRight2 = new Image(getClass().getResourceAsStream(imagePathRight2));
        brackenImageLeft1 = new Image(getClass().getResourceAsStream(imagePathLeft1));
        brackenImageLeft2 = new Image(getClass().getResourceAsStream(imagePathLeft2));
        currentImage = brackenImageRight1;
        this.map = map;
        this.currentRoomId = startingRoomId;

        bfsQueue = new LinkedList<>();
        visitedRooms = new HashSet<>();
        this.barriers = new ArrayList<>();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(currentImage, x, y, 175, 175);
    }

    @Override
    public void animate() {
        currentImage = movingRight
                ? (imageToggle ? brackenImageRight1 : brackenImageRight2)
                : (imageToggle ? brackenImageLeft1 : brackenImageLeft2);
        imageToggle = !imageToggle;
    }

    @Override
    public void move(double dx, double dy) {
        double nextX = x + dx;
        double nextY = y + dy;

        boolean collisionX = barriers.stream().anyMatch(barrier -> checkCollision(nextX, y, barrier));
        boolean collisionY = barriers.stream().anyMatch(barrier -> checkCollision(x, nextY, barrier));

        if (!collisionX) x = nextX;
        if (!collisionY) y = nextY;

        movingRight = dx > 0;
    }

    private boolean checkCollision(double nextX, double nextY, Barrier barrier) {
        return nextX < barrier.getX() + barrier.getWidth() &&
                nextX + 175 > barrier.getX() &&
                nextY < barrier.getY() + barrier.getHeight() &&
                nextY + 175 > barrier.getY();
    }

    public void setupPlayerChase(Player player, List<Barrier> barriers) {
        this.barriers = barriers;
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            if (currentRoomId.equals(player.getCurrentRoom())) {
                moveTowardsPlayer(player);
            } else {
                isHunting = true;
            }
        }));
        movementTimeline.setCycleCount(Animation.INDEFINITE);
        movementTimeline.play();
    }

    private void moveTowardsPlayer(Player player) {
        double dx = player.getX() > x ? speed : -speed;
        double dy = player.getY() > y ? speed : -speed;
        move(dx, dy);
        animate();
        System.out.println("Bracken is moving towards player at (" + player.getX() + ", " + player.getY() + ")");
    }

    public void stopChasingPlayer() {
        if (movementTimeline != null) {
            movementTimeline.stop();
        }
        System.out.println("Bracken has stopped chasing the player.");
    }

    public void setupRoomTraversal(Player player) {
        // Check if Bracken is hunting or not
        if (!isHunting) {
            // If not hunting, use 5-second delay for room traversal
            roomTraversalTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                if (!currentRoomId.equals(player.getCurrentRoom())) {
                    exploreNextRoom(player);
                }
            }));
            roomTraversalTimeline.setCycleCount(Animation.INDEFINITE);
            roomTraversalTimeline.play();
        } else {
            // If hunting, use 2-second delay for room traversal
            roomTraversalTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
                if (!currentRoomId.equals(player.getCurrentRoom())) {
                    exploreNextRoom(player);
                }
            }));
            roomTraversalTimeline.setCycleCount(Animation.INDEFINITE);
            roomTraversalTimeline.play();
        }
    }


    private void exploreNextRoom(Player player) {
        // Check if Bracken is hunting the player
        if (!isHunting) {
            // Normal BFS when not hunting
            if (bfsQueue.isEmpty()) {
                visitedRooms.clear();
                bfsQueue.add(currentRoomId);
                visitedRooms.add(currentRoomId);
            }

            if (!bfsQueue.isEmpty()) {
                // Remove Bracken from the current room (if necessary)
                Room currentRoom = map.get(currentRoomId);
                if (currentRoom != null) {
                    currentRoom.removeEntity(this);  // Assuming a removeEntity method exists to remove Bracken
                }

                // Move to the next room in the BFS queue
                currentRoomId = bfsQueue.poll();
                Room nextRoom = map.get(currentRoomId); // Get the current room using the currentRoomId
                if (nextRoom != null) {
                    // Place Bracken at the center of the new room
                    System.out.println("Bracken is exploring room: " + currentRoomId);
                    x = 530; // Set Bracken's X position to the center of the new room
                    y = 300; // Set Bracken's Y position to the center of the new room

                    // Add the new Bracken entity to the new room
                    nextRoom.addEntity(this);  // Assuming an addEntity method exists to add Bracken to the room

                    // Update the barriers to reflect the new room
                    this.barriers.clear();  // Remove the old barriers
                    this.barriers.addAll(nextRoom.getBarriers());  // Add the new barriers from the next room

                    // Start chasing the player if they are in the same room
                    if (player != null && currentRoomId.equals(player.getCurrentRoom())) {
                        setupPlayerChase(player, barriers); // Start chasing the player immediately if they're in the same room
                    }

                    // Iterate through the neighboring rooms and explore them
                    for (Room neighborRoom : nextRoom.getNeighbors(map)) {
                        String neighborId = neighborRoom.getId(); // Get the neighbor's ID
                        if (!visitedRooms.contains(neighborId) && !neighborId.equals("exit")) {
                            bfsQueue.add(neighborId);
                            visitedRooms.add(neighborId);
                            System.out.println("Bracken added neighbor room: " + neighborId + " to the queue.");
                        }
                    }
                }
            }

        } else {
            // Room traversal based on player's movement when hunting
            if (player != null) {
                // When the player leaves the room, Bracken will follow the player's next room
                String playerCurrentRoom = player.getCurrentRoom();

                // If Bracken is not already in the next room
                if (!currentRoomId.equals(playerCurrentRoom)) {
                    // Remove Bracken from the previous room
                    Room previousRoom = map.get(currentRoomId);
                    if (previousRoom != null) {
                        previousRoom.removeEntity(this);  // Remove Bracken from the previous room
                        System.out.println("Bracken removed from the room: " + currentRoomId);
                    }

                    // Add the player's current room to the queue
                    bfsQueue.add(playerCurrentRoom);
                    visitedRooms.add(playerCurrentRoom);
                    System.out.println("Bracken added player's next room: " + playerCurrentRoom + " to the queue.");

                    // Proceed to the next room in the queue
                    currentRoomId = bfsQueue.poll();
                    Room nextRoom = map.get(currentRoomId); // Get the room using the updated currentRoomId
                    if (nextRoom != null) {
                        // Place Bracken at the center of the new room
                        System.out.println("Bracken is following the player to room: " + currentRoomId);
                        x = 600; // Set Bracken's X position to the center of the new room
                        y = 300; // Set Bracken's Y position to the center of the new room

                        // Add the new Bracken entity to the new room
                        nextRoom.addEntity(this);  // Assuming an addEntity method exists to add Bracken to the room

                        // Update the barriers to reflect the new room
                        this.barriers.clear();  // Remove the old barriers
                        this.barriers.addAll(nextRoom.getBarriers());  // Add the new barriers from the next room

                        // Start chasing the player if they are in the same room
                        if (player != null && currentRoomId.equals(player.getCurrentRoom())) {
                            setupPlayerChase(player, barriers); // Start chasing the player immediately if they're in the same room
                        }
                    }
                }
            }
        }
    }

    public void stopRoomTraversal() {
        if (roomTraversalTimeline != null) {
            roomTraversalTimeline.stop();
        }
        System.out.println("Bracken has stopped room traversal.");
    }
}
