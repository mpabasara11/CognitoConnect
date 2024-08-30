package com.cognitoapps.cognitoconnect.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.R;

public class Controller_Chat_Settings extends AppCompatActivity {

    String chat_owner,chat_recipient,chat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_chat_settings);


         chat_owner = Model_Current_User.usrStore.getPhone();
        chat_recipient = getIntent().getStringExtra("chat_recipient");
        chat_id = getIntent().getStringExtra("chat_id");


    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        Intent intent = new Intent(Controller_Chat_Settings.this, Controller_Chat.class);
        intent.putExtra("chat_recipient", chat_recipient);
        intent.putExtra("chat_id", chat_id);
        startActivity(intent);


        //remove this activity
        finish();
    }




}