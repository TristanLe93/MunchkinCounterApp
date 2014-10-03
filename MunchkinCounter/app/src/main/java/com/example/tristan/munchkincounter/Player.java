package com.example.tristan.munchkincounter;

/**
 * Player class contains information of the player's munchkin.
 * All stat changes are performed here.
 */
public class Player {

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

    public Player(String name) {
        this.name = name;
        level = 1;
        gear = 0;
    }

    public void incrementLevel() {
        level += 1;
    }

    public void decrementLevel() {
        if (level - 1 > 1) {
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
