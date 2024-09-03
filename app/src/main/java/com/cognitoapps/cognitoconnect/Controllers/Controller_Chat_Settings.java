package com.cognitoapps.cognitoconnect.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cognitoapps.cognitoconnect.Models.Model_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Controller_Chat_Settings extends AppCompatActivity {

    String chat_owner,chat_recipient,chat_id;
    Switch facial_exp,last_seen;

    Boolean is_button_changed_programmatically = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_chat_settings);

        facial_exp = findViewById(R.id.switch_facial_exp);
        last_seen = findViewById(R.id.switch_last_seen);


         chat_owner = Model_Current_User.usrStore.getPhone();
        chat_recipient = getIntent().getStringExtra("chat_recipient");
        chat_id = getIntent().getStringExtra("chat_id");



        final DatabaseReference RootRef1;
        RootRef1 = FirebaseDatabase.getInstance().getReference();

        RootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Model_Chat chatData = snapshot.child("Chat_log").child(chat_owner).child(chat_recipient).getValue(Model_Chat.class);

                if(chatData != null) {

                  boolean is_last_seen_on = chatData.getLast_seen();
                  boolean is_facial_exp_on = chatData.getFace_exp();

                    if(is_last_seen_on)
                    {
                        last_seen.setChecked(true);
                    }
                    else
                    {
                        last_seen.setChecked(false);

                    }

                    if(is_facial_exp_on)
                    {
                        is_button_changed_programmatically = true;
                        facial_exp.setChecked(true);
                        is_button_changed_programmatically = false;
                    }
                    else
                    {
                        is_button_changed_programmatically = true;
                        facial_exp.setChecked(false);
                        is_button_changed_programmatically = false;
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        facial_exp.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {

                if(!is_button_changed_programmatically)
                {
                    String privacyPolicy =
                            "Privacy Policy\n\n" +
                                    "Effective Date: 09/03/2024\n\n" +
                                    "1. Introduction\n\n" +
                                    "Welcome to CognitoConnect! Your privacy is important to us. This Privacy Policy explains how we handle and protect your personal information, specifically related to the facial expression analysis feature in our chat application.\n\n" +
                                    "2. Facial Expression Analysis\n\n" +
                                    "Our chat app includes a feature that analyzes facial expressions using your mobile device’s camera. This feature processes facial images directly on your device and uses the results to provide additional emotional context in your conversations.\n\n" +
                                    "3. Data Processing and Storage\n\n" +
                                    "- On-Device Processing: All facial images captured for expression analysis are processed exclusively on your device. We do not transmit or store any facial images or raw data on our servers.\n" +
                                    "- Data Sharing: Only the results of the facial expression analysis are shared with other users as part of your chat interactions. The raw facial images remain on your device and are never sent to or accessed by any third party.\n\n" +
                                    "4. Data Security\n\n" +
                                    "We are committed to protecting your data. All processing of facial images occurs locally on your device, ensuring that sensitive information remains private and secure.\n\n" +
                                    "5. Third-Party Access\n\n" +
                                    "No third parties have access to your facial images or the results of the facial expression analysis. Your data is not shared with external entities or used for any purposes beyond providing the feature’s functionality.\n\n" +
                                    "6. Updates to This Policy\n\n" +
                                    "We may update this Privacy Policy from time to time. We will notify you of any significant changes by updating the effective date of this policy. We encourage you to review this policy periodically to stay informed about how we are protecting your data.\n\n" +
                                    "7. Contact Us\n\n" +
                                    "If you have any questions or concerns about this Privacy Policy or how we handle your data, please contact us at mpabasara11@gmail.com .\n\n" +
                                    "8. Consent\n\n" +
                                    "By using our app, you consent to the terms outlined in this Privacy Policy.\n\n" +
                                    "Thank you for choosing CognitoConnect.";


                    LayoutInflater inflater = LayoutInflater.from(this);
                    android.view.View dialogView = inflater.inflate(R.layout.view_dialog_privacy_policy, null);

                    // Find the TextView and set the privacy policy text
                    TextView privacyPolicyTextView = dialogView.findViewById(R.id.tvPrivacyPolicy);

                    privacyPolicyTextView.setText(privacyPolicy);



                    // Show a dialog to inform the user about the privacy risk
                    new AlertDialog.Builder(this)
                            .setTitle("Privacy Notice")
                            .setView(dialogView)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // User accepted the terms, feature is enabled

                                change_facial_exp_status(chat_owner,chat_recipient,true);

                                facial_exp.setChecked(true);
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                // User declined, revert the switch

                                change_facial_exp_status(chat_owner,chat_recipient,false);

                                facial_exp.setChecked(false);
                            })
                            .setCancelable(false)
                            .show();
                }


            } else {
                // Feature is disabled

                change_facial_exp_status(chat_owner,chat_recipient,false);

            }


        });



        last_seen.setOnCheckedChangeListener((buttonView, isChecked) ->
        {

            if (isChecked)
            {
                change_last_seen_status(chat_owner,chat_recipient,true);
            }
            else
            {
                change_last_seen_status(chat_owner,chat_recipient,false);
            }


        });











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



    public void change_last_seen_status(String chat_owner,String chat_recipient,Boolean status) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> userSettingsMap = new HashMap<>();
        userSettingsMap.put("last_seen",status);


        RootRef.child("Chat_log").child(chat_owner).child(chat_recipient).updateChildren(userSettingsMap);
    }

    public void change_facial_exp_status(String chat_owner,String chat_recipient,Boolean status) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> userSettingsMap = new HashMap<>();
        userSettingsMap.put("face_exp",status);

        RootRef.child("Chat_log").child(chat_owner).child(chat_recipient).updateChildren(userSettingsMap);

    }




}