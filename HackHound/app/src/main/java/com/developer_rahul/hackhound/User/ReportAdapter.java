package com.developer_rahul.hackhound.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer_rahul.hackhound.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report1, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);

        holder.textViewBloodGroup.setText("Blood Group: " + report.getBloodGroup());
        holder.textViewDate.setText("Date: " + report.getDate());
        holder.textViewDoctorName.setText("Doctor Name: " + report.getDoctorName());
        holder.textViewHospitalName.setText("Hospital Name: " + report.getHospitalName());
        holder.textViewPatientName.setText("Patient Name: " + report.getPatientName());
        holder.textViewRemark.setText("Remark: " + report.getRemark());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBloodGroup, textViewDate, textViewDoctorName, textViewHospitalName, textViewPatientName, textViewRemark;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewBloodGroup = itemView.findViewById(R.id.textViewBloodGroup);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDoctorName = itemView.findViewById(R.id.textViewDoctorName);
            textViewHospitalName = itemView.findViewById(R.id.textViewHospitalName);
            textViewPatientName = itemView.findViewById(R.id.textViewPatientName);
            textViewRemark = itemView.findViewById(R.id.textViewRemark);
        }
    }
}
