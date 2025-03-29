package edu.uga.cs.hungerhero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextInputLayout email, password;
    Button registerButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        registerButton = findViewById(R.id.registerButton);
        resetButton = findViewById(R.id.forgetPassword);

        // Set an OnClickListener on the registerButton
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the button is clicked, navigate to the Register activity
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        // Set an OnClickListener on the Reset Button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the button is clicked, navigate to the Register activity
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, navigate to Dashboard
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish(); // Finish the activity to prevent returning to login page
        }
    }

    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid Email Address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        // Validate Login Info
        if (!validateEmail() || !validatePassword()) {
            return;
        }

        // Get user credentials
        String userEmail = email.getEditText().getText().toString().trim();
        String userPassword = password.getEditText().getText().toString().trim(); // Initialize user password variable

        // Authenticate user with Firebase Authentication
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Authentication success, navigate to Dashboard
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                    finish(); // Prevent returning to login page
                } else {
                    // Authentication failed, display error message
                    Toast.makeText(Login.this, "Authentication failed. Invalid credentials.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
                Log.e("LoginActivity", "Authentication failed: " + e.getMessage());
                Toast.makeText(Login.this, "Authentication failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
