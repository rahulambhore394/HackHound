package com.developer_rahul.hackhound.Doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer_rahul.hackhound.ExtraClasses.Appointment;
import com.developer_rahul.hackhound.ExtraClasses.AppointmentsAdapter;
import com.developer_rahul.hackhound.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorDashboard extends AppCompatActivity {

    private RecyclerView appointmentsRecyclerView;
    private AppointmentsAdapter appointmentsAdapter;
    private List<Appointment> appointmentList;
    private DatabaseReference databaseReference;
    Button btn_my_patient;

    // SharedPreferences keys
    private static final String PREFS_NAME_DOCTOR = "LoginDoctorPrefs";
    private static final String KEY_USERNAME_DOCTOR = "usernameDoctor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        // Initialize views
        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);

        btn_my_patient = findViewById(R.id.btn_my_patients);
        appointmentList = new ArrayList<>();
        appointmentsAdapter = new AppointmentsAdapter(appointmentList);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentsRecyclerView.setAdapter(appointmentsAdapter);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Doctors");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME_DOCTOR, MODE_PRIVATE);
        String doctorUsername = sharedPreferences.getString(KEY_USERNAME_DOCTOR, null);

        if (doctorUsername != null) {
            getSupportActionBar().setTitle("Welcome, " + doctorUsername);
            fetchAppointmentsForDoctor(doctorUsername);
        } else {
            Toast.makeText(this, "Doctor not logged in", Toast.LENGTH_SHORT).show();
        }

        btn_my_patient.setOnClickListener(v -> {
            Intent i = new Intent(DoctorDashboard.this, MyPatientActivity.class);
            startActivity(i);
        });

       }

    private void fetchAppointmentsForDoctor(String doctorUsername) {
        // Query to get appointments for the doctor using the stored username
        databaseReference.orderByChild("Doctor_username").equalTo(doctorUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                                DataSnapshot appointmentsSnapshot = doctorSnapshot.child("appointments");
                                for (DataSnapshot appointmentSnapshot : appointmentsSnapshot.getChildren()) {
                                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                                    if (appointment != null) {
                                        // Add the appointment to the list
                                        appointmentList.add(appointment);
                                    }
                                }
                                appointmentsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(DoctorDashboard.this, "No appointments found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DoctorDashboard.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}