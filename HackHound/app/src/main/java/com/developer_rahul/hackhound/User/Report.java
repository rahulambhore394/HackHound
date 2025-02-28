package com.developer_rahul.hackhound.User;

public class Report {
    private String bloodGroup;
    private String date;
    private String doctorName;
    private String hospitalName;
    private String patientName;
    private String remark;

    // Default constructor for Firebase
    public Report() {}

    // Parameterized constructor
    public Report(String bloodGroup, String date, String doctorName, String hospitalName,
                  String patientName, String remark) {
        this.bloodGroup = bloodGroup;
        this.date = date;
        this.doctorName = doctorName;
        this.hospitalName = hospitalName;
        this.patientName = patientName;
        this.remark = remark;
    }

    // Getters and Setters
    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}