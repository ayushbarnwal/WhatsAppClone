package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class GroupChatDetailSetupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_detail_setup);

        TextView etGroupName = (TextView) findViewById(R.id.etGroup_name);
        Button saveBtn = (Button) findViewById(R.id.button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(GroupChatDetailSetupActivity.this,UserListActivity.class);
                i.putExtra("GroupName",etGroupName.getText().toString());
                startActivity(i);
            }
        });


    }
}