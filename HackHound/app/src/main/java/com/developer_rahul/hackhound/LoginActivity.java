package com.developer_rahul.hackhound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.developer_rahul.hackhound.Doctor.DoctorDashboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView textView;

    private DatabaseReference databaseReference;

    // SharedPreferences keys
    private static final String PREFS_NAME = "LoginPrefs";  // User's SharedPreferences
    private static final String KEY_USERNAME = "username";  // Key for storing user username
    private static final String PREFS_NAME_DOCTOR = "LoginDoctorPrefs";  // Doctor's SharedPreferences
    private static final String KEY_USERNAME_DOCTOR = "usernameDoctor";  // Key for storing doctor username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username_login);
        etPassword = findViewById(R.id.et_password_login);
        btnLogin = findViewById(R.id.btn_login_login);
        textView = findViewById(R.id.tv_new_user);

        // Navigate to RegistrationActivity
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Handle Login Button Click
        btnLogin.setOnClickListener(v -> loginUserOrDoctor());
    }

    private void loginUserOrDoctor() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check in users table
        databaseReference.child("users").orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String storedPassword = userSnapshot.child("password").getValue(String.class);
                                if (storedPassword != null && storedPassword.equals(password)) {
                                    // Login as user
                                    saveUserToSharedPreferences(username);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                            }
                        }

                        // If not found in users, check in doctors table
                        checkDoctorLogin(username, password);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Database Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkDoctorLogin(String usernameDoctor, String password) {
        databaseReference.child("Doctors").orderByChild("Doctor_username").equalTo(usernameDoctor)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                                String storedPassword = doctorSnapshot.child("Doctor_password").getValue(String.class);
                                if (storedPassword != null && storedPassword.equals(password)) {
                                    // Login as doctor
                                    saveDoctorToSharedPreferences(usernameDoctor);
                                    Intent intent = new Intent(LoginActivity.this, DoctorDashboard.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                            }
                        }

                        // If not found in either table
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Database Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Save user username to SharedPreferences
    private void saveUserToSharedPreferences(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);  // Store the username
        editor.apply();  // Commit changes
    }

    // Save doctor username to SharedPreferences
    private void saveDoctorToSharedPreferences(String usernameDoctor) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME_DOCTOR, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME_DOCTOR, usernameDoctor);  // Store doctor username
        editor.apply();  // Commit changes
    }

    // Retrieve username from SharedPreferences (for use in other activities)
    public static String getUsernameFromSharedPreferences(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(KEY_USERNAME, null); // Return null if username is not found
    }

    // Retrieve doctor username from SharedPreferences (for use in other activities)
    public static String getDoctorUsernameFromSharedPreferences(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(KEY_USERNAME_DOCTOR, null); // Return null if doctor username is not found
    }
}
