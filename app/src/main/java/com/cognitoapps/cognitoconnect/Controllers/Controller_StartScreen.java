package com.cognitoapps.cognitoconnect.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cognitoapps.cognitoconnect.R;

public class Controller_StartScreen extends AppCompatActivity {

    private Button btn_signin, btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.view_start_screen);

        btn_signin = findViewById(R.id.btn_to_signin);
        btn_signup = findViewById(R.id.btn_to_signup);



        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Controller_StartScreen.this, Controller_SignIn.class);
                startActivity(intent);

            }
        });



        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Controller_StartScreen.this, Controller_SignUp.class);
                startActivity(intent);
            }
        });


    }
}