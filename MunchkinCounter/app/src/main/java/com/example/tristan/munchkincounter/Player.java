package com.example.tristan.munchkincounter;

/**
 * Player class contains information of the player's munchkin.
 * All stat changes are performed here.
 */
public class Player {
    private String name;
    private int level;
    private int gear;
    public int bonus;

    public String getName() {
        return name;
    }

    public int getTotal() {
        return level + gear + bonus;
    }

    public int getLevel() {
        return level;
    }

    public int getGear() { return gear; }

    public int getBonus() { return bonus; }

    public Player(String name) {
        this.name = name;
        reset();
    }

    public void reset() {
        level = 1;
        gear = 0;
        bonus = 0;
    }

    public void setLevelOne() { level = 1; }
    public void setGearZero() {
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
    public void incrementBonus() { bonus += 1; }
    public void decrementBonus() { bonus -= 1; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" - Level: ");
        sb.append(level);
        sb.append(" Total: ");
        sb.append(getTotal());

        return sb.toString();
    }
}
