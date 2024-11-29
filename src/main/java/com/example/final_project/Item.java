package com.example.final_project;


import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class Item {
    private double x, y;
    private double width, height;
    private int value;
    private Image image;
    private boolean collectible = true;

    public Item(double x, double y, int value, String imagePath) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.image = new Image(getClass().getResourceAsStream(imagePath));
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    public boolean checkCollision(double playerX, double playerY, double playerWidth, double playerHeight) {
        if (!collectible) {
            return false; // Skip collision checking if the item is not collectible
        }
        return playerX < x + width && playerX + playerWidth > x && playerY < y + height && playerY + playerHeight > y;
    }

    public double getValue() {
        return value;
    }

    public void collect() {
        Game game = Game.getCurrentGame(); // Retrieve the active Game instance

        if (game.getCollectedCount() < 4) { // Use game-level count

            game.addInventory("/Axel.png");
            System.out.println("Item collected with value: " + value);
            System.out.println("Total collected: " + game.getCollectedCount());
        } else {
            System.out.println("Inventory full. Disabling collection for this item.");
            collectible = false; // Mark this item as no longer collectible
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
