package edu.uga.cs.hungerhero;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Find views
        EditText emailBox = findViewById(R.id.verification_code_entered_by_user);
        Button resetButton = findViewById(R.id.verify_btn);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        // Set onClickListener for the RESET PASSWORD button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailBox.getText().toString().trim();

                // Validate email
                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    emailBox.setError("Enter a valid email address");
                    emailBox.requestFocus();
                    return;
                }

                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Send password reset email
                mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        // Password reset email sent successfully
                        Toast.makeText(ForgetPassword.this, "Check your email for password reset instructions", Toast.LENGTH_SHORT).show();
                    } else {
                        // Password reset email sending failed
                        Toast.makeText(ForgetPassword.this, "Failed to send password reset email. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
