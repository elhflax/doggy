package com.hananfinal2;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import android.Manifest;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private FrameLayout fragmentContainer;
    private Button btnLogin, btnRegister;
    private ImageView ivLogo;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        fragmentContainer = findViewById(R.id.fragment_container);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        ivLogo = findViewById(R.id.ivLogo);
        tvTitle = findViewById(R.id.tvTitle);


        btnLogin.setOnClickListener(v -> {
            hideWelcomeScreen();
            showFragment(new LoginFragment());
        });

        btnRegister.setOnClickListener(v -> {
            hideWelcomeScreen();
            showFragment(new RegisterFragment());
        });

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(this, "Notification permission is required for pet reminders", Toast.LENGTH_LONG).show();
                    }
                }
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        if (auth.getCurrentUser() != null) {
            hideWelcomeScreen();
            fetchPet(auth.getCurrentUser().getUid());
        }
    }

    private void hideWelcomeScreen() {
        ivLogo.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
    }

    private void showWelcomeScreen() {
        ivLogo.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        fragmentContainer.setVisibility(View.GONE);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentContainer.getVisibility() == View.VISIBLE) {
            showWelcomeScreen();
        } else {
            super.onBackPressed();
        }
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
