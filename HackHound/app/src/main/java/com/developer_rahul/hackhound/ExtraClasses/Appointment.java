package com.developer_rahul.hackhound.ExtraClasses;

public class Appointment {
    private String appointmentId;
    private String appointmentDate;
    private String appointmentTime;
    private String doctorName;
    private String hospitalName;
    private String patientName;
    private String patientAge;  // Store as String temporarily
    private String patientMobile;

    // Default constructor
    public Appointment() {
    }

    // Constructor with all parameters
    public Appointment(String appointmentId, String appointmentDate, String appointmentTime, String doctorName,
                       String hospitalName, String patientName, String patientAge, String patientMobile) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.doctorName = doctorName;
        this.hospitalName = hospitalName;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientMobile = patientMobile;
    }

    // Getters and setters for all the fields
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
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

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

    // Method to get patient age as an integer (if needed)
    public int getPatientAgeAsInt() {
        try {
            return Integer.parseInt(patientAge);
        } catch (NumberFormatException e) {
            return 0; // Return default value if parsing fails
        }
    }
}
