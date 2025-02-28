package com.developer_rahul.hackhound.Doctor;

import java.util.Map;

public class Report {
    private String patientName, bloodGroup, date, remark, doctorName, hospitalName;
    private Map<String, String> customFields;

    public Report() {
        // Default constructor required for Firebase
    }

    public Report(String patientName, String bloodGroup, String date, String remark, String doctorName, String hospitalName, Map<String, String> customFields) {
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.date = date;
        this.remark = remark;
        this.doctorName = doctorName;
        this.hospitalName = hospitalName;
        this.customFields = customFields;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getDate() {
        return date;
    }

    public String getRemark() {
        return remark;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }
}
