package com.hananfinal2;

import java.io.Serializable;

public class Pet implements Serializable {

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
        if (hunger > 0.04) hunger -= 0.05;
        if (happiness > 0.09) happiness -= 0.1;
        if (energy > 0) energy -= 0.01;
        else energy = 0;
    }

    public void feed(float num) {
        if (hunger < 100 - num) hunger += num;
        else hunger = 100;
    }

    public void play(float num) {
        if (happiness < 100 - num) happiness += num;
        else happiness = 100;
    }

    public void sleep(float num) {
        if (energy < 100 - num) energy += num;
        else energy = 100;
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
