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

import com.cognitoapps.cognitoconnect.Models.Model_User;
import com.cognitoapps.cognitoconnect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Controller_SignUp extends AppCompatActivity {

    EditText phone, pass, con_pass;
    Button btn_reg;

    ProgressDialog loading_bar;

    private Model_User model_user;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sign_up);


        phone = findViewById(R.id.txt_signup_phone);
        pass = findViewById(R.id.txt_signup_pass);
        con_pass = findViewById(R.id.txt_signup_conf_pass);
        btn_reg = findViewById(R.id.btn_signup);
        loading_bar = new ProgressDialog(Controller_SignUp.this);


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateForm()) {
                    // Proceed with form submission, e.g., send data to server

                  //  createAccount(phone.getText().toString(), pass.getText().toString());
                    createAccount(model_user.getPhone(),model_user.getPassword());
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
        String confirmPassword = con_pass.getText().toString();

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
        else if (!isValidPassword(password)) {
            pass.setError("Password must be at least 8 characters and include 1 uppercase letter, 1 lowercase letter, 1 number");
            pass.requestFocus();
            return false;
        }
        else if (confirmPassword.isEmpty()) {
            con_pass.setError("Please confirm your password");
            con_pass.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)) {
            con_pass.setError("Password does not match with confirm password");
            con_pass.requestFocus();
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

    // Function to validate strong password pattern
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(regex);
    }


    private void createAccount(final String phone, final String password)
    {
        loading_bar.setTitle("Creating An Account");
        loading_bar.setMessage("Please wait, while we are checking our databases");
        loading_bar.setCanceledOnTouchOutside(false);
        loading_bar.show();


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (!(snapshot.child("Users").child(phone).exists())) {

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {



                            Toast.makeText(Controller_SignUp.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                            loading_bar.dismiss();


                            //terminate current activity
                            finish();

                            //redirect to login page
                            Intent intent = new Intent(Controller_SignUp.this, Controller_SignIn.class);
                            startActivity(intent);



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Controller_SignUp.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {

                    Toast.makeText(Controller_SignUp.this, "Account with phone " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loading_bar.dismiss();
                    Toast.makeText(Controller_SignUp.this, "Please try again using another phone number or Login using excisting number", Toast.LENGTH_SHORT).show();



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        Intent intent = new Intent(Controller_SignUp.this, Controller_StartScreen.class);
        startActivity(intent);
    }


}