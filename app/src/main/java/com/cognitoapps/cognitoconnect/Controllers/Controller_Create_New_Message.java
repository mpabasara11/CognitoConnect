package com.cognitoapps.cognitoconnect.Controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.Models.Model_User;
import com.cognitoapps.cognitoconnect.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.Locale;

public class Controller_Create_New_Message extends AppCompatActivity {


    private Model_User model_user;

    EditText phone;
    Button btn_create_chat;
    ProgressDialog loading_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_create_new_message);

        btn_create_chat = findViewById(R.id.btn_create_chat);
        phone = findViewById(R.id.txt_new_chat_phone);
        loading_bar = new ProgressDialog(this);




        btn_create_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loading_bar.setTitle("Checking for receiver account");
                loading_bar.setMessage("Please wait, while we are checking our databases");
                loading_bar.setCanceledOnTouchOutside(false);
                loading_bar.show();

////////////////////////////

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                String phones_num = phone.getText().toString();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean y;
                        if(!(snapshot.child("Users").child(phones_num).exists()))
                        {
                            Toast.makeText(Controller_Create_New_Message.this, "No receiver account found with that number.", Toast.LENGTH_SHORT).show();
                            loading_bar.dismiss();

                        }
                        else
                        {
                            loading_bar.dismiss();
                            loading_bar.setTitle("Checking for existing chats");
                            loading_bar.setMessage("Please wait, while we are checking our databases");
                            loading_bar.setCanceledOnTouchOutside(false);
                            loading_bar.show();


                            if(!(snapshot.child("Chat_log").child(Model_Current_User.usrStore.getPhone()).child(phones_num).exists()))
                            {
                                String chat_id = Model_Current_User.usrStore.getPhone()+phones_num;
                                /////////////
                                   final DatabaseReference RootRef;
                                   RootRef = FirebaseDatabase.getInstance().getReference();

                                   ////////////////////////chat create on senders
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String currentDate = sdf.format(new Date());

                                   HashMap<String, Object> userdataMap = new HashMap<>();
                                    userdataMap.put("identity", phones_num);
                                    userdataMap.put("created", currentDate);
                                    userdataMap.put("chat_id", chat_id);


                                  RootRef.child("Chat_log").child(Model_Current_User.usrStore.getPhone()).child(phones_num).updateChildren(userdataMap);


                                ////////////////////////chat create on receivers

                                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String currentDate1 = sdf.format(new Date());

                                HashMap<String, Object> userdataMap1 = new HashMap<>();
                                userdataMap.put("identity", Model_Current_User.usrStore.getPhone());
                                userdataMap.put("created", currentDate);
                                userdataMap.put("chat_id", chat_id);

                                RootRef.child("Chat_log").child(phones_num).child(Model_Current_User.usrStore.getPhone()).updateChildren(userdataMap);



                                loading_bar.dismiss();

                                //kill the activity when screen is off
                                finish();

                                Intent intent = new Intent(Controller_Create_New_Message.this, Controller_Chat.class);
                                intent.putExtra("chat_recipient", phones_num);
                                intent.putExtra("chat_id", chat_id);
                                startActivity(intent);


                                /////////////////

                            }
                            else
                            {
                                loading_bar.dismiss();

                                String chat_id = Model_Current_User.usrStore.getPhone()+phones_num;

                                Intent intent = new Intent(Controller_Create_New_Message.this, Controller_Chat.class);
                                intent.putExtra("chat_recipient", phones_num);
                                intent.putExtra("chat_id", chat_id);
                                startActivity(intent);
                            }



                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



//////////////////////////////


            }
        });




    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

            Intent intent = new Intent(Controller_Create_New_Message.this, Controller_Home.class);
            startActivity(intent);

    }





}