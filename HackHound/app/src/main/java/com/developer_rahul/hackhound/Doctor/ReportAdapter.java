package com.developer_rahul.hackhound.Doctor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.developer_rahul.hackhound.R;
import java.util.List;
import java.util.Map;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.textPatientName.setText("Patient: " + report.getPatientName());
        holder.textBloodGroup.setText("Blood Group: " + report.getBloodGroup());
        holder.textDate.setText("Date: " + report.getDate());
        holder.textRemark.setText("Remark: " + report.getRemark());
        holder.textDoctor.setText("Doctor: " + report.getDoctorName());
        holder.textHospital.setText("Hospital: " + report.getHospitalName());

        // Display custom fields
        StringBuilder customFieldsText = new StringBuilder();
        if (report.getCustomFields() != null) {
            for (Map.Entry<String, String> entry : report.getCustomFields().entrySet()) {
                customFieldsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        holder.textCustomFields.setText(customFieldsText.toString());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView textPatientName, textBloodGroup, textDate, textRemark, textDoctor, textHospital, textCustomFields;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            textPatientName = itemView.findViewById(R.id.textPatientName);
            textBloodGroup = itemView.findViewById(R.id.textBloodGroup);
            textDate = itemView.findViewById(R.id.textDate);
            textRemark = itemView.findViewById(R.id.textRemark);
            textDoctor = itemView.findViewById(R.id.textDoctor);
            textHospital = itemView.findViewById(R.id.textHospital);
            textCustomFields = itemView.findViewById(R.id.textCustomFields);
        }
    }
}
