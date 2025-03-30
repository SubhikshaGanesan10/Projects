package edu.uga.cs.hungerhero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private ImageView sendButton;
    private TextView receiverNameTextView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference chatRootRef;
    private String chatRoomId;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chat);

        // Initialize Firebase Auth and Database
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        chatRootRef = FirebaseDatabase.getInstance().getReference().child("chat_rooms");

        // Retrieve data from ViewReceiverAcceptedDonationsAdapter
        String donorUserId = getIntent().getStringExtra("donorUserId");
        String receiverUserId = getIntent().getStringExtra("receiverUserId");
        String itemName = getIntent().getStringExtra("itemName");
        String receiverUsername = getIntent().getStringExtra("receiverUsername");

        // Set up the chat room ID
        chatRoomId = generateChatRoomId(donorUserId, receiverUserId);

        // Set the receiver's username in the TextView
        receiverNameTextView = findViewById(R.id.name);
        receiverNameTextView.setText(receiverUsername);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        // Set up the RecyclerView and its adapter
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);
        messagesRecyclerView.setAdapter(chatAdapter);

        // Set up the EditText and send button
        messageEditText = findViewById(R.id.messageEditTxt);
        sendButton = findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Listen for new messages in the chat room
        listenForNewMessages();
    }

    private String generateChatRoomId(String donorUserId, String receiverUserId) {
        // Generate a unique chat room ID based on the donor's and receiver's user IDs
        return donorUserId + "_" + receiverUserId;
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            String messageId = chatRootRef.child(chatRoomId).push().getKey();
            ChatMessage message = new ChatMessage(messageId, messageText, currentUser.getUid());
            chatRootRef.child(chatRoomId).child(messageId).setValue(message);
            messageEditText.setText("");
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void listenForNewMessages() {
        chatRootRef.child(chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = snapshot.getValue(ChatMessage.class);
                    chatMessages.add(message);
                }
                chatAdapter.notifyDataSetChanged();

                // Check if the chatMessages list is not empty
                if (!chatMessages.isEmpty()) {
                    int lastPosition = chatMessages.size() - 1;
                    // Check if the last position is a valid position for the adapter
                    if (lastPosition < chatAdapter.getItemCount()) {
                        messagesRecyclerView.smoothScrollToPosition(lastPosition);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }
}