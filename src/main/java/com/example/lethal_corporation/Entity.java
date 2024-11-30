package com.example.lethal_corporation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

abstract class Entity {
    protected double x, y, speed;
    protected Image currentImage;

    public Entity(double x, double y, double speed, String imagePath1, String imagePath2, String imagePath3, String imagePath4) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.currentImage = new Image(getClass().getResourceAsStream(imagePath1));
    }

    public abstract void draw(GraphicsContext gc);
    public abstract void animate();
    public abstract void move(double dx, double dy);

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return currentImage.getWidth(); }
    public double getHeight() { return currentImage.getHeight(); }
}