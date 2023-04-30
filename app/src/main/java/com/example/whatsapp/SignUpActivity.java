package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        EditText etPhone = (EditText) findViewById(R.id.phone_no);
        Button signUpBtn = (Button) findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,VerifyOtpActivity.class);
                i.putExtra("number",etPhone.getText().toString().trim());
                startActivity(i);
            }
        });

        if(auth.getCurrentUser()!=null){
            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(i);
        }
    }
}