package com.cognitoapps.cognitoconnect.Controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cognitoapps.cognitoconnect.Adapter_Chat;
import com.cognitoapps.cognitoconnect.Adapter_Conversation;
import com.cognitoapps.cognitoconnect.Models.Model_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Conversation;
import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Controller_Chat extends AppCompatActivity {


    EditText input_message;
    TextView lbl_username;
    Button btn_send_message;
    ProgressDialog loading_bar;

    String chat_owner,chat_recipient,chat_id;

     RecyclerView recyclerView;
     Adapter_Conversation adapterConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_chat);


        input_message = findViewById(R.id.txt_new_message);
        lbl_username = findViewById(R.id.lbl_receiver_username);
        btn_send_message = findViewById(R.id.btn_send_message);


        recyclerView = findViewById(R.id.rcl_chat_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        chat_owner = getIntent().getStringExtra("chat_owner");
        chat_recipient = getIntent().getStringExtra("chat_recipient");
        chat_id = getIntent().getStringExtra("chat_id");

        lbl_username.setText("Recipient: "+ chat_recipient);


        FirebaseRecyclerOptions<Model_Conversation> options =
                new FirebaseRecyclerOptions.Builder<Model_Conversation>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Chats").child(chat_id), Model_Conversation.class)
                        .build();

        adapterConversation = new Adapter_Conversation(options, this);
        recyclerView.setAdapter(adapterConversation);











        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

 if(!(input_message.getText().toString().isEmpty()))
 {
     final DatabaseReference RootRef;
     RootRef = FirebaseDatabase.getInstance().getReference();

     String timestamp = String.valueOf(System.currentTimeMillis());

     HashMap<String, Object> message = new HashMap<>();
     message.put("sender", chat_owner);
     message.put("message", input_message.getText().toString());
     message.put("timestamp",timestamp);


     RootRef.child("Chats").child(chat_id).child(timestamp).updateChildren(message);

     input_message.getText().clear();
 }
 else
 {
     input_message.setError("You did not enter any message !");
     input_message.requestFocus();
 }



            }
        });




    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        Intent intent = new Intent(Controller_Chat.this, Controller_Home.class);
        startActivity(intent);
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapterConversation.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

            adapterConversation.stopListening();

        //kill the activity when screen is off
        finish();
    }
}