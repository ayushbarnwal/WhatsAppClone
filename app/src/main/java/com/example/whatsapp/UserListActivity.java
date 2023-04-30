package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsapp.Adapters.GroupUserAdapter;
import com.example.whatsapp.Adapters.UserAdapter;
import com.example.whatsapp.Fragments.StatusFragment;
import com.example.whatsapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase database;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        database = FirebaseDatabase.getInstance();

        String groupName = getIntent().getStringExtra("GroupName");

        GroupUserAdapter adapter = new GroupUserAdapter(list, UserListActivity.this,groupName);
        recyclerView = findViewById(R.id.user_list_recycler_view);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        database.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setUserId(dataSnapshot.getKey());
                    if (!user.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                        list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button createGroup = (Button) findViewById(R.id.bbb);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserListActivity.this, StatusFragment.class);
                i.putExtra("groupName",groupName);
                startActivity(i);
            }
        });
    }
}