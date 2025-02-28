package com.developer_rahul.hackhound.Doctor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.developer_rahul.hackhound.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<AppointmentDoctor> appointmentList;
    private Context context;

    public AppointmentAdapter(List<AppointmentDoctor> appointmentList, Context context) {
        this.appointmentList = appointmentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentDoctor appointment = appointmentList.get(position);

        holder.tvPatientName.setText("Patient: " + appointment.getPatientName());
        holder.tvDate.setText("Date: " + appointment.getAppointmentDate());
        holder.tvTime.setText("Time: " + appointment.getAppointmentTime());
        holder.tvContact.setText("Mobile No: " + appointment.getPatientMobile());

        // Item click listener for showing dialog
        holder.itemView.setOnClickListener(v -> showAppointmentOptionsDialog(appointment));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    private void showAppointmentOptionsDialog(AppointmentDoctor appointment) {
        new AlertDialog.Builder(context)
                .setTitle("Appointment Details")
                .setMessage("Patient: " + appointment.getPatientName() + "\n" +
                        "Date: " + appointment.getAppointmentDate() + "\n" +
                        "Time: " + appointment.getAppointmentTime())
                .setPositiveButton("Add Report", (dialog, which) -> showValidationDialog(appointment, "add"))
                .setNegativeButton("View Report", (dialog, which) -> showValidationDialog(appointment, "view"))
                .setNeutralButton("Send Alerts", (dialog, which) -> {
                    Intent intent = new Intent(context, SendAlertsActivity.class);
                    intent.putExtra("appointmentId", appointment.getAppointmentId());
                    context.startActivity(intent);
                })
                .show();
    }

    private void showValidationDialog(AppointmentDoctor appointment, String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Verify Aadhar & Token");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_validate, null);
        EditText edtAadhar = view.findViewById(R.id.edtAadhar);
        EditText edtToken = view.findViewById(R.id.edtToken);
        Button btnValidate = view.findViewById(R.id.btnValidate);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        btnValidate.setOnClickListener(v -> {
            String aadhar = edtAadhar.getText().toString().trim();
            String token = edtToken.getText().toString().trim();

            if (aadhar.isEmpty() || token.isEmpty()) {
                Toast.makeText(context, "Please enter both Aadhar and Token", Toast.LENGTH_SHORT).show();
                return;
            }

            validateAadharAndToken(aadhar, token, dialog, appointment, action);
        });
    }

    private void validateAadharAndToken(String aadhar, String token, AlertDialog dialog, AppointmentDoctor appointment, String action) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.child(aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String storedToken = snapshot.child("id").getValue(String.class);

                    if (storedToken != null && storedToken.equals(token)) {
                        Toast.makeText(context, "Validated Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        if (action.equals("add")) {
                            //generate the random 6 digit number and replace in the id
                            Intent intent = new Intent(context, AddReportActivity.class);
                            intent.putExtra("appointmentId", appointment.getAppointmentId());
                            intent.putExtra("aadhar_no", aadhar);
                            context.startActivity(intent);
                        } else if (action.equals("view")) {
                            //generate the random  digit number and replace in id
                            Intent intent = new Intent(context, ViewReportActivity.class);
                            intent.putExtra("appointmentId", appointment.getAppointmentId());
                            intent.putExtra("aadhar_no", aadhar);  // Pass the Aadhar number for validation in the report view
                            context.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context, "Invalid Token", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Aadhar not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvDate, tvTime, tvContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContact = itemView.findViewById(R.id.tvMobile);
        }
    }
}
