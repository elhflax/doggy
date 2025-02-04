package com.hananfinal2;

import java.io.Serializable;

public class Pet implements Serializable {
    private static final int MAX_HUNGER = 100;
    private static final int MAX_HAPPINESS = 100;
    private static final int MAX_ENERGY = 100;

    private static final int MIN_HUNGER = 0;
    private static final int MIN_HAPPINESS = 0;
    private static final int MIN_ENERGY = 0;

    private int hunger;
    private int happiness;
    private int energy;
    private boolean sleeping;

    public Pet() {
        hunger = 50;
        happiness = 50;
        energy = 50;
        sleeping = false;
    }

    public void decrement() {
        if (hunger > MIN_HUNGER) hunger -= 5;
        if (happiness > MIN_HAPPINESS) happiness -= 3;
        if (energy > MIN_ENERGY) energy -= 3;
        else energy = MIN_ENERGY; // Ensure energy doesn't go negative
    }

    public void feed() {
        if (hunger < MAX_HUNGER - 20) hunger += 20;
        else hunger = MAX_HUNGER;  // Cap hunger at 100
    }

    public void play() {
        if (happiness < MAX_HAPPINESS - 20) happiness += 20;
        else happiness = MAX_HAPPINESS;  // Cap happiness at 100
    }

    public void sleep() {
        if (energy < MAX_ENERGY - 25) energy += 25;
        else energy = MAX_ENERGY;  // Cap energy at 100
    }

    // Getters
    public int getHunger() {
        return hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean getSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    // Check if the pet is unhappy
    public boolean isUnhappy() {
        return hunger == MIN_HUNGER || happiness == MIN_HAPPINESS || energy == MIN_ENERGY;
    }
}
