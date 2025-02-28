package com.developer_rahul.hackhound.Doctor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer_rahul.hackhound.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MyPatientActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<AppointmentDoctor> appointmentList;
    private DatabaseReference doctorsRef;
    private String loggedInUsername, doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patient);

        recyclerView = findViewById(R.id.recyclerViewPatients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList, this);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("LoginDoctorPrefs", MODE_PRIVATE);
        loggedInUsername = prefs.getString("usernameDoctor", "");

        if (loggedInUsername.isEmpty()) {
            Toast.makeText(this, "Doctor username not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors");

        // Fetch Doctor ID using username
        fetchDoctorId();
    }

    private void fetchDoctorId() {
        doctorsRef.orderByChild("Doctor_username").equalTo(loggedInUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                                doctorId = doctorSnapshot.child("Doctor_id").getValue(String.class);
                                if (doctorId != null) {
                                    fetchAppointments(doctorId);
                                } else {
                                    Toast.makeText(MyPatientActivity.this, "Doctor ID not found!", Toast.LENGTH_SHORT).show();
                                }
                                break; // No need to loop further
                            }
                        } else {
                            Toast.makeText(MyPatientActivity.this, "Doctor not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MyPatientActivity.this, "Error fetching doctor ID!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchAppointments(String doctorId) {
        DatabaseReference appointmentsRef = doctorsRef.child(doctorId).child("appointments");

        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    AppointmentDoctor appointment = appointmentSnapshot.getValue(AppointmentDoctor.class);
                    if (appointment != null) {
                        appointmentList.add(appointment);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyPatientActivity.this, "Failed to load appointments!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
