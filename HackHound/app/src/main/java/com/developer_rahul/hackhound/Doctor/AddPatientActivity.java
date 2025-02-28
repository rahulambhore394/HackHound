package com.developer_rahul.hackhound.Doctor;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.developer_rahul.hackhound.ExtraClasses.Patient;
import com.developer_rahul.hackhound.ExtraClasses.PatientAdapter;
import com.developer_rahul.hackhound.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AddPatientActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientAdapter patientAdapter;
    private List<Patient> patientList;
    private DatabaseReference databaseReference;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize patient list and adapter
        patientList = new ArrayList<>();
        patientAdapter = new PatientAdapter(patientList);
        recyclerView.setAdapter(patientAdapter);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Fetch all patients from Firebase
        fetchPatients();

        // Implement SearchView functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                patientAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                patientAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void fetchPatients() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    patientList.clear();
                    for (DataSnapshot patientSnapshot : snapshot.getChildren()) {
                        // Fetch all user data
                        String aadharNo = patientSnapshot.child("aadhar_no").getValue(String.class);
                        String name = patientSnapshot.child("name").getValue(String.class);
                        String mobile = patientSnapshot.child("mobile").getValue(String.class);
                        String email = patientSnapshot.child("email").getValue(String.class);
                        String username = patientSnapshot.child("username").getValue(String.class);
                        String password = patientSnapshot.child("password").getValue(String.class);

                        if (aadharNo != null && name != null && mobile != null && email != null && username != null && password != null) {
                            patientList.add(new Patient(aadharNo, name, mobile, email, username, password));
                        }
                    }
                    patientAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AddPatientActivity.this, "No users found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddPatientActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}