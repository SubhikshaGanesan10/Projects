package edu.uga.cs.hungerhero;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Receive extends AppCompatActivity {

    private Button btnViewDonations, btnViewAcceptedDonations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_receive);

        // Initialize button
        btnViewDonations = findViewById(R.id.btnViewDonations);
        btnViewAcceptedDonations = findViewById(R.id.btnViewAcceptedDonations);

        // Set OnClickListener for View Donations button
        btnViewDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewDonations activity
                startActivity(new Intent(Receive.this, ViewDonations.class));
            }
        });

        // Set OnClickListener for View Accepted Donations button
        btnViewAcceptedDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewDonations activity
                startActivity(new Intent(Receive.this, ViewAcceptedDonations.class));
            }
        });
    }
}
