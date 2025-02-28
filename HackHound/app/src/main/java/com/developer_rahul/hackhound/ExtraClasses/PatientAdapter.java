package com.developer_rahul.hackhound.ExtraClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.developer_rahul.hackhound.R;
import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> implements Filterable {

    private List<Patient> patientList;
    private List<Patient> patientListFull; // For filtering

    public PatientAdapter(List<Patient> patientList) {
        this.patientList = patientList;
        this.patientListFull = new ArrayList<>(patientList); // Initialize full list
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.textViewName.setText(patient.getName());
        holder.textViewMobile.setText(patient.getMobile());
        holder.textViewAadhar.setText(patient.getAadharNo());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    @Override
    public Filter getFilter() {
        return patientFilter;
    }

    private final Filter patientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Patient> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(patientListFull); // Show full list if no search query
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Patient patient : patientListFull) {
                    if (patient.getName().toLowerCase().contains(filterPattern) ||
                            patient.getMobile().toLowerCase().contains(filterPattern) ||
                            patient.getAadharNo().toLowerCase().contains(filterPattern)) {
                        filteredList.add(patient);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            patientList.clear();
            patientList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewMobile, textViewAadhar;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewMobile = itemView.findViewById(R.id.textViewMobile);
            textViewAadhar = itemView.findViewById(R.id.textViewAadhar);
        }
    }
}