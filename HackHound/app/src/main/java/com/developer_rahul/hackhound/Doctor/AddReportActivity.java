package com.developer_rahul.hackhound.Doctor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developer_rahul.hackhound.MainActivity;
import com.developer_rahul.hackhound.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddReportActivity extends AppCompatActivity {

    private LinearLayout container;
    private Button btnAddField, btnSubmit;
    private EditText editPatientName, editBloodGroup, editDate, editRemark;
    private HashMap<String, String> customFields; // Store field names and data

    private String aadharNo, doctorUsername, doctorName, hospitalName; // Variables for fetched data
    private DatabaseReference doctorRef, patientRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        container = findViewById(R.id.container);
        btnAddField = findViewById(R.id.btn_add_field);
        btnSubmit = findViewById(R.id.btn_submit);
        editPatientName = findViewById(R.id.edit_patient_name);
        editBloodGroup = findViewById(R.id.edit_blood_group);
        editDate = findViewById(R.id.edit_date);
        editRemark = findViewById(R.id.edit_remark);

        customFields = new HashMap<>();

        // Get Aadhar number from Intent
        aadharNo = getIntent().getStringExtra("aadhar_no");

        // Get Doctor Username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginDoctorPrefs", Context.MODE_PRIVATE);
        doctorUsername = sharedPreferences.getString("usernameDoctor", "");

        Toast.makeText(this, doctorUsername, Toast.LENGTH_SHORT).show();

        if (doctorUsername.isEmpty()) {
            Toast.makeText(this, "Doctor not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch Doctor Name and Hospital Name
        doctorRef = FirebaseDatabase.getInstance().getReference("Doctors").child(doctorUsername);
        fetchDoctorDetails();

        btnAddField.setOnClickListener(v -> openAddFieldDialog());
        btnSubmit.setOnClickListener(v -> collectAndSubmitData());
    }

    private void fetchDoctorDetails() {
        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors");

        doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean doctorFound = false;
                for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                    String storedUsername = doctorSnapshot.child("Doctor_username").getValue(String.class);

                    if (storedUsername != null && storedUsername.equals(doctorUsername)) {
                        doctorName = doctorSnapshot.child("Doctor_name").getValue(String.class);
                        hospitalName = doctorSnapshot.child("Hospital_name").getValue(String.class);
                        doctorFound = true;
                        break; // Exit loop after finding the doctor
                    }
                }

                if (!doctorFound) {
                    Toast.makeText(AddReportActivity.this, "Doctor details not found!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AddReportActivity.this, "Failed to load doctor details", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openAddFieldDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_field);

        EditText editFieldName = dialog.findViewById(R.id.edit_field_name);
        EditText editFieldData = dialog.findViewById(R.id.edit_field_data);
        Button btnSaveField = dialog.findViewById(R.id.btn_save_field);

        btnSaveField.setOnClickListener(v -> {
            String fieldName = editFieldName.getText().toString().trim();
            String fieldData = editFieldData.getText().toString().trim();

            if (fieldName.isEmpty()) {
                Toast.makeText(this, "Field name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            addCustomField(fieldName, fieldData);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addCustomField(String fieldName, String fieldData) {
        TextView textView = new TextView(this);
        textView.setText(fieldName);
        textView.setTextSize(16);
        textView.setPadding(0, 16, 0, 4);
        container.addView(textView, container.indexOfChild(btnAddField));

        EditText editText = new EditText(this);
        editText.setHint("Enter " + fieldName);
        editText.setText(fieldData);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        container.addView(editText, container.indexOfChild(btnAddField));

        customFields.put(fieldName, fieldData);

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                customFields.put(fieldName, editText.getText().toString());
            }
        });

        if (btnSubmit.getVisibility() == View.GONE) {
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void collectAndSubmitData() {
        String patientName = editPatientName.getText().toString().trim();
        String bloodGroup = editBloodGroup.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String remark = editRemark.getText().toString().trim();

        if (patientName.isEmpty() || bloodGroup.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> reportData = new HashMap<>();
        reportData.put("patientName", patientName);
        reportData.put("bloodGroup", bloodGroup);
        reportData.put("date", date);
        reportData.put("remark", remark);
        reportData.put("doctorName", doctorName);
        reportData.put("hospitalName", hospitalName);
        reportData.put("customFields", customFields);  // Store additional fields

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference patientRef = databaseReference.child("PatientDetails").child("Reports").child(aadharNo);

        // Directly set the report details without using a unique ID
        patientRef.setValue(reportData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Report submitted successfully", Toast.LENGTH_SHORT).show();

                    // Clear fields
                    clearAllFields();

                    // Navigate to MyPatientActivity
                    Intent intent = new Intent(AddReportActivity.this, MyPatientActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to submit data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearAllFields() {
        editPatientName.setText("");
        editBloodGroup.setText("");
        editDate.setText("");
        editRemark.setText("");
        container.removeAllViews();
        customFields.clear();
        btnSubmit.setVisibility(View.GONE);
    }
}
