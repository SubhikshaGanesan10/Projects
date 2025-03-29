package edu.uga.cs.hungerhero;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewReceiverAcceptedDonations extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewReceiverAcceptedDonationsAdapter adapter;
    private List<AcceptedDonationItem> acceptedDonationList;
    private TextView noDataTextView;

    private DatabaseReference donorAcceptedDonationsRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_receiver_accepted_donations);

        recyclerView = findViewById(R.id.viewReceiverAccepted_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noDataTextView = findViewById(R.id.noDataTextView);

        acceptedDonationList = new ArrayList<>();
        adapter = new ViewReceiverAcceptedDonationsAdapter(this, acceptedDonationList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase reference and auth
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
        donorAcceptedDonationsRef = FirebaseDatabase.getInstance().getReference("donor's accepted donations list");

        // Retrieve accepted donations from Firebase
        retrieveAcceptedDonations();
    }

    private void retrieveAcceptedDonations() {
        Query query = donorAcceptedDonationsRef.orderByChild("donorUserId").equalTo(currentUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                acceptedDonationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AcceptedDonationItem acceptedDonationItem = snapshot.getValue(AcceptedDonationItem.class);
                    acceptedDonationList.add(acceptedDonationItem);
                }

                if (acceptedDonationList.isEmpty()) {
                    noDataTextView.setVisibility(View.VISIBLE);
                } else {
                    noDataTextView.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }
}