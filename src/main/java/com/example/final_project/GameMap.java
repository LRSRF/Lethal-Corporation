package com.example.final_project;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private String upMapKey;
    private String downMapKey;
    private String leftMapKey;
    private String rightMapKey;

    private List<Barrier> barriers;
    private List<Item> items;
    private List<Bracken> mobs;
    private String backgroundImagePath;

    public GameMap(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
        this.barriers = new ArrayList<>();
        this.items = new ArrayList<>();
        this.mobs = new ArrayList<>();
    }

    public String getUpMapKey() { return upMapKey; }
    public void setUpMapKey(String upMapKey) { this.upMapKey = upMapKey; }

    public String getDownMapKey() { return downMapKey; }
    public void setDownMapKey(String downMapKey) { this.downMapKey = downMapKey; }

    public String getLeftMapKey() { return leftMapKey; }
    public void setLeftMapKey(String leftMapKey) { this.leftMapKey = leftMapKey; }

    public String getRightMapKey() { return rightMapKey; }
    public void setRightMapKey(String rightMapKey) { this.rightMapKey = rightMapKey; }
    public void addBarrier(Barrier barrier) {
        barriers.add(barrier);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void addMob(Bracken bracken) {
        mobs.add(bracken);
    }

    public void draw(GraphicsContext gc) {
        // Draw the background image
        gc.drawImage(new Image(getClass().getResourceAsStream(backgroundImagePath)), 0, 0, 1280, 720);

        // Draw barriers
        for (Barrier barrier : barriers) {
            barrier.draw(gc);
        }

        // Draw items
        for (Item item : items) {
            item.draw(gc);
        }

        // Draw mobs
        for (Bracken bracken : mobs) {
            bracken.draw(gc);
        }
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Bracken> getMobs() {
        return mobs;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }
}
