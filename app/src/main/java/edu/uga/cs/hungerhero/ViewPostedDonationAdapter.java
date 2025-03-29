package edu.uga.cs.hungerhero;

import android.content.Context;
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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ViewPostedDonationAdapter extends RecyclerView.Adapter<ViewPostedDonationAdapter.ViewDonationViewHolder> {

    private Context context;
    private List<DonationItem> donationList;
    private DatabaseReference donationRef;

    public ViewPostedDonationAdapter(Context context, List<DonationItem> donationList) {
        this.context = context;
        this.donationList = donationList;
        this.donationRef = FirebaseDatabase.getInstance().getReference("donations");
    }

    @NonNull
    @Override
    public ViewDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_posted_donation, parent, false);
        return new ViewDonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDonationViewHolder holder, int position) {
        DonationItem donationItem = donationList.get(position);

        holder.donorNameTextView.setText("DONOR NAME: " + donationItem.getDonorName());
        holder.donorAddressTextView.setText("DONOR ADDRESS: " + donationItem.getDonorAddress());
        holder.itemNameTextView.setText("ITEM: " + donationItem.getItemName());
        holder.itemDescriptionTextView.setText("ITEM DESCRIPTION: " + donationItem.getItemDescription());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDonation(donationItem.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    private void deleteDonation(String donationId) {
        donationRef.child(donationId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getRef().removeValue();
                    int position = donationList.indexOf(dataSnapshot.getValue(DonationItem.class));
                    donationList.remove(position);
                    notifyItemRemoved(position);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    public static class ViewDonationViewHolder extends RecyclerView.ViewHolder {
        TextView donorNameTextView, donorAddressTextView, itemNameTextView, itemDescriptionTextView;
        Button deleteButton;

        public ViewDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            donorNameTextView = itemView.findViewById(R.id.donorNameTextView);
            donorAddressTextView = itemView.findViewById(R.id.donorAddressTextView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            deleteButton = itemView.findViewById(R.id.viewPostedDonationButton);
        }
    }
}