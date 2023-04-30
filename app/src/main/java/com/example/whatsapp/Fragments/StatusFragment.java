package com.example.whatsapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.Adapters.GroupUserAdapter;
import com.example.whatsapp.Adapters.UserAdapter;
import com.example.whatsapp.GroupChatDetailSetupActivity;
import com.example.whatsapp.Models.User;
import com.example.whatsapp.R;
import com.example.whatsapp.UserListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StatusFragment extends Fragment {

    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase database;
    private RecyclerView recyclerView;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        database = FirebaseDatabase.getInstance();

        FloatingActionButton fab = view.findViewById(R.id.group_chat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GroupChatDetailSetupActivity.class);
                startActivity(intent);
            }
        });

        GroupUserAdapter adapter = new GroupUserAdapter(list, getContext());
        recyclerView = view.findViewById(R.id.groupChat_recycler_view);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        String groupName = getActivity().getIntent().getStringExtra("groupName");

        database.getReference().child("GroupChat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setGroupName(dataSnapshot.child(groupName).getKey());
                    String nn = user.getGroupName();
                    Log.e("TAG",nn);
                        list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

}