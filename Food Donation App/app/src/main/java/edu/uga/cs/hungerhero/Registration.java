package edu.uga.cs.hungerhero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    TextInputLayout regName, regUsername, regEmail, regNumber, regPassword;
    DatabaseReference usersReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        Button loginButton = findViewById(R.id.backToLogin);

        // Initialize Firebase Authentication and Database reference
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("users");

        regName = findViewById(R.id.registerFullName);
        regUsername = findViewById(R.id.registerUsername);
        regEmail = findViewById(R.id.registerEmail);
        regNumber = findViewById(R.id.phoneNumber);
        regPassword = findViewById(R.id.loginPassword);

        // Set an OnClickListener on the back to login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the button is clicked, navigate to the Login activity
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, redirect to Dashboard activity
            Intent intent = new Intent(Registration.this, Dashboard.class);
            startActivity(intent);
            finish(); // Optional: Finish the current activity to prevent going back to it
        }
    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();
        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Username is too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("Username cannot have spaces");
            return false;
        } else {
            regUsername.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid Email Address");
            return false;
        } else {
            regEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String val = regNumber.getEditText().getText().toString();
        if (val.isEmpty()) {
            regNumber.setError("Field cannot be empty");
            return false;
        } else {
            regNumber.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            return true;
        }
    }

    public void registerUser(View view) {
        if (!validateName() || !validateEmail() || !validateUsername() || !validatePassword() || !validatePhoneNumber()) {
            return;
        }
        final String name = regName.getEditText().getText().toString();
        final String username = regUsername.getEditText().getText().toString();
        final String email = regEmail.getEditText().getText().toString();
        final String phoneNumber = regNumber.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        // Store additional user information in Firebase Realtime Database
                        String userId = user.getUid();
                        HelperClass newUser = new HelperClass(name, username, email, phoneNumber, password);
                        usersReference.child(userId).setValue(newUser);

                        // Navigate to the Dashboard activity
                        Intent intent = new Intent(Registration.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(Registration.this, "Registration failed: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
