package com.example.tristan.munchkincounter;


/**
 * Player class contains information of the player's munchkin.
 * All stat changes are performed here.
 */
public class Player {
    private String name;
    private int level;
    private int gear;
    private int bonus;
    private boolean isMale;

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

    public boolean isMale() {
        return isMale;
    }

    public Player(String name) {
        this.name = name;
        isMale = true;
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
    public void setMale() { isMale = true; }
    public void setFemale() { isMale = false; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" - [");
        sb.append(getTotal());
        sb.append("] (Level: ");
        sb.append(level);
        sb.append(")");

        return sb.toString();
    }
}
