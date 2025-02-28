package com.developer_rahul.hackhound.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.developer_rahul.hackhound.R;
import com.developer_rahul.hackhound.User.Report;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private List<Report> reportList;
    private DatabaseReference userRef, reportsRef;
    private String username, aadharNo;

    private static final String PREFS_NAME = "LoginPrefs"; // SharedPreferences name
    private static final String KEY_USERNAME = "username"; // Key for storing username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(reportList);
        recyclerView.setAdapter(reportAdapter);

        // Get the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(KEY_USERNAME, "");

        if (username.isEmpty()) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            userRef = FirebaseDatabase.getInstance().getReference("users").child(username);
            fetchAadharNo();
        }
    }


    private void fetchAadharNo() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Fetch all users to find the matching username
        usersRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userFound = false;

                // Iterate over all users in the 'users' node
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Get the current username (key of each child node)
                    String currentUsername = userSnapshot.getKey();

                    // If the current username matches the username from SharedPreferences
                    if (currentUsername != null && currentUsername.equals(username)) {
                        // Get the aadhar_no for this user
                        aadharNo = userSnapshot.child("aadhar_no").getValue(String.class);

                        // If we have a valid aadhar_no, proceed to fetch reports
                        if (aadharNo != null && !aadharNo.isEmpty()) {
                            // Get the reports for the given aadhar_no
                            reportsRef = FirebaseDatabase.getInstance().getReference("PatientDetails")
                                    .child("Reports").child(aadharNo);
                            loadReports();
                            userFound = true;
                            break; // Exit the loop after finding the user
                        }
                    }
                }

                // If the username was not found, show a message
                if (!userFound) {
                    Toast.makeText(HistoryActivity.this, "User not found or Aadhar number not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void loadReports() {
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();

                // Iterate over all the reports
                for (DataSnapshot reportSnapshot : snapshot.getChildren()) {
                    // Convert each report snapshot into a Report object
                    Report report = reportSnapshot.getValue(Report.class);

                    if (report != null) {
                        reportList.add(report);
                    }
                }

                // If no reports found, show a message
                if (reportList.isEmpty()) {
                    Toast.makeText(HistoryActivity.this, "No reports found", Toast.LENGTH_SHORT).show();
                } else {
                    // Notify the adapter to refresh the list
                    reportAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to load reports", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
