package com.example.lethal_corporation;

import javafx.scene.input.KeyEvent;
import javafx.scene.control.TreeTableView;

public class PlayerInventory {
    public TreeTableView<?> Inventory;

    public void handleKeyPress(KeyEvent event) {
        if (event.getCode().toString().equals("I")) {
            // Toggle opacity
            if (Inventory.getOpacity() == 0.0) {
                Inventory.setOpacity(1.0); // Show inventory
            } else {
                Inventory.setOpacity(0.0); // Hide inventory
            }
        }
    }
}

