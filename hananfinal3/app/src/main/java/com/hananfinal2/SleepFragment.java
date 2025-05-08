package com.hananfinal2;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SleepFragment extends Fragment {

    private ImageView dogImageView;
    private AnimationDrawable dogAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep, container, false);
        dogImageView = view.findViewById(R.id.sleep_dog);
        String dogType = PetManager.getInstance().getPet().getDogType();
        if (dogImageView != null) {
            int animationResId = getResources().getIdentifier(dogType + "_sleep", "drawable", requireContext().getPackageName());
            dogImageView.setImageResource(animationResId);
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
