package edu.uga.cs.hungerhero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostDonations extends AppCompatActivity {

    private EditText etDonorName, etDonorAddress, etItemName, etItemDescription;
    private Button btnPostDonation;

    // Firebase
    private DatabaseReference donationRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_post_donations);

        // Initialize Firebase
        donationRef = FirebaseDatabase.getInstance().getReference("donations");
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        etDonorName = findViewById(R.id.etDonorName);
        etDonorAddress = findViewById(R.id.etDonorAddress);
        etItemName = findViewById(R.id.etItemName);
        etItemDescription = findViewById(R.id.etItemDescription);
        btnPostDonation = findViewById(R.id.btnPostDonation);

        // Set onClickListener for post donation button
        btnPostDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDonation();
            }
        });
    }

    // Method to post donation data to Firebase
    private void postDonation() {
        // Get values from EditText fields
        String donorName = etDonorName.getText().toString().trim();
        String donorAddress = etDonorAddress.getText().toString().trim();
        String itemName = etItemName.getText().toString().trim();
        String itemDescription = etItemDescription.getText().toString().trim();

        // Check if any of the strings are empty
        if (donorName.isEmpty() || donorAddress.isEmpty() || itemName.isEmpty() || itemDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user's UID
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Generate a unique ID for the donation item
        String donationId = donationRef.push().getKey();

        // Create new DonationItem object with user's UID and generated ID
        DonationItem donationItem = new DonationItem(donationId, userId, donorName, donorAddress, itemName, itemDescription);

        // Push donation data to Firebase Realtime Database
        donationRef.child(donationId).setValue(donationItem);

        // Clear EditText fields after posting donation
        etDonorName.setText("");
        etDonorAddress.setText("");
        etItemName.setText("");
        etItemDescription.setText("");

        // Display a toast message
        Toast.makeText(this, "Donation Posted", Toast.LENGTH_SHORT).show();

        // Navigate to the Donation Activity
        Intent intent = new Intent(this, Donation.class);
        startActivity(intent);
    }
}