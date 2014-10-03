package com.example.tristan.munchkincounter;

import java.io.Serializable;

/**
 * Player class contains information of the player's munchkin.
 * All stat changes are performed here.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int level;
    private int gear;

    public String getName() {
        return name;
    }

    public int getTotal() {
        return level + gear;
    }

    public int getLevel() {
        return level;
    }

    public int getGear() { return gear; }

    public Player(String name) {
        this.name = name;
        level = 1;
        gear = 0;
    }

    public void incrementLevel() {
        level += 1;
    }

    public void decrementLevel() {
        if (level - 1 > 0) {
            level -= 1;
        }
    }

    public void incrementGear() {
        gear += 1;
    }

    public void decrementGear() {
        gear -= 1;
    }
}
