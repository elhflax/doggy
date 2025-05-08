package com.hananfinal2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class GameSelectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_selection, container, false);

        Button game1Button = view.findViewById(R.id.game1_button);
        Button game2Button = view.findViewById(R.id.game2_button);
        Button game3Button = view.findViewById(R.id.game3_button);

        game1Button.setOnClickListener(v -> {
            ClickGameFragment clickGameFragment = new ClickGameFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, clickGameFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        game2Button.setOnClickListener(v -> {
            // TODO: Implement Hide and Seek game
            Toast.makeText(getContext(), "Hide and Seek game coming soon!", Toast.LENGTH_SHORT).show();
        });

        game3Button.setOnClickListener(v -> {
            // TODO: Implement Memory Match game
            Toast.makeText(getContext(), "Memory Match game coming soon!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
} 