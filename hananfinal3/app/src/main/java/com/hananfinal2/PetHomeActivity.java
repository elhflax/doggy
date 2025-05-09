package com.hananfinal2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class PetHomeActivity extends AppCompatActivity {

    protected Context context;
    protected Pet pet;
    protected ProgressBar hungerMeter, happinessMeter, energyMeter;
    ImageView feedButton, playButton, sleepButton;
    protected boolean onScreen = true;
    protected ConstraintLayout constraintLayout;
    protected View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_home);

        context = this;
        pet = PetManager.getInstance().getPet();

        feedButton = findViewById(R.id.feed_button);
        playButton = findViewById(R.id.play_button);
        sleepButton = findViewById(R.id.sleep_button);
        hungerMeter = findViewById(R.id.hunger_meter);
        happinessMeter = findViewById(R.id.happiness_meter);
        energyMeter = findViewById(R.id.energy_meter);
        constraintLayout = findViewById(R.id.constraintLayout);
        fragmentContainer = findViewById(R.id.fragment_container);

        ViewGroup.LayoutParams params = fragmentContainer.getLayoutParams();
        params.height = constraintLayout.getHeight() - hungerMeter.getHeight();
        fragmentContainer.setLayoutParams(params);

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new KitchenFragment());

                if (pet != null) {
                    updateUI();
                } else {
                    Toast.makeText(context, "Pet is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DogFragment());
            }
        });

        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pet != null) {
                    if (!pet.getSleeping()) {
                        pet.setSleeping(true);
                        constraintLayout.setBackgroundColor(Color.DKGRAY);
                        Buttons(false);
                        replaceFragment(new SleepFragment());
                    } else {
                        pet.setSleeping(false);
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.bakcgroundlightgreen));
                        Buttons(true);
                        replaceFragment(new DogFragment());
                    }
                    updateUI();
                } else {
                    Toast.makeText(context, "Pet is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(pet.getSleeping()){
            constraintLayout.setBackgroundColor(Color.DKGRAY);
            replaceFragment(new SleepFragment());
        }
        else {
            replaceFragment(new DogFragment());}
        startAutoDecrement();
        updateUI();
    }

    private void startAutoDecrement() {

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!pet.getSleeping()) {
                    pet.decrement();
                } else {
                    pet.sleep(0.05f);
                }
                updateUI();
                handler.postDelayed(this, 100);
            }
        };

        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        Map<String, Object> petData = new HashMap<>();
        petData.put("hunger", pet.getHunger());
        petData.put("happiness", pet.getHappiness());
        petData.put("energy", pet.getEnergy());
        petData.put("sleeping", pet.getSleeping());

        db.collection("pets")
            .document(auth.getCurrentUser().getUid())
            .update(petData)
            .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreUpdate", "Pet data updated successfully");})
            .addOnFailureListener(e -> {
                db.collection("pets")
                    .document(auth.getCurrentUser().getUid())
                    .set(petData);
                Log.d("FirestoreUpdate", "Pet data failed!");
            });

        if (!pet.getSleeping()) {
            AlarmHelper.scheduleAlarm(
                    this,
                    5000,
                    "\uD83D\uDCA4",
                    "Oops! Looks like you forgot to put your puppy to sleep.",
                    101,
                    false
            );
        }
        AlarmHelper.scheduleAlarm(
                this,
                3600000,
                "stat decrease",
                "every 1 hour",
                404,
                true
        );

    }

    private void updateUI() {
        if (pet != null) {
            hungerMeter.setProgress((int)pet.getHunger());
            happinessMeter.setProgress((int)pet.getHappiness());
            energyMeter.setProgress((int)pet.getEnergy());
        }
    }
    private void replaceFragment(Fragment newFragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null || !currentFragment.getClass().equals(newFragment.getClass())) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
    private void Buttons(boolean bool) {
        feedButton.setEnabled(bool);
        playButton.setEnabled(bool);
    }
} 