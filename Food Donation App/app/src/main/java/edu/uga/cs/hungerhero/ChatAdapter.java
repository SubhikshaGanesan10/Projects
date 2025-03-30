package edu.uga.cs.hungerhero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import edu.uga.cs.hungerhero.ChatMessage;
import edu.uga.cs.hungerhero.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context; // Context of the activity or fragment
    private List<ChatMessage> chatMessages; // List of chat messages to be displayed
    private FirebaseUser currentUser; // Current user instance from Firebase Authentication

    // Constructor to initialize the adapter
    public ChatAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser(); // Get the current user instance
    }

    // This method is called when the RecyclerView needs a new ViewHolder
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the chat_item_layout.xml layout file
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_layout, parent, false);
        return new ChatViewHolder(view); // Create and return a new ChatViewHolder instance
    }

    // This method is called to bind the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position); // Get the chat message at the current position
        holder.messageTextView.setText(message.getMessageText()); // Set the message text

        // Set the message background based on the sender
        if (message.getSenderId().equals(currentUser.getUid())) {
            // If the message is sent by the current user, set the sender bubble background
            holder.messageTextView.setBackgroundResource(R.drawable.sender_bubble);
        } else {
            // If the message is received from another user, set the receiver bubble background
            holder.messageTextView.setBackgroundResource(R.drawable.receiver_bubble);
        }
    }

    // This method returns the total number of items in the adapter
    @Override
    public int getItemCount() {
        return chatMessages.size(); // Return the size of the chatMessages list
    }

    // ViewHolder class to hold the views for each chat message item
    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView; // TextView to display the chat message text

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView); // Find the TextView in the chat_item_layout.xml
        }
    }
}