package com.hananfinal2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import android.Manifest;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        Intent intent1 = new Intent();
        intent1.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        startActivity(intent1);

        if (auth.getCurrentUser() != null ) {
            fetchPet(auth.getCurrentUser().getUid());
        } else {
            showFragment(new LoginFragment());
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    private void fetchPet(String uid) {
        FirebaseFirestore.getInstance()
                .collection("pets")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Pet pet = documentSnapshot.toObject(Pet.class);
                        if (pet != null) {
                            PetManager.getInstance().setPet(pet);
                            startActivity(new Intent(this, PetHomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to load pet data", Toast.LENGTH_SHORT).show();
                            showFragment(new LoginFragment());
                        }
                    } else {
                        Toast.makeText(this, "No pet found for this account", Toast.LENGTH_SHORT).show();
                        showFragment(new LoginFragment());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching pet: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    showFragment(new LoginFragment());
                });
    }

}
