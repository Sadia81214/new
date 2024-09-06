package com.example.grihoshikkhok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.grihoshikkhok.models.Chat;
import com.example.grihoshikkhok.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerViewAdapterChat recyclerViewAdapter ;
    private List<User> mUser ;
    private List<String> chatlist ;
    ProgressBar progressBar ;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recyclerviewchat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        findViewById(R.id.goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar = findViewById(R.id.progressbarchat);
        progressBar.setVisibility(View.VISIBLE);
        chatlist = new ArrayList<>();
        assert firebaseUser != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getSender().equals(firebaseUser.getUid()))
                    {
                        //if(!chatlist.contains(chat.getReceiver()))
                        chatlist.remove(chat.getReceiver());
                        chatlist.add(chat.getReceiver());
                    }
                    else if (chat.getReceiver().equals(firebaseUser.getUid()))
                    {
                        //if(!chatlist.contains(chat.getSender()))
                        chatlist.remove(chat.getSender());
                        chatlist.add(chat.getSender());
                    }
                }
                readuser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void readuser()
    {
        mUser = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();

                for (String id : chatlist)
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        if (user.getId().equals(id))
                        {
                            mUser.add(0,user);
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                recyclerViewAdapter = new RecyclerViewAdapterChat(ChatActivity.this,mUser);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}