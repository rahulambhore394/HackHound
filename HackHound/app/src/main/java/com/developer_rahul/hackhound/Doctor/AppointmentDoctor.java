package com.developer_rahul.hackhound.Doctor;

public class AppointmentDoctor {
    private String appointmentId;
    private String patientName;
    private String patientAge;
    private String patientMobile;
    private String appointmentDate;
    private String appointmentTime;
    private String doctorName;
    private String hospitalName;

    public AppointmentDoctor() {
        // Default constructor required for Firebase
    }

    public AppointmentDoctor(String appointmentId, String patientName, String patientAge, String patientMobile,
                       String appointmentDate, String appointmentTime, String doctorName, String hospitalName) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientMobile = patientMobile;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.doctorName = doctorName;
        this.hospitalName = hospitalName;
    }

    public String getAppointmentId() { return appointmentId; }
    public String getPatientName() { return patientName; }
    public String getPatientAge() { return patientAge; }
    public String getPatientMobile() { return patientMobile; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getDoctorName() { return doctorName; }
    public String getHospitalName() { return hospitalName; }
}
