package com.example.lethal_corporation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Item {
    private double x, y;
    private double width, height;
    private int collectedcount;
    private int value;
    private Image image;
    private String name;
    private String imagePath;
    private boolean weight ;
    private boolean collectible = true;

    public Item(double x, double y, double value, String imagePath, String itemName) {
        this.x = x;
        this.y = y;
        this.value = (int) value;
        this.imagePath = imagePath; // Store the image path
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream == null) {
            throw new IllegalArgumentException("Image not found: " + imagePath);
        }
        this.image = new Image(imageStream);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.name = itemName;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    public boolean checkCollision(double playerX, double playerY, double playerWidth, double playerHeight) {
        return playerX < x + width && playerX + playerWidth > x && playerY < y + height && playerY + playerHeight > y;
    }

    public double getValue() {
        return value;
    }


    public void collect() {
        Game game = Game.getCurrentGame(); // Retrieve the active Game instance

        if (game.getCollectedCount() < 4) { // Use game-level count

            game.addInventory(name, imagePath, value);
            System.out.println("Item collected with value: " + value);
            System.out.println("Total collected: " + game.getCollectedCount());




        }

    }

    // Getters and setters for x, y, width, height, and value
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}