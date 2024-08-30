package com.cognitoapps.cognitoconnect.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cognitoapps.cognitoconnect.Controllers.Adapters.Adapter_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Controller_Home extends AppCompatActivity {


    RecyclerView recyclerView_chats;
    Adapter_Chat adapterChat;

    TextView lbl_username;
    Button btn_new_chat,btn_sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_home);

        recyclerView_chats = findViewById(R.id.rcl_chat_view);
        btn_new_chat = findViewById(R.id.btn_new_chat);
        btn_sign_out = findViewById(R.id.btn_sign_out);
        recyclerView_chats.setLayoutManager(new LinearLayoutManager(this));
        lbl_username = findViewById(R.id.lbl_chat);

          // lbl_username.setText("User : "+Model_Current_User.usrStore.getPhone());



        //recyclerview handler
        FirebaseRecyclerOptions<Model_Chat> options =
                new FirebaseRecyclerOptions.Builder<Model_Chat>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Chat_log").child(Model_Current_User.usrStore.getPhone()), Model_Chat.class)
                        .build();

        adapterChat = new Adapter_Chat(options, this);
        recyclerView_chats.setAdapter(adapterChat);




        btn_new_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Controller_Home.this, Controller_Create_New_Message.class);
                startActivity(intent);
                
            }
        });





        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //remove login status and username
                SharedPreferences sharedPreferences = getSharedPreferences("CognitoConnectPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username","" );
                editor.putBoolean("isLoggedIn",false);
                editor.apply();

                Intent intent = new Intent(Controller_Home.this, Controller_SignIn.class);
                startActivity(intent);


               // remove this activity
                finish();





            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterChat.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterChat.stopListening();


        //kill the activity when screen is off
        finish();
    }


    //back press removed
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
    }
}