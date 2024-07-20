package com.example.final_project;


import javafx.scene.media.MediaPlayer;

public class AppState {
    private static AppState instance = null;
    private double brightness = 1.0;
    private MediaPlayer mediaPlayer;

    // Private constructor for singleton pattern
    private AppState() {}

    // Get the singleton instance of AppState
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    // Getters and setters for brightness
    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    // Getters and setters for MediaPlayer
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
}