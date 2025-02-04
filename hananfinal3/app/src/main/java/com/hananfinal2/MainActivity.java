package com.hananfinal2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    protected Context context;
    protected Pet pet;
    protected ProgressBar hungerMeter, happinessMeter, energyMeter;
    ImageView feedButton, playButton, sleepButton;
    protected boolean onScreen = true;
    protected ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the pet if it doesn't exist
        if (PetManager.getInstance().getPet() == null) {
            pet = new Pet();
            PetManager.getInstance().setPet(pet);
        } else {
            pet = PetManager.getInstance().getPet();
        }

        context = this; // Use the activity context

        // Find the buttons and set click listeners
        feedButton = findViewById(R.id.feed_button);
        playButton = findViewById(R.id.play_button);
        sleepButton = findViewById(R.id.sleep_button);
        hungerMeter = findViewById(R.id.hunger_meter);
        happinessMeter = findViewById(R.id.happiness_meter);
        energyMeter = findViewById(R.id.energy_meter);
        constraintLayout = findViewById(R.id.constraintLayout);

        // Set click listeners for feed button
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Replace the current fragment with KitchenFragment
                KitchenFragment kitchenFragment = new KitchenFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, kitchenFragment)
                        .addToBackStack(null) // Add to back stack if you want the user to navigate back
                        .commit();

                // Retrieve the food input from KitchenFragment
                String foodInput = kitchenFragment.getFoodInput();
                Toast.makeText(context, "Food Input: " + foodInput, Toast.LENGTH_SHORT).show();

                // Perform the feed action on the pet
                if (pet != null) {
                    pet.feed();
                    updateUI();  // Update the UI to reflect changes (e.g., happiness, energy)
                } else {
                    Toast.makeText(context, "Pet is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listeners for play button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Perform the play action on the pet
                if (pet != null) {
                    pet.play();
                    updateUI();  // Update the UI to reflect changes (e.g., happiness, energy)
                } else {
                    Toast.makeText(context, "Pet is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listeners for sleep button
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pet != null) {
                    if (!pet.getSleeping()) {
                        // If pet is not sleeping, set it to sleeping
                        pet.setSleeping(true);
                        constraintLayout.setBackgroundColor(Color.DKGRAY); // Change background to indicate sleep
                        Buttons(false);  // Disable buttons while pet is sleeping
                    } else {
                        // If pet is already sleeping, wake it up
                        pet.setSleeping(false);
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.bakcgroundlightgreen));
                        Buttons(true);  // Enable buttons again
                    }

                    // Always update the UI to reflect changes
                    updateUI();
                } else {
                    Toast.makeText(context, "Pet is not available", Toast.LENGTH_SHORT).show(); // Pet is null
                }
            }
        });

        // Start the auto-decrement process for pet stats
        startAutoDecrement();
        updateUI();

        // Add DogFragment dynamically
        //if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DogFragment()) // Add DogFragment to the container
                    .commit();
        //}
    }

    private void startAutoDecrement() {
        if (pet == null) {
            // Initialize the pet object if it's not already initialized
            pet = new Pet();
            PetManager.getInstance().setPet(pet);
        }

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!pet.getSleeping()) {
                    pet.decrement();
                } else {
                    pet.sleep();
                }
                updateUI();
                handler.postDelayed(this, 2000); // Repeat every 2 seconds
            }
        };

        handler.post(runnable);
    }

    private void updateUI() {
        if (pet != null) {
            hungerMeter.setProgress(pet.getHunger());
            happinessMeter.setProgress(pet.getHappiness());
            energyMeter.setProgress(pet.getEnergy());
        }
    }

    private void Buttons(boolean bool) {
        feedButton.setEnabled(bool);
        playButton.setEnabled(bool);
    }
}
