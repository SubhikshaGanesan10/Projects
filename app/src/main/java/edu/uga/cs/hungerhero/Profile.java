package edu.uga.cs.hungerhero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    TextInputLayout fullName, email, phoneNo, password;
    TextView fullNameLabel, usernameLabel;
    Button deleteAccountButton, updateButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String fullNameFromDB;
    private String usernameFromDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phone_no_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.fullname_field);
        usernameLabel = findViewById(R.id.username_field);
        deleteAccountButton = findViewById(R.id.deleteAccount);
        updateButton = findViewById(R.id.updateButton);

        // Retrieve user data from the Firebase Realtime Database
        retrieveUserData();

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to delete the user account and associated data
                deleteUserAccountAndData();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        RelativeLayout donorRelativeLayout = findViewById(R.id.donor_relativeLayout);
        donorRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Donation activity
                Intent intent = new Intent(Profile.this, Donation.class);
                startActivity(intent);
            }
        });

        RelativeLayout receiverRelativeLayout = findViewById(R.id.receiver_relativeLayout);
        receiverRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Donation activity
                Intent intent = new Intent(Profile.this, Receive.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();
            DatabaseReference userRef = database.getReference("users").child(userUid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        fullNameFromDB = dataSnapshot.child("name").getValue(String.class);
                        usernameFromDB = dataSnapshot.child("username").getValue(String.class);
                        updateProfileFields();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });
        }
    }

    private void updateProfileFields() {
        fullNameLabel.setText(fullNameFromDB);
        usernameLabel.setText(usernameFromDB);
    }

    private void updateUserData() {
        String newFullName = fullName.getEditText().getText().toString().trim();
        String newEmail = email.getEditText().getText().toString().trim();
        String newPhoneNo = phoneNo.getEditText().getText().toString().trim();
        String newPassword = password.getEditText().getText().toString().trim();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();
            DatabaseReference userRef = database.getReference("users").child(userUid);

            // Create a map to store the updated user data
            Map<String, Object> updatedUserData = new HashMap<>();
            updatedUserData.put("name", newFullName);
            updatedUserData.put("email", newEmail);
            updatedUserData.put("phoneNo", newPhoneNo);
            updatedUserData.put("password", newPassword);

            // Update the user data in the Realtime Database
            userRef.updateChildren(updatedUserData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Profile.this, "User data updated successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Profile.this, "Failed to update user data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void deleteUserAccountAndData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            final String userUid = currentUser.getUid();
            deleteUserData(userUid);
        }
    }

    private void deleteUserData(String userUid) {
        DatabaseReference userToDeleteRef = database.getReference("users").child(userUid);
        userToDeleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // User data successfully deleted
                    Toast.makeText(Profile.this, "User data deleted successfully.", Toast.LENGTH_SHORT).show();
                    // Now, delete the user account
                    deleteUserAccount();
                } else {
                    // Handle the error
                    Toast.makeText(Profile.this, "Failed to delete user data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteUserAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Account removed successfully
                        Toast.makeText(Profile.this, "Account removed successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Profile.this, Login.class);
                        startActivity(intent);
                        finish(); // Close the current activity
                    } else {
                        // Failed to remove account
                        Toast.makeText(Profile.this, "Failed to remove account.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
