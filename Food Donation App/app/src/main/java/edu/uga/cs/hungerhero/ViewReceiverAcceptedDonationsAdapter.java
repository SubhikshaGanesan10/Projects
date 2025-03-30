package edu.uga.cs.hungerhero;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewReceiverAcceptedDonationsAdapter extends RecyclerView.Adapter<ViewReceiverAcceptedDonationsAdapter.AcceptedDonationViewHolder> {

    private Context context;
    private List<AcceptedDonationItem> acceptedDonationList;
    private DatabaseReference confirmedDonationsRef;
    private DatabaseReference donorAcceptedDonationsRef;

    public ViewReceiverAcceptedDonationsAdapter(Context context, List<AcceptedDonationItem> acceptedDonationList) {
        this.context = context;
        this.acceptedDonationList = acceptedDonationList;
        this.confirmedDonationsRef = FirebaseDatabase.getInstance().getReference("confirmed donations");
        this.donorAcceptedDonationsRef = FirebaseDatabase.getInstance().getReference("donor's accepted donations list");
    }

    @NonNull
    @Override
    public AcceptedDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_view_receiver_accepted_donations_cardview, parent, false);
        return new AcceptedDonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedDonationViewHolder holder, int position) {
        AcceptedDonationItem acceptedDonationItem = acceptedDonationList.get(position);

        holder.receiverNameTextView.setText("RECEIVER NAME: " + acceptedDonationItem.getReceiverUsername());
        holder.itemNameTextView.setText("ITEM: " + acceptedDonationItem.getItemName());
        holder.itemDescriptionTextView.setText("ITEM DESCRIPTION: " + acceptedDonationItem.getItemDescription());

        // Set the confirm donation button click listener
        holder.confirmDonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store the donation data in the "confirmed donations" path
                storeConfirmedDonation(acceptedDonationItem);

                // Start the ChatboxActivity
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("donorUserId", acceptedDonationItem.getDonorUserId());
                intent.putExtra("receiverUserId", acceptedDonationItem.getReceiverUserId());
                intent.putExtra("itemName", acceptedDonationItem.getItemName());
                intent.putExtra("receiverUsername", acceptedDonationItem.getReceiverUsername());
                context.startActivity(intent);
            }
        });

        // Set the cancel donation button click listener
        holder.cancelDonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the card view from the display
                int position = holder.getAdapterPosition();
                acceptedDonationList.remove(position);
                notifyItemRemoved(position);

                // Remove the donation data from the "donor's accepted donations list" path
                removeDonationFromDatabase(acceptedDonationItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return acceptedDonationList.size();
    }

    private void storeConfirmedDonation(AcceptedDonationItem acceptedDonationItem) {
        // Create a new map to store the confirmed donation data
        Map<String, Object> confirmedDonationData = new HashMap<>();
        confirmedDonationData.put("donorName", acceptedDonationItem.getDonorName());
        confirmedDonationData.put("donorAddress", acceptedDonationItem.getDonorAddress());
        confirmedDonationData.put("itemName", acceptedDonationItem.getItemName());
        confirmedDonationData.put("itemDescription", acceptedDonationItem.getItemDescription());
        confirmedDonationData.put("donorUserId", acceptedDonationItem.getDonorUserId());
        confirmedDonationData.put("receiverUserId", acceptedDonationItem.getReceiverUserId());
        confirmedDonationData.put("receiverUsername", acceptedDonationItem.getReceiverUsername());

        // Push the confirmed donation data to the "confirmed donations" path
        confirmedDonationsRef.push().setValue(confirmedDonationData);
    }

    private void removeDonationFromDatabase(AcceptedDonationItem acceptedDonationItem) {
        // Get the reference to the donation data in the "donor's accepted donations list" path
        Query donationQuery = donorAcceptedDonationsRef.child(acceptedDonationItem.getDonorUserId())
                .orderByChild("itemName")
                .equalTo(acceptedDonationItem.getItemName());

        donationQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AcceptedDonationItem item = snapshot.getValue(AcceptedDonationItem.class);
                    if (item.getReceiverUserId().equals(acceptedDonationItem.getReceiverUserId())) {
                        snapshot.getRef().removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    public static class AcceptedDonationViewHolder extends RecyclerView.ViewHolder {
        TextView receiverNameTextView, itemNameTextView, itemDescriptionTextView;
        Button confirmDonationButton, cancelDonationButton;

        public AcceptedDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverNameTextView = itemView.findViewById(R.id.receiverNameTextView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            confirmDonationButton = itemView.findViewById(R.id.confirmDonationButton);
            cancelDonationButton = itemView.findViewById(R.id.cancelDonationButton);
        }
    }
}