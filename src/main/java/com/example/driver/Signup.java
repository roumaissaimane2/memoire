package com.example.driver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.driver.Driver;
import com.example.driver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, driverIdEditText;
    private Button signupButton;

    private FirebaseAuth mAuth;
    private DatabaseReference driversReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        driversReference = FirebaseDatabase.getInstance().getReference("drivers");

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        driverIdEditText = findViewById(R.id.driverIdEditText);
        signupButton = findViewById(R.id.signupButton);
        TextView loginText = findViewById(R.id.Already_link);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupDriver();
            }
        });
    }
    private void goToLogin() {
        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);
    }
    private void signupDriver() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        final String driverId = driverIdEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(driverId)) {
            Toast.makeText(getApplicationContext(), "Enter driver ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String driverId = mAuth.getCurrentUser().getUid();
                            saveDriverToDatabase(driverId,email,password);
                            Toast.makeText(getApplicationContext(), "Signup successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveDriverToDatabase(String driverId ,String email, String password) {
        Driver driver = new Driver(driverId,email,password);
        driversReference.child(driverId).setValue(driver);
        driversReference.child(email).setValue(driver);
        driversReference.child(password).setValue(driver);
    }
}
