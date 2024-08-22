package com.cognitoapps.cognitoconnect.Controllers;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cognitoapps.cognitoconnect.Adapter_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Controller_Home extends AppCompatActivity {


    RecyclerView recyclerView_chats;
    Adapter_Chat adapterChat;
    Button button_new_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_home);

        recyclerView_chats = findViewById(R.id.rcl_chat_view);
        button_new_chat = findViewById(R.id.btn_new_chat);
        recyclerView_chats.setLayoutManager(new LinearLayoutManager(this));


        ///////////////
     //   final DatabaseReference RootRef;
     //   RootRef = FirebaseDatabase.getInstance().getReference();
     //   HashMap<String, Object> userdataMap = new HashMap<>();
     //  userdataMap.put("identity", "0312243587");
    //    userdataMap.put("status", "online");
    //    userdataMap.put("chat_id", "#001");


      //  RootRef.child("Chat_log").child("0715158902").child("lg1").updateChildren(userdataMap);
     //   RootRef.child("Chat_log").child("0715158902").child("lg2").updateChildren(userdataMap);

      //  RootRef.child("Chats").child("0715158902").child("a").updateChildren(userdataMap);
     //  RootRef.child("Chats").child("0715158902").child("b").updateChildren(userdataMap);

        
        ////////////////


        //////////

        //recyclerview handler
        FirebaseRecyclerOptions<Model_Chat> options =
                new FirebaseRecyclerOptions.Builder<Model_Chat>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Chat_log").child(Model_Current_User.usrStore.getPhone()), Model_Chat.class)
                        .build();

        adapterChat = new Adapter_Chat(options, this);
        recyclerView_chats.setAdapter(adapterChat);





//////////////
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



}