package com.example.final_project;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Bracken extends Entity {
    private Image brackenImageRight1, brackenImageRight2, brackenImageLeft1, brackenImageLeft2;
    private Timeline movementTimeline;
    private boolean movingRight = true;
    private boolean imageToggle = true;

    public Bracken(double x, double y, double speed, String imagePathRight1, String imagePathRight2, String imagePathLeft1, String imagePathLeft2) {
        super(x, y, speed, imagePathRight1, imagePathRight2, imagePathLeft1, imagePathLeft2);
        brackenImageRight1 = new Image(getClass().getResourceAsStream(imagePathRight1));
        brackenImageRight2 = new Image(getClass().getResourceAsStream(imagePathRight2));
        brackenImageLeft1 = new Image(getClass().getResourceAsStream(imagePathLeft1));
        brackenImageLeft2 = new Image(getClass().getResourceAsStream(imagePathLeft2));
        currentImage = brackenImageRight1;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(currentImage, x, y, 175, 175);
    }

    @Override
    public void animate() {
        // Alternate the image based on direction
        if (movingRight) {
            currentImage = imageToggle ? brackenImageRight1 : brackenImageRight2;
        } else {
            currentImage = imageToggle ? brackenImageLeft1 : brackenImageLeft2;
        }
        imageToggle = !imageToggle;
    }

    @Override
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
        movingRight = dx > 0;
    }

    public void setupMovement(Player player) {
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(500), event -> { // Animation update frequency
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