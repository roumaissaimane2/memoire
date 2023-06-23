package com.example.driver;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1; // Any integer value
    private static final long INTERVAL = 120000; // Interval in milliseconds (2 minutes)

    private Button activateButton;
    private Button logoutButton;

    private Button deactivateButton;
    private DatabaseReference databaseReference;
    private boolean trackingActivated = false;
    FirebaseAuth firebaseAuth;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get references to buttons
        activateButton = findViewById(R.id.activateButton);
        deactivateButton = findViewById(R.id.deactivateButton);

        // Create FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Create location request
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);

        // Create location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    // Save driver's location to Firebase
                    databaseReference.child("driver").child("location").setValue(location);
                }
            }
        };
         firebaseAuth = FirebaseAuth.getInstance();

        // Get reference to logout button
         logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    private void logout() {
        firebaseAuth.signOut();
        // Redirect the user to the login screen or perform any other desired action
        // For example, you can start a new LoginActivity:
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish(); // Finish the current activity to prevent the user from going back to it
    }
    public void onActivateButtonClick(View view) {
        if (!trackingActivated) {
            trackingActivated = true;
            activateButton.setEnabled(false);
            deactivateButton.setEnabled(true);
            startTracking();
            Toast.makeText(this, "Tracking activated", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDeactivateButtonClick(View view) {
        if (trackingActivated) {
            trackingActivated = false;
            activateButton.setEnabled(true);
            deactivateButton.setEnabled(false);
            stopTracking();
            Toast.makeText(this, "Tracking deactivated", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start tracking
                startTracking();
            } else {
                // Permission denied, show a message or handle it accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startTracking() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }


    private void stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
