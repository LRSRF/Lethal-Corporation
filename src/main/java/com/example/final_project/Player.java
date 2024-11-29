package com.example.final_project;

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

    public Player(Game game, double x, double y, double speed, List<Barrier> barriers, String imagePath1, String imagePath2, String imagePath3, String imagePath4) {
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

        // Handle movement keys
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

        // Check for barrier collision
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

        // Check for item collision
        GameMap currentMap = game.getCurrentMap(); // Get current map from game
        Iterator<Item> iterator = currentMap.getItems().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.checkCollision(x, y, getWidth(), getHeight())) {
                // Check if inventory is full
                if (game.getCollectedCount() < 4) { // Assuming a max capacity of 4
                    collectedItems.add(item);
                    item.collect(); // Collect the item
                    iterator.remove();
                    System.out.println("removed");// Remove the item from the map
                    game.incrementCollectedCount();
                } else {
                    System.out.println("Inventory full! Can't collect item.");
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
}
