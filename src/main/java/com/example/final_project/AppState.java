package com.example.final_project;

import javafx.scene.media.MediaPlayer;

public class AppState {
    private static AppState instance = null;
    private double brightness = 1.0;
    private double volume = 1.0; // Default volume is 100%
    private MediaPlayer mediaPlayer;

    private AppState() {}

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.mediaPlayer.setVolume(volume);
    }
}
