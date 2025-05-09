    package com.hananfinal2;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.FrameLayout;
    import android.widget.RadioButton;
    import android.widget.RadioGroup;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;

    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.firestore.FirebaseFirestore;

    import java.util.HashMap;
    import java.util.Map;

    public class RegisterFragment extends Fragment {
        private TextInputEditText petNameInput;
        private TextInputEditText usernameInput;
        private TextInputEditText passwordInput;
        private RadioGroup dogSelectionGroup;
        private Button registerButton;
        private FirebaseAuth auth;
        private FirebaseFirestore db;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_register, container, false);

            petNameInput = view.findViewById(R.id.pet_name_input);
            usernameInput = view.findViewById(R.id.username_input);
            passwordInput = view.findViewById(R.id.password_input);
            dogSelectionGroup = view.findViewById(R.id.dog_selection_group);
            registerButton = view.findViewById(R.id.register_button);

            auth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            dogSelectionGroup.setOnCheckedChangeListener((group, checkedId) -> {
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (child instanceof FrameLayout) {
                        RadioButton radioButton = (RadioButton) ((FrameLayout) child).getChildAt(0);
                        if (radioButton.getId() != checkedId) {
                            radioButton.setChecked(false);
                        }
                    }
                }
            });

            registerButton.setOnClickListener(v -> register());

            return view;
        }

        private void register() {
            String petName = petNameInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            int selectedDogId = dogSelectionGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = dogSelectionGroup.findViewById(selectedDogId);
            String dogType = selectedRadioButton.getTag().toString();;
            if (petName.isEmpty() || username.isEmpty() || password.isEmpty() || selectedDogId == -1) {
                Toast.makeText(getContext(), "Please fill all fields and select a pet", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(getContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                    } else {
                        String email = username + "@petapp.com";

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(authResult -> {
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("username", username);
                                userData.put("email", email);

                                Map<String, Object> petData = new HashMap<>();
                                petData.put("name", petName);
                                petData.put("dogType", dogType);
                                petData.put("happiness", 100);
                                petData.put("hunger", 100);
                                petData.put("energy", 100);
                                petData.put("sleeping", false);

                                db.collection("users")
                                    .document(authResult.getUser().getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        db.collection("pets")
                                            .document(authResult.getUser().getUid())
                                            .set(petData)
                                            .addOnSuccessListener(aVoid2 -> {
                                                Pet pet = new Pet(petName,dogType,100,100,100);
                                                PetManager.getInstance().setPet(pet);
                                                Intent intent = new Intent(getActivity(), PetHomeActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            })
                                            .addOnFailureListener(e ->
                                                Toast.makeText(getContext(), "Error saving pet data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    })
                                    .addOnFailureListener(e ->
                                        Toast.makeText(getContext(), "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            })
                            .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e ->
                    Toast.makeText(getContext(), "Error checking username: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }