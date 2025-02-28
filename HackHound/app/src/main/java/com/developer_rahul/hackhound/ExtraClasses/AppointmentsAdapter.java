package com.developer_rahul.hackhound.ExtraClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.developer_rahul.hackhound.R;
import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;

    // Constructor
    public AppointmentsAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout for each appointment item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.tvAppointmentDate.setText("Date: " + appointment.getAppointmentDate());
        holder.tvAppointmentTime.setText("Time: " + appointment.getAppointmentTime());
        holder.tvPatientName.setText("Patient Name: " + appointment.getPatientName());
        holder.tvDoctorName.setText("Doctor Name: " + appointment.getDoctorName());
        holder.tvHospitalName.setText("Hospital: " + appointment.getHospitalName());
        holder.tvPatientAge.setText("Age: " + appointment.getPatientAgeAsInt()); // Using the method to get age as int
        holder.tvPatientMobile.setText("Mobile: " + appointment.getPatientMobile());
    }


    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    // ViewHolder class to hold each appointment's UI components
    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppointmentDate, tvAppointmentTime, tvPatientName, tvDoctorName, tvHospitalName, tvPatientAge, tvPatientMobile;

        public AppointmentViewHolder(View itemView) {
            super(itemView);
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvPatientAge = itemView.findViewById(R.id.tvPatientAge);
            tvPatientMobile = itemView.findViewById(R.id.tvPatientMobile);
        }
    }
}
