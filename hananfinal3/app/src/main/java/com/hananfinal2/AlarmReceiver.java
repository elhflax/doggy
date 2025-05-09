package com.hananfinal2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {
    private String title;
    private String message;
    private int notificationId;
    

    @Override
    public void onReceive(Context context, Intent intent) {
         title = intent.getStringExtra("title");
         message = intent.getStringExtra("message");
         notificationId = intent.getIntExtra("notificationId", 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("pet_alarm_channel", "Pet Alarms", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        switch (notificationId) {
            case 404:
                updatePetStats();
                break;
            default:
                showNotification(context);
                break;
        }
    }

    private void updatePetStats() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        
        if (userId == null) {
            Log.d("AlarmReceiver", "No user logged in");
            return;
        }

        db.collection("pets")
            .document(userId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Pet pet = documentSnapshot.toObject(Pet.class);
                    if (pet != null) {
                        // Update stats based on sleeping status
                        if (pet.getSleeping()) {
                            float newEnergy = Math.min(100, pet.getEnergy() + 50);
                            pet.setEnergy(newEnergy);
                        } else {
                            float newHunger = Math.max(0, pet.getHunger() - 25);
                            float newHappiness = Math.max(0, pet.getHappiness() - 25);
                            float newEnergy = Math.max(0, pet.getEnergy() - 25);
                            
                            pet.setHunger(newHunger);
                            pet.setHappiness(newHappiness);
                            pet.setEnergy(newEnergy);
                        }

                        // Update the database
                        Map<String, Object> petData = new HashMap<>();
                        petData.put("hunger", pet.getHunger());
                        petData.put("happiness", pet.getHappiness());
                        petData.put("energy", pet.getEnergy());
                        petData.put("sleeping", pet.getSleeping());
                        petData.put("name", pet.getName());

                        db.collection("pets")
                            .document(userId)
                            .update(petData)
                            .addOnSuccessListener(aVoid -> 
                                Log.d("AlarmReceiver", "Pet stats updated successfully"))
                            .addOnFailureListener(e -> 
                                Log.e("AlarmReceiver", "Error updating pet stats: " + e.getMessage()));
                    }
                }
            })
            .addOnFailureListener(e -> 
                Log.e("AlarmReceiver", "Error fetching pet data: " + e.getMessage()));
    }

    private void showNotification(Context context) {
        Intent openIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "pet_alarm_channel")
                .setSmallIcon(R.drawable.paw)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
//101- forgot sleep
//202 - hungry
//303 - sad
//404 - decrease happiness and hunger
