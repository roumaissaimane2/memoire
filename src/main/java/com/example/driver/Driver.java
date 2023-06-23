package com.example.driver;
public class Driver {
    private  String email;
    private  String password;
    private String driverId;
    private double latitude;
    private double longitude;
    // Other driver information fields

    public Driver() {
        // Default constructor required for Firebase
    }

    public Driver(String driverId, String email, String password) {
        this.driverId = driverId;
        this.email = email;
        this.password = password;
    }

        // Assign other driver information fields


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
// Getters and setters for the driver's information fields
}