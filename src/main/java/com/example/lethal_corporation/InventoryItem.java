package com.example.lethal_corporation;


import javafx.scene.image.Image;


public class InventoryItem {
    private String name;
    private Image image;
    private String imagePath;
    private double value;
    // Constructor
    public InventoryItem(String name, String imagePath, double value) {
        this.name = name;
        this.imagePath = imagePath;
        this.value = value;

        // Use getResourceAsStream for loading resources from classpath
        if (imagePath != null) {
            this.image = new Image(getClass().getResourceAsStream(imagePath));
            if (this.image.isError()) {
                System.err.println("Failed to load image from path: " + imagePath);
            }
        } else {
            throw new IllegalArgumentException("Image path cannot be null for InventoryItem: " + name);
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getValue() {
        return value;
    }
}