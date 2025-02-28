package com.developer_rahul.hackhound.Doctor;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.developer_rahul.hackhound.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ViewReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private List<Report> reportList;
    private DatabaseReference reportsRef;
    private String aadharNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        recyclerView = findViewById(R.id.recyclerViewReports);
        reportList = new ArrayList<>();
        adapter = new ReportAdapter(reportList);
        recyclerView.setAdapter(adapter);

        aadharNo = getIntent().getStringExtra("aadhar_no");
        reportsRef = FirebaseDatabase.getInstance().getReference("PatientDetails").child(aadharNo).child("Reports");

        loadReports();
    }

    private void loadReports() {
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Report report = data.getValue(Report.class);
                    if (report != null) {
                        reportList.add(report);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewReportActivity.this, "Failed to load reports", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
