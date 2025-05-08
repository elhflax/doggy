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
    private boolean sleeping = false;
    private String dogType;
    private String name;
    public Pet() {
    }
    public Pet(String name, String dogType, int happiness, int hunger, int energy) {
        this.name = name;
        this.dogType = dogType;
        this.happiness = happiness;
        this.hunger = hunger;
        this.energy = energy;
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
    public String getDogType(){return dogType;}
    public String getName(){return name;}

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }
    public void setHunger(float hunger) {
        this.hunger = hunger;
    }

    public void setHappiness(float happiness) {
        this.happiness = happiness;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public void setDogType(String dogType) {
        this.dogType = dogType;
    }

    public void setName(String name) {
        this.name = name;
    }


}
