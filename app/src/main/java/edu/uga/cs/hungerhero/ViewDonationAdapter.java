package edu.uga.cs.hungerhero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewDonationAdapter extends RecyclerView.Adapter<ViewDonationAdapter.ViewDonationViewHolder> {

    private Context context;
    private List<DonationItem> donationList;

    // Firebase references
    private DatabaseReference acceptedDonationsRef;
    private DatabaseReference donorAcceptedDonationsRef;
    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String receiverUserId;

    public ViewDonationAdapter(Context context, List<DonationItem> donationList) {
        this.context = context;
        this.donationList = donationList;

        // Initialize the Firebase references
        acceptedDonationsRef = FirebaseDatabase.getInstance().getReference("receiver accepted donations");
        donorAcceptedDonationsRef = FirebaseDatabase.getInstance().getReference("donor's accepted donations list");
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        receiverUserId = currentUser.getUid();
    }

    @NonNull
    @Override
    public ViewDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donation_item, parent, false);
        return new ViewDonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDonationViewHolder holder, int position) {
        DonationItem donationItem = donationList.get(position);
        holder.bind(donationItem);

        holder.acceptDonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store the accepted donation in the "receiver accepted donations" and "donor's accepted donations list" paths
                getUsernameFromDatabase(donationItem);

                // Remove the donation item from the local list
                int itemPosition = donationList.indexOf(donationItem);
                donationList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    private void getUsernameFromDatabase(DonationItem donationItem) {
        usersRef.child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String receiverName = dataSnapshot.child("name").getValue(String.class);
                    storeAcceptedDonationData(donationItem, receiverUserId, receiverName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    private void storeAcceptedDonationData(DonationItem donationItem, String receiverUserId, String receiverName) {
        // Get the donor's user ID from the DonationItem object
        String donorUserId = donationItem.getUserId();

        // Create a new map to store the accepted donation data
        Map<String, Object> acceptedDonationData = new HashMap<>();
        acceptedDonationData.put("donorName", donationItem.getDonorName());
        acceptedDonationData.put("donorAddress", donationItem.getDonorAddress());
        acceptedDonationData.put("itemName", donationItem.getItemName());
        acceptedDonationData.put("itemDescription", donationItem.getItemDescription());
        acceptedDonationData.put("donorUserId", donorUserId);
        acceptedDonationData.put("receiverUserId", receiverUserId);
        acceptedDonationData.put("receiverUsername", receiverName);

        // Push the accepted donation data to the "receiver accepted donations" path
        acceptedDonationsRef.push().setValue(acceptedDonationData);

        // Push the accepted donation data to the "donor's accepted donations list" path
        donorAcceptedDonationsRef.push().setValue(acceptedDonationData);

    }

    public static class ViewDonationViewHolder extends RecyclerView.ViewHolder {

        private TextView donorNameTextView;
        private TextView donorAddressTextView;
        private TextView itemNameTextView;
        private TextView itemDescriptionTextView;
        public Button acceptDonationButton;

        public ViewDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            donorNameTextView = itemView.findViewById(R.id.donorNameTextView);
            donorAddressTextView = itemView.findViewById(R.id.donorAddressTextView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            acceptDonationButton = itemView.findViewById(R.id.acceptDonationButton);
        }

        public void bind(DonationItem donationItem) {
            donorNameTextView.setText("DONOR NAME: " + donationItem.getDonorName());
            donorAddressTextView.setText("DONOR ADDRESS: " + donationItem.getDonorAddress());
            itemNameTextView.setText("ITEM: "+ donationItem.getItemName());
            itemDescriptionTextView.setText("ITEM DESCRIPTION: " + donationItem.getItemDescription());
        }
    }
}