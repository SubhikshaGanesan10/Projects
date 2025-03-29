package edu.uga.cs.hungerhero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewDonations extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewDonationAdapter adapter;
    private List<DonationItem> donationList;
    private DatabaseReference donationRef;
    private TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_donations);

        recyclerView = findViewById(R.id.viewDonation_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noDataTextView = findViewById(R.id.noDataTextView);

        donationList = new ArrayList<>();
        adapter = new ViewDonationAdapter(this, donationList);
        recyclerView.setAdapter(adapter);

        donationRef = FirebaseDatabase.getInstance().getReference("donations");

        retrieveDonations();
    }

    private void retrieveDonations() {
        Query query = donationRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonationItem donationItem = dataSnapshot.getValue(DonationItem.class);
                    donationList.add(donationItem);
                }

                if (donationList.isEmpty()) {
                    noDataTextView.setVisibility(View.VISIBLE);
                } else {
                    noDataTextView.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}