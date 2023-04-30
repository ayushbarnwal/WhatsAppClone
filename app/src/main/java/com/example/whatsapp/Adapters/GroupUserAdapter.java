package com.example.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.ChatDetailActivity;
import com.example.whatsapp.Models.User;
import com.example.whatsapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.ViewHolder> {

    ArrayList<User> list;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String grpName;

    public GroupUserAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public GroupUserAdapter(ArrayList<User> list, Context context,String grpName) {
        this.list = list;
        this.context = context;
        this.grpName = grpName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);
        return new GroupUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = list.get(position);
        Picasso.get().load(user.getProfilePicture()).placeholder(R.drawable.user).into(holder.image);
        holder.groupName.setText(user.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", user.getUserId());
                intent.putExtra("profilePic", user.getProfilePicture());
                intent.putExtra("userName", user.getUserName());
                context.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                database.getReference().child("Group Chat").child(grpName).child("users").push().setValue(list.get(holder.getAdapterPosition()).getUserId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView groupName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            groupName = itemView.findViewById(R.id.user_name);
            lastMessage = itemView.findViewById(R.id.last_message);
        }
    }



}
