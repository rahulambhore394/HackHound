package com.developer_rahul.hackhound.User;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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

import java.util.Calendar;

public class PatientBookingActivity extends AppCompatActivity {

    private EditText etDoctorName, etHospitalName, etSelectDate, etSelectTime, etPatientName, etPatientAge, etPatientMobile;
    private Button btnSubmit;
    private DatabaseReference doctorsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_booking);

        etDoctorName = findViewById(R.id.et_doctor_name);
        etHospitalName = findViewById(R.id.et_hospital_name);
        etSelectDate = findViewById(R.id.et_select_date);
        etSelectTime = findViewById(R.id.et_select_time);
        etPatientName = findViewById(R.id.et_patient_name);
        etPatientAge = findViewById(R.id.et_patient_age);
        etPatientMobile = findViewById(R.id.et_patient_mobile);
        btnSubmit = findViewById(R.id.btn_submit);

        // Initialize Firebase Database reference
        doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors");

        // Set up date picker
        etSelectDate.setOnClickListener(view -> openDatePicker());

        // Set up time picker
        etSelectTime.setOnClickListener(view -> openTimePicker());

        // Handle submission
        btnSubmit.setOnClickListener(view -> submitAppointment());
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etSelectDate.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    etSelectTime.setText(time);
                },
                hour, minute,
                true // Use 24-hour format
        );

        timePickerDialog.show();
    }

    private void submitAppointment() {
        String doctorName = etDoctorName.getText().toString().trim();
        String hospitalName = etHospitalName.getText().toString().trim();
        String selectedDate = etSelectDate.getText().toString().trim();
        String selectedTime = etSelectTime.getText().toString().trim();
        String patientName = etPatientName.getText().toString().trim();
        String patientAge = etPatientAge.getText().toString().trim();
        String patientMobile = etPatientMobile.getText().toString().trim();

        if (doctorName.isEmpty() || hospitalName.isEmpty() || selectedDate.isEmpty() ||
                selectedTime.isEmpty() || patientName.isEmpty() || patientAge.isEmpty() || patientMobile.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query Firebase to find the matching doctor
        doctorsRef.orderByChild("Doctor_name").equalTo(doctorName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean doctorFound = false;

                        for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                            String dbHospitalName = doctorSnapshot.child("Hospital_name").getValue(String.class);

                            // Check if the hospital name matches as well
                            if (dbHospitalName != null && dbHospitalName.equals(hospitalName)) {
                                doctorFound = true;

                                // Generate a unique ID for the appointment
                                String appointmentId = doctorSnapshot.getRef().child("appointments").push().getKey();

                                // Create appointment data
                                DatabaseReference appointmentRef = doctorSnapshot.getRef().child("appointments").child(appointmentId);
                                appointmentRef.child("appointmentId").setValue(appointmentId);
                                appointmentRef.child("patientName").setValue(patientName);
                                appointmentRef.child("patientAge").setValue(patientAge);
                                appointmentRef.child("patientMobile").setValue(patientMobile);
                                appointmentRef.child("appointmentDate").setValue(selectedDate);
                                appointmentRef.child("appointmentTime").setValue(selectedTime);
                                appointmentRef.child("doctorName").setValue(doctorName);
                                appointmentRef.child("hospitalName").setValue(hospitalName);

                                Toast.makeText(PatientBookingActivity.this, "Appointment Submitted Successfully!", Toast.LENGTH_SHORT).show();
                                break; // Exit loop after storing data
                            }
                        }

                        if (!doctorFound) {
                            Toast.makeText(PatientBookingActivity.this, "Doctor not found in the specified hospital!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PatientBookingActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}