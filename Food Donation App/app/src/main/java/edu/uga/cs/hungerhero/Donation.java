package edu.uga.cs.hungerhero;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Donation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_donation);

        Button btnPostDonation = findViewById(R.id.btnPostDonation);
        Button btnViewDonations = findViewById(R.id.btnViewDonations);
        Button btnViewAcceptedDonations = findViewById(R.id.btnAcceptedDonations);

        btnPostDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity for posting donations
                Intent intent = new Intent(Donation.this, PostDonations.class);
                startActivity(intent);
            }
        });

        btnViewDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity for posting donations
                Intent intent = new Intent(Donation.this, ViewPostedDonations.class);
                startActivity(intent);
            }
        });

        btnViewAcceptedDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity for posting donations
                Intent intent = new Intent(Donation.this, ViewReceiverAcceptedDonations.class);
                startActivity(intent);
            }
        });

        Button btnViewChats = findViewById(R.id.btnViewChats);
        btnViewChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity for viewing chats
                Intent intent = new Intent(Donation.this, Chat.class);
                startActivity(intent);
            }
        });
    }
}
