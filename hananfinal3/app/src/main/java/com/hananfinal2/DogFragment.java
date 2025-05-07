package com.hananfinal2;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DogFragment extends Fragment {

    private ImageView dogImageView;
    private AnimationDrawable dogAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog, container, false);
        dogImageView = view.findViewById(R.id.home_dog);

        if (dogImageView != null) {
            dogImageView.setImageResource(R.drawable.dog1_idle);
            dogAnimation = (AnimationDrawable) dogImageView.getDrawable();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (dogAnimation != null && !dogAnimation.isRunning()) {
            dogAnimation.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (dogAnimation != null && dogAnimation.isRunning()) {
            dogAnimation.stop();
        }
    }
}
