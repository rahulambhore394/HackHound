package com.developer_rahul.hackhound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer_rahul.hackhound.User.AlertActivity;
import com.developer_rahul.hackhound.User.HistoryActivity;
import com.developer_rahul.hackhound.User.PatientBookingActivity;
import com.developer_rahul.hackhound.User.PatientProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextView tvWelcome, tvEmail, tvMobile;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;

    // CardViews
    CardView card1, card2, card3, card4;
    Toolbar toolbar;  // Declare the Toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up window insets to avoid overlapping with system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize CardViews
        card1 = findViewById(R.id.card_history);
        card2 = findViewById(R.id.card_book_appointment);
        card3 = findViewById(R.id.card_nominee);
        card4 = findViewById(R.id.card_alerts);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");
        String profileImageUri = sharedPreferences.getString("profileImageUri", ""); // URL or path to the image

        TextView usernameTextView = findViewById(R.id.username);
        ImageView profileImageView = findViewById(R.id.profile_image);

        usernameTextView.setText(username);

        // Set OnClickListener for Card 1 (History)
        card1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for Card 2 (Book Appointment)
        card2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PatientBookingActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for Card 4 (Alerts)
        card4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlertActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for the Toolbar to open ProfileActivity
        toolbar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PatientProfileActivity.class);
            startActivity(intent);
        });
    }
}
