package com.example.lethal_corporation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends Entity {
    private Image spriteImage1, spriteImage2, spriteImage3, spriteImage4;
    private boolean isMoving = false;
    private boolean direction = true; // true = facing right, false = facing left
    private List<Barrier> barriers;
    private List<Item> collectedItems;
    private double x, y; // Ensure these are initialized
    private Game game;
    private String currentRoom;

    public Player(Game game, double x, double y, double speed, List<Barrier> barriers, String imagePath1, String imagePath2, String imagePath3, String imagePath4, String currentRoom) {
        super(x, y, speed, imagePath1, imagePath2, imagePath3, imagePath4);
        spriteImage1 = new Image(getClass().getResourceAsStream(imagePath1));
        spriteImage2 = new Image(getClass().getResourceAsStream(imagePath2));
        spriteImage3 = new Image(getClass().getResourceAsStream(imagePath3));
        spriteImage4 = new Image(getClass().getResourceAsStream(imagePath4));
        currentImage = spriteImage1;
        this.barriers = barriers;
        this.x = x;
        this.y = y;
        this.collectedItems = new ArrayList<>();
        this.game = game;
        this.currentRoom = currentRoom;
        resetImage();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(currentImage, x, y);
    }

    @Override
    public void animate() {
        if (isMoving) {
            if (direction) {
                // Move right
                currentImage = (currentImage == spriteImage1) ? spriteImage2 : spriteImage1;
            } else {
                // Move left
                currentImage = (currentImage == spriteImage3) ? spriteImage4 : spriteImage3;
            }
        } else {
            // Reset to standing image
            currentImage = direction ? spriteImage1 : spriteImage3;
        }
    }

    @Override
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void setBarriers(List<Barrier> barriers) {
        this.barriers = barriers;
    }

    public void handleKeyPress(KeyEvent event) {
        double newX = x;
        double newY = y;

        if (event.getCode() == KeyCode.D) {
            newX += speed;
            direction = true;
        } else if (event.getCode() == KeyCode.A) {
            newX -= speed;
            direction = false;
        } else if (event.getCode() == KeyCode.W) {
            newY -= speed;
        } else if (event.getCode() == KeyCode.S) {
            newY += speed;
        }

        boolean collision = false;
        if (barriers != null) {
            for (Barrier barrier : barriers) {
                if (barrier.checkCollision(newX, newY, getWidth(), getHeight())) {
                    collision = true;
                    break;
                }
            }
        }

        if (!collision) {
            x = newX;
            y = newY;
        }

        isMoving = !collision;

        if (game.getCollectedCount() < 4) {// Check for item collision
            Room currentRoom = game.getCurrentRoom(); // Get current room from game
            Iterator<Item> iterator = currentRoom.getItems().iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item.checkCollision(x, y, getWidth(), getHeight())) {
                    collectedItems.add(item);
                    item.collect();
                    iterator.remove(); // Safe removal
                }
            }
        }
    }
    public void handleKeyRelease(KeyEvent event) {
        // Implement logic for when a key is released, if needed
        isMoving = false;
        resetImage();
    }

    public void resetImage() {
        currentImage = direction ? spriteImage1 : spriteImage3;
    }

    public boolean isMoving() { return isMoving; }

    public void setMoving(boolean isMoving) { this.isMoving = isMoving; }

    public boolean checkCollision(double otherX, double otherY, double otherWidth, double otherHeight) {
        return x < otherX + otherWidth && x + getWidth() > otherX && y < otherY + otherHeight && y + getHeight() > otherY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public List<Item> getCollectedItems() {
        return collectedItems;
    }

    // Method to add an item to the collected items list
    public void collectItem(Item item) {
        collectedItems.add(item);
    }

    // Method to check if an item is collected
    public boolean hasCollected(Item item) {
        return collectedItems.contains(item);
    }

    // Method to get total value of collected items
    public double getTotalCollectedValue() {
        return collectedItems.stream().mapToDouble(Item::getValue).sum();
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }
}
