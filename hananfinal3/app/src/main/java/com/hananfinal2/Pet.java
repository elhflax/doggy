package com.hananfinal2;

import java.io.Serializable;

public class Pet implements Serializable {
    private static final int MAX_HUNGER = 100;
    private static final int MAX_HAPPINESS = 100;
    private static final int MAX_ENERGY = 100;

    private static final int MIN_HUNGER = 0;
    private static final int MIN_HAPPINESS = 0;
    private static final int MIN_ENERGY = 0;

    private float hunger;
    private float happiness;
    private float energy;
    private boolean sleeping;

    public Pet() {
        hunger = 50;
        happiness = 50;
        energy = 50;
        sleeping = false;
    }

    public void decrement() {
        if (hunger > MIN_HUNGER) hunger -= 0.05;
        if (happiness > MIN_HAPPINESS) happiness -= 0.1;
        if (energy > MIN_ENERGY) energy -= 0.01;
        else energy = MIN_ENERGY;
    }

    public void feed(float num) {
        if (hunger < MAX_HUNGER - num) hunger += num;
        else hunger = MAX_HUNGER;
    }

    public void play(float num) {
        if (happiness < MAX_HAPPINESS - num) happiness += num;
        else happiness = MAX_HAPPINESS;
    }

    public void sleep(float num) {
        if (energy < MAX_ENERGY - num) energy += num;
        else energy = MAX_ENERGY;
    }

    // Getters
    public float getHunger() {
        return hunger;
    }

    public float getHappiness() {
        return happiness;
    }

    public float getEnergy() {
        return energy;
    }

    public boolean getSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

}
