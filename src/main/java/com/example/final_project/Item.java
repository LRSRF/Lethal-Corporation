package com.example.final_project;


import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class Item {
    private double x, y;
    private double width, height;
    private boolean collected;
    private int value;
    private Image image;
    private int collectedCount = 0;
    @FXML
    private Text Gearnumber;
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
        return playerX < x + width && playerX + playerWidth > x && playerY < y + height && playerY + playerHeight > y;
    }

    public double getValue() {
        return value;
    }

    public void collect() {
        collected = true;

        System.out.println("Item collected with value: " + value);

    }

    // Getters and setters for x, y, width, height, and value
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
