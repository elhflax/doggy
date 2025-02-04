package com.hananfinal2;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class DogFragment extends Fragment {

    private ImageView dogImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dog, container, false);

        // Find the ImageView for the dog
        dogImageView = view.findViewById(R.id.dog_image);

        // Set the animation drawable if dogImageView is not null
        if (dogImageView != null) {
            dogImageView.setImageResource(R.drawable.dog_animation);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if the ImageView and the drawable are not null
        if (dogImageView != null) {
            Drawable drawable = dogImageView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                AnimationDrawable animation = (AnimationDrawable) drawable;
                animation.start();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Stop the animation when the fragment is no longer visible
        if (dogImageView != null) {
            Drawable drawable = dogImageView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                AnimationDrawable animation = (AnimationDrawable) drawable;
                animation.stop();
            }
        }
    }
}
