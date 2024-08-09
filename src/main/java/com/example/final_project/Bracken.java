package com.example.final_project;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Bracken extends Entity {
    private Image brackenImage1, brackenImage2, brackenImage3, brackenImage4;
    private Timeline movementTimeline;

    public Bracken(double x, double y, double speed, String imagePath1, String imagePath2, String imagePath3, String imagePath4) {
        super(x, y, speed, imagePath1, imagePath2, imagePath3, imagePath4);
        brackenImage1 = new Image(getClass().getResourceAsStream(imagePath1));
        brackenImage2 = new Image(getClass().getResourceAsStream(imagePath2));
        currentImage = brackenImage1;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(currentImage, x, y, 175, 175);
    }

    @Override
    public void animate() {
        currentImage = currentImage == brackenImage1 ? brackenImage2 : brackenImage1;
    }

    @Override
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void setupMovement(Player player) {
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
            double playerX = player.getX();
            double playerY = player.getY();
            double dx = playerX > x ? speed : -speed;
            double dy = playerY > y ? speed : -speed;

            move(dx, dy);
            animate();
        }));
        movementTimeline.setCycleCount(Animation.INDEFINITE);
        movementTimeline.play();
    }

    public void stopMovement() {
        if (movementTimeline != null) {
            movementTimeline.stop();
        }
    }
}