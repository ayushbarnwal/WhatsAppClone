package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    ImageView profilePic;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        EditText etUserText = (EditText) findViewById(R.id.etUser_name);
        EditText etStatusText = (EditText) findViewById(R.id.status);
        profilePic = (ImageView) findViewById(R.id.profile_pic);

        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {   //to make that profile pic get intact to user setting every time
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getProfilePicture())
                        .placeholder(R.drawable.user).into(profilePic);
                etStatusText.setText(user.getStatus());
                etUserText.setText(user.getUserName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView setImage = (ImageView) findViewById(R.id.set_profile_picture);
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,33);
            }
        });

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName = etUserText.getText().toString();
                String Status = etStatusText.getText().toString();

                database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                        .child("userName").setValue(UserName);
                database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                        .child("Status").setValue(Status);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null){       //if user selected any image then getdata will give path
            Uri sFile = data.getData();
            profilePic.setImageURI(sFile);

            final StorageReference reference = storage.getReference().child("profile picture")
                    .child(FirebaseAuth.getInstance().getUid());        //to upload the image in storage here we given uid so when user update his profile pic then it will get overridden in storage.... so if we want to create different id for different image uploaded then use push method
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {     //moto is to display that image in recycler view of mainActivity so first we grab the link of image and then set the link in users Firebase
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                                    .child("profile picture").setValue(uri.toString());
                            Toast.makeText(SettingsActivity.this,"Profile Pic Updated",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });
        }

    }
}