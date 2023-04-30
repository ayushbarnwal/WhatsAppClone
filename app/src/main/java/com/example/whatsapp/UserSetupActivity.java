package com.example.whatsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsapp.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UserSetupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ImageView profileImage;
    EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        getSupportActionBar().hide();

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        profileImage = (ImageView) findViewById(R.id.profile_picture);
        etUserName = (EditText) findViewById(R.id.etUser_name);

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

        Button saveProfile = (Button) findViewById(R.id.button);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserSetupActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null){       //if user selected any image then getdata will give path
            Uri sFile = data.getData();
            profileImage.setImageURI(sFile);

            final StorageReference reference = storage.getReference().child("profile picture")
                    .child(FirebaseAuth.getInstance().getUid());        //to upload the image in storage here we given uid so when user update his profile pic then it will get overridden in storage.... so if we want to create different id for different image uploaded then use push method
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {     //moto is to display that image in recycler view of mainActivity so first we grab the link of image and then set the link in users Firebase
                        @Override
                        public void onSuccess(Uri uri) {
                            User user = new User();
                            user.setUserId(FirebaseAuth.getInstance().getUid());
                            user.setProfilePicture(uri.toString());
                            user.setUserName(etUserName.getText().toString());
                            HashMap<String , Object> obj = new HashMap<>();     // to update on firebase
                            obj.put("profile picture",uri.toString());
                            obj.put("userName",etUserName.getText().toString());
                            database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                                    .updateChildren(obj);
                            Toast.makeText(UserSetupActivity.this,"Profile Pic Updated",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}