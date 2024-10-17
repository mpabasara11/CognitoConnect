package com.cognitoapps.cognitoconnect.Controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class Controller_SignIn extends AppCompatActivity {


    private Model_User model_user;

    EditText phone, pass;
    Button btn_log;
    ProgressDialog loading_bar;

 //   String parentDb = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sign_in);

        btn_log = findViewById(R.id.btn_signin);
        phone = findViewById(R.id.txt_signin_phone);
        pass = findViewById(R.id.txt_signin_pass);
        loading_bar = new ProgressDialog(this);


        //login button
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateForm()) {
                    // Proceed with form submission, e.g., send data to server

                    loading_bar.setTitle("Login Account");
                    loading_bar.setMessage("Please wait, while we are checking the credentials.");
                    loading_bar.setCanceledOnTouchOutside(false);
                    loading_bar.show();

                    logginAccount(model_user.getPhone(),model_user.getPassword());

                } else {
                    // Handle the validation errors, maybe show a Toast message
                    // ...
                }

            }
        });


    }

    private boolean validateForm() {
        String phone = this.phone.getText().toString();
        String password = pass.getText().toString();


        if (phone.isEmpty()) {
            this.phone.setError("Please enter your phone number");
            this.phone.requestFocus();
            return false;
        } else if (!isValidPhoneNumber(phone)) {
            this.phone.setError("Phone number must be at least 10 digits");
            this.phone.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            pass.setError("Please enter your password");
            pass.requestFocus();
            return false;
        }

        else {

            // All validations passed

            model_user = new Model_User(phone, password);
            return true;
        }
    }


    // Function to validate phone number (can be modified for specific phone number format)
    private boolean isValidPhoneNumber(String phone) {
        return phone.length() >= 10 && phone.matches("[0-9]+"); // Only digits and minimum 10 characters
    }




    //////////////////////
    //login part
    private void logginAccount(final String phone, final String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()) {

                    Model_User usersData = dataSnapshot.child("Users").child(phone).getValue(Model_User.class);
                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            loading_bar.dismiss();

                            Toast.makeText(Controller_SignIn.this, "Login Successful", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(Controller_SignIn.this, Controller_Home.class);
                            startActivity(intent);

                            Model_Current_User.usrStore = usersData;

                            //save login status and username
                            SharedPreferences sharedPreferences = getSharedPreferences("CognitoConnectPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username",phone );
                            editor.putBoolean("isLoggedIn",true);
                            editor.apply();

                            //remove activity
                            finish();



                        } else {
                            loading_bar.dismiss();
                            Toast.makeText(Controller_SignIn.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Controller_SignIn.this, "Account with this " + phone + " number do not exists on our databases!", Toast.LENGTH_SHORT).show();
                    loading_bar.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //////////////////


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        Intent intent = new Intent(Controller_SignIn.this, Controller_StartScreen.class);
        startActivity(intent);
    }
}