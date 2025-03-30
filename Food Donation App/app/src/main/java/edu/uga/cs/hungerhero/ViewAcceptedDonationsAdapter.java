package edu.uga.cs.hungerhero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewAcceptedDonationsAdapter extends RecyclerView.Adapter<ViewAcceptedDonationsAdapter.AcceptedDonationViewHolder> {

    private Context context;
    private List<AcceptedDonationItem> acceptedDonationList;

    public ViewAcceptedDonationsAdapter(Context context, List<AcceptedDonationItem> acceptedDonationList) {
        this.context = context;
        this.acceptedDonationList = acceptedDonationList;
    }

    @NonNull
    @Override
    public AcceptedDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_view_accepted_donations_cardview, parent, false);
        return new AcceptedDonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedDonationViewHolder holder, int position) {
        AcceptedDonationItem acceptedDonationItem = acceptedDonationList.get(position);

        holder.accepted_donorNameTextView.setText("DONOR NAME: " + acceptedDonationItem.getDonorName());
        holder.accepted_donorAddressTextView.setText("DONOR ADDRESS: " + acceptedDonationItem.getDonorAddress());
        holder.accepted_itemNameTextView.setText("ITEM: " + acceptedDonationItem.getItemName());
        holder.accepted_itemDescriptionTextView.setText("ITEM DESCRIPTION: " + acceptedDonationItem.getItemDescription());
    }

    @Override
    public int getItemCount() {
        return acceptedDonationList.size();
    }

    public static class AcceptedDonationViewHolder extends RecyclerView.ViewHolder {
        TextView accepted_donorNameTextView, accepted_donorAddressTextView, accepted_itemNameTextView, accepted_itemDescriptionTextView;

        public AcceptedDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            accepted_donorNameTextView = itemView.findViewById(R.id.accepted_donorNameTextView);
            accepted_donorAddressTextView = itemView.findViewById(R.id.accepted_donorAddressTextView);
            accepted_itemNameTextView = itemView.findViewById(R.id.accepted_itemNameTextView);
            accepted_itemDescriptionTextView = itemView.findViewById(R.id.accepted_itemDescriptionTextView);
        }
    }
}