package com.hananfinal2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class KitchenFragment extends Fragment {

    private EditText etKitchen;
    private Button btnFinishEating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kitchen, container, false);

        // Initialize views
        etKitchen = view.findViewById(R.id.etKitchen);
        btnFinishEating = view.findViewById(R.id.btnFinishEating);

        // Set default text in the EditText using string resource
        if (etKitchen != null) {
            etKitchen.setText("Welcome to the kitchen");  // Use string resource instead of hardcoding
        }

        // Set the button click listener to replace the fragment
        btnFinishEating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishEating();
            }
        });

        return view;
    }

    // Method to get the input from the EditText
    public String getFoodInput() {
        return etKitchen != null ? etKitchen.getText().toString() : "";
    }

    // Method to replace EatFragment with DogFragment
    private void finishEating() {
        if (getActivity() != null) {
            replaceFragment(new DogFragment());  // Using helper method to handle fragment replacement
        }
    }

    // Helper method for fragment replacement
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Optional: add to back stack to allow navigation back
        transaction.commit();
    }
}
