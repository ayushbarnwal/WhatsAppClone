package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.Adapters.ChatAdapter;
import com.example.whatsapp.Models.MessageModel;
import com.example.whatsapp.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatDetailActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    String senderRoom;
    String receiverRoom;
    String receiveId;
    String senderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        getSupportActionBar().hide();

        senderId = auth.getUid();
        receiveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        String UserPhoneNo = getIntent().getStringExtra("userPhoneNo");

        Log.e("TAG","AYUSH"+UserPhoneNo);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(userName);
        ImageView image = (ImageView)findViewById(R.id.profile_image);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(image);

        ImageView bachArrow = (ImageView) findViewById(R.id.back_btn);
        bachArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this,receiveId);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        recyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        EditText etmsg_typed = (EditText) findViewById(R.id.msg_type);

        senderRoom = senderId+receiveId;
        receiverRoom = receiveId+senderId;

        database.getReference().child("chats")
                .child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    MessageModel model = snapshot1.getValue(MessageModel.class);
                    model.setMessageId(snapshot1.getKey());
                    messageModels.add(model);
                }
                chatAdapter.notifyDataSetChanged();   //in order to see msg at run time... if we not do this then msg seen only when we press back button
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ImageView send_btn = (ImageView) findViewById(R.id.send_msg);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg_typed = etmsg_typed.getText().toString();
                final MessageModel model = new MessageModel(senderId,msg_typed);
                model.setTimestamp(new Date().getTime());
                etmsg_typed.setText(" ");

                database.getReference().child("chats")                //to store msg
                        .child(senderRoom)
                        .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {          //push will allote node to different msgs
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(receiverRoom).push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });


                    }
                });

            }
        });

        ImageView call_btn = (ImageView) findViewById(R.id.phone_call);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+UserPhoneNo));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChatDetailActivity.this,"Grant Request to Make call",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(ChatDetailActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                }else
                startActivity(intent);
            }
        });

        ImageView select_file = (ImageView) findViewById(R.id.select_file);
        select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null){       //if user selected any image then getdata will give path
            Uri sFile = data.getData();

            final StorageReference reference = storage.getReference().child("ChatFiles").child(sFile.getLastPathSegment());        //to upload the image in storage here we given uid so when user update his profile pic then it will get overridden in storage.... so if we want to create different id for different image uploaded then use push method
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Toast.makeText(ChatDetailActivity.this,"file Uploaded",Toast.LENGTH_SHORT).show();
                     reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                         @Override
                         public void onSuccess(Uri uri) {
                             MessageModel model = new MessageModel(senderId,uri.toString(),new Date().getTime());
//                             HashMap<String , Object> obj = new HashMap<>();     // to update on firebase
//                             obj.put("file",uri.toString());
//                             obj.put("uid",model.getuId());
//                             obj.put("timestamp",model.getTimestamp());
                             database.getReference().child("chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void unused) {
                                     database.getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void unused) {
                                             Toast.makeText(ChatDetailActivity.this,"file Uploaded in databse",Toast.LENGTH_SHORT).show();
                                         }
                                     });
                                 }
                             });
                         }
                     });
                }
            });



    }}
}