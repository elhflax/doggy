package com.hananfinal2;

public class PetManager {
    private static PetManager instance;
    private Pet pet;

    private PetManager() {
        // Private constructor to prevent instantiation
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
