package com.developer_rahul.hackhound;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    ToggleButton toggleRegistration;
    ViewFlipper viewFlipper;

    // User Registration Fields
    EditText etName, etEmail, etUsername, etPassword, etAadhar, etMobile;
    Button btnRegisterUser;

    // Doctor Registration Fields
    EditText etDoctorId, etHospitalName, etDoctorName, etDoctorContact, etDoctorUsername, etDoctorPassword;
    Button btnRegisterDoctor;

    // Firebase Database Reference
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toggleRegistration = findViewById(R.id.toggle_registration);
        viewFlipper = findViewById(R.id.viewFlipper);

        // User Registration Fields
        etName = findViewById(R.id.et_name_register);
        etEmail = findViewById(R.id.et_email_register);
        etUsername = findViewById(R.id.et_username_register);
        etPassword = findViewById(R.id.et_password_register);
        etAadhar = findViewById(R.id.et_adhar_id_register);
        etMobile = findViewById(R.id.et_mobile_no_register);
        btnRegisterUser = findViewById(R.id.btn_register_user);

        // Doctor Registration Fields
        etDoctorId = findViewById(R.id.et_doctor_id);
        etDoctorName = findViewById(R.id.et_doctor_name);
        etHospitalName = findViewById(R.id.et_hospital_name);
        etDoctorContact = findViewById(R.id.et_doctor_contact);
        etDoctorUsername = findViewById(R.id.et_doctor_username);
        etDoctorPassword = findViewById(R.id.et_doctor_password);
        btnRegisterDoctor = findViewById(R.id.btn_register_hospital);

        // Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Toggle between User and Doctor registration forms
        toggleRegistration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewFlipper.setDisplayedChild(1); // Show Doctor Registration Layout
            } else {
                viewFlipper.setDisplayedChild(0); // Show User Registration Layout
            }
        });

        // Register User
        btnRegisterUser.setOnClickListener(v -> checkUserExistence());

        // Register Doctor
        btnRegisterDoctor.setOnClickListener(v -> checkDoctorExistence());
    }

    // Check if the user already exists in Firebase
    private void checkUserExistence() {
        String aadharNo = etAadhar.getText().toString().trim();

        if (TextUtils.isEmpty(aadharNo)) {
            Toast.makeText(this, "Aadhar No is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query to check if the user already exists
        Query userRef = databaseReference.child("users").orderByChild("aadhar_no").equalTo(aadharNo);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(RegistrationActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegistrationActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String aadharNo = etAadhar.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(aadharNo) || TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique numeric ID
        String userId = generateNumericId();

        // Store user data in Firebase
        DatabaseReference userRef = databaseReference.child("users").child(aadharNo);
        userRef.child("id").setValue(userId); // Store the unique numeric ID
        userRef.child("name").setValue(name);
        userRef.child("aadhar_no").setValue(aadharNo);
        userRef.child("email").setValue(email);
        userRef.child("username").setValue(username);
        userRef.child("mobile").setValue(mobile);
        userRef.child("password").setValue(password);

        Toast.makeText(this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }

    // Check if the doctor already exists in Firebase
    private void checkDoctorExistence() {
        String doctorId = etDoctorId.getText().toString().trim();

        if (TextUtils.isEmpty(doctorId)) {
            Toast.makeText(this, "Doctor ID is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query to check if the doctor already exists
        Query doctorRef = databaseReference.child("Doctors").orderByChild("Doctor_id").equalTo(doctorId);
        doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(RegistrationActivity.this, "Doctor already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    registerDoctor();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegistrationActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerDoctor() {
        String doctorName = etDoctorName.getText().toString().trim();
        String hospitalName = etHospitalName.getText().toString().trim();
        String doctorId = etDoctorId.getText().toString().trim();
        String doctorContact = etDoctorContact.getText().toString().trim();
        String doctorUsername = etDoctorUsername.getText().toString().trim();
        String doctorPassword = etDoctorPassword.getText().toString().trim();

        if (TextUtils.isEmpty(doctorName) || TextUtils.isEmpty(hospitalName) || TextUtils.isEmpty(doctorId) ||
                TextUtils.isEmpty(doctorContact) || TextUtils.isEmpty(doctorUsername) || TextUtils.isEmpty(doctorPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique numeric ID
        String uniqueDoctorId = generateNumericId();

        // Store doctor data in Firebase
        DatabaseReference doctorRef = databaseReference.child("Doctors").child(doctorId);
        doctorRef.child("id").setValue(uniqueDoctorId); // Store the unique numeric ID
        doctorRef.child("Doctor_name").setValue(doctorName);
        doctorRef.child("Hospital_name").setValue(hospitalName);
        doctorRef.child("Doctor_id").setValue(doctorId);
        doctorRef.child("Doctor_contact").setValue(doctorContact);
        doctorRef.child("Doctor_username").setValue(doctorUsername);
        doctorRef.child("Doctor_password").setValue(doctorPassword);

        Toast.makeText(this, "Doctor Registered Successfully!", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }
    private String generateNumericId() {
        int min = 100000; // Minimum 6-digit number
        int max = 999999; // Maximum 6-digit number
        return String.valueOf(min + (int) (Math.random() * ((max - min) + 1)));
    }

    // Navigate to LoginActivity
    private void navigateToLogin() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
