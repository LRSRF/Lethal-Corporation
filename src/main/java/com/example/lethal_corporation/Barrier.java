package com.example.lethal_corporation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Barrier {
    private double x, y, width, height;

    public Barrier(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(x, y, width, height);
    }

    public boolean checkCollision(double entityX, double entityY, double entityWidth, double entityHeight) {
        return entityX < x + width && entityX + entityWidth > x && entityY < y + height && entityY + entityHeight > y;
    }
}
