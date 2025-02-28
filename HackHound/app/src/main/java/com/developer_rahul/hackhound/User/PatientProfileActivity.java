package com.developer_rahul.hackhound.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.developer_rahul.hackhound.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfileActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";

    private EditText etName, etMobile, etEmail, etAdhar, etId; // Add etId
    private Button btnEditData;
    private DatabaseReference databaseReference;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // Initialize UI components
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etemail);
        etAdhar = findViewById(R.id.et_adhar);
        etId = findViewById(R.id.et_access); // Initialize etId
        btnEditData = findViewById(R.id.btnEditData);
        etId.setEnabled(false);

        // Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Get username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        username = sharedPreferences.getString(KEY_USERNAME, null);

        if (username != null) {
            fetchUserDataByUsername(username);
        } else {
            Toast.makeText(this, "Username not found in preferences!", Toast.LENGTH_SHORT).show();
        }

        // Edit/Update data click listener
        btnEditData.setOnClickListener(v -> {
            if (btnEditData.getText().toString().equals("Edit Data")) {
                enableEditing();
                btnEditData.setText("Update");
            } else {
                // If it's "Update", submit the changes
                if (validateFields()) {
                    updateUserData();
                }
            }
        });
    }

    private void fetchUserDataByUsername(String username) {
        // Query the database to find the user with the matching username
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Retrieve user data from Firebase and populate the fields
                        String name = userSnapshot.child("name").getValue(String.class);
                        String mobile = userSnapshot.child("mobile").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        String adhar = userSnapshot.child("aadhar_no").getValue(String.class);
                        String id = userSnapshot.child("id").getValue(String.class); // Fetch the ID

                        etName.setText(name);
                        etMobile.setText(mobile);
                        etEmail.setText(email);
                        etAdhar.setText(adhar);
                        etId.setText(id);
                        disableEditing();
                    }
                } else {
                    Toast.makeText(PatientProfileActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PatientProfileActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableEditing() {
        // Enable the EditText fields for editing
        etName.setEnabled(true);
        etMobile.setEnabled(true);
        etEmail.setEnabled(true);
        etAdhar.setEnabled(true);
    }

    private void disableEditing() {
        // Disable the EditText fields after editing
        etName.setEnabled(false);
        etMobile.setEnabled(false);
        etEmail.setEnabled(false);
        etAdhar.setEnabled(false);
    }

    private boolean validateFields() {
        // Validate that none of the fields are empty
        if (etName.getText().toString().isEmpty() ||
                etMobile.getText().toString().isEmpty() ||
                etEmail.getText().toString().isEmpty() ||
                etAdhar.getText().toString().isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateUserData() {
        // Get updated data from the EditText fields
        String name = etName.getText().toString();
        String mobile = etMobile.getText().toString();
        String email = etEmail.getText().toString();
        String adhar = etAdhar.getText().toString();

        // Query the database to find the user with the matching username
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Update the user data in Firebase
                        userSnapshot.getRef().child("name").setValue(name);
                        userSnapshot.getRef().child("mobile").setValue(mobile);
                        userSnapshot.getRef().child("email").setValue(email);
                        userSnapshot.getRef().child("aadhar_no").setValue(adhar).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(PatientProfileActivity.this, "Data updated successfully.", Toast.LENGTH_SHORT).show();
                                // After update, disable editing and change button text back to "Edit"
                                disableEditing();
                                btnEditData.setText("Edit Data");
                            } else {
                                Toast.makeText(PatientProfileActivity.this, "Failed to update data.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(PatientProfileActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PatientProfileActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}