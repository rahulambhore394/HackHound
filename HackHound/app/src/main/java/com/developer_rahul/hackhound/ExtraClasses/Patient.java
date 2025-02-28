package com.developer_rahul.hackhound.ExtraClasses;

public class Patient {
    private String aadharNo;
    private String name;
    private String mobile;
    private String email;
    private String username;
    private String password;

    // Default constructor (Required for Firebase)
    public Patient() {}

    public Patient(String aadharNo, String name, String mobile, String email, String username, String password) {
        this.aadharNo = aadharNo;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getAadharNo() { return aadharNo; }
    public void setAadharNo(String aadharNo) { this.aadharNo = aadharNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
