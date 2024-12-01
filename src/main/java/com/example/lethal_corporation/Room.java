package com.example.lethal_corporation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private String id; // Unique identifier for the room
    private Map<String, String> neighbors; // Direction to connected room ID (left, right, up, down)
    private List<Barrier> barriers;
    private List<Item> items;
    private List<Bracken> mobs;
    private String backgroundImagePath;

    public Room(String id, String backgroundImagePath) {
        this.id = id;
        this.backgroundImagePath = backgroundImagePath;
        this.neighbors = new HashMap<>();
        this.barriers = new ArrayList<>();
        this.items = new ArrayList<>();
        this.mobs = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void addNeighbor(String direction, String roomId) {
        // Store connections in the neighbors room (direction -> roomId)
        neighbors.put(direction, roomId);
    }

    public String getNeighbor(String direction) {
        return neighbors.get(direction);
    }

    public List<Room> getNeighbors(Map<String, Room> map) {
        List<Room> neighborRooms = new ArrayList<>();
        for (String direction : neighbors.keySet()) {
            String neighborId = neighbors.get(direction);
            Room neighborRoom = map.get(neighborId); // Fetch the room by its ID directly from the map
            if (neighborRoom != null) {
                neighborRooms.add(neighborRoom);
            }
        }
        return neighborRooms;
    }

    // Method to add an entity to the room
    public void addEntity(Bracken entity) {
        mobs.add(entity);
    }

    // Method to remove an entity from the room
    public void removeEntity(Bracken entity) {
        mobs.remove(entity);
    }

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
        // Draw the background image for the room
        gc.drawImage(new Image(getClass().getResourceAsStream(backgroundImagePath)), 0, 0, 1280, 720);

        // Draw all barriers, items, and mobs
        for (Barrier barrier : barriers) {
            barrier.draw(gc);
        }

        for (Item item : items) {
            item.draw(gc);
        }

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
}