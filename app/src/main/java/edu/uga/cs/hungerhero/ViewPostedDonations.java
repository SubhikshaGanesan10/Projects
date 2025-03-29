package edu.uga.cs.hungerhero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPostedDonations extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewPostedDonationAdapter donationAdapter;
    private List<DonationItem> donationList;
    private TextView noDataTextView;

    private DatabaseReference donationRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_posted_donations);

        recyclerView = findViewById(R.id.viewPostedDonation_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noDataTextView = findViewById(R.id.noDataTextView);

        donationList = new ArrayList<>();
        donationAdapter = new ViewPostedDonationAdapter(this, donationList);
        recyclerView.setAdapter(donationAdapter);

        // Initialize Firebase reference and auth
        donationRef = FirebaseDatabase.getInstance().getReference("donations");
        firebaseAuth = FirebaseAuth.getInstance();

        // Retrieve donations from Firebase
        retrieveDonations();
    }

    private void retrieveDonations() {
        String currentUserId = firebaseAuth.getCurrentUser().getUid();

        // Create a query to retrieve donations with the current user's UID
        Query query = donationRef.orderByChild("userId").equalTo(currentUserId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonationItem donationItem = snapshot.getValue(DonationItem.class);
                    donationList.add(donationItem);
                }

                if (donationList.isEmpty()) {
                    noDataTextView.setVisibility(View.VISIBLE);
                } else {
                    noDataTextView.setVisibility(View.GONE);
                }

                donationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }
}