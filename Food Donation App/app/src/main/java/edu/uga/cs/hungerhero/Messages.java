package edu.uga.cs.hungerhero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messages extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
    private RecyclerView messagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hungerhero-c990d-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_messages);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messagesRecyclerView.setAdapter(new MessagesAdapter(messagesLists, Messages.this));

        messagesAdapter = new MessagesAdapter(messagesLists,  Messages.this);
        messagesRecyclerView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesLists.clear();

                for(DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){

                    String lastMessage = "";
                    int unseenMessages = 0;

                    databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int getChatCounts = (int)snapshot.getChildrenCount();

                            if(getChatCounts > 0){
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                    final String getKey =  dataSnapshot1.getKey();
                                    final String getUserOne =  dataSnapshot1.child("user_1").getValue(String.class);
                                    final String getUserTwo =  dataSnapshot1.child("user_2").getValue(String.class);
                                }
                            }
                            messagesAdapter.updateData(messagesLists);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}