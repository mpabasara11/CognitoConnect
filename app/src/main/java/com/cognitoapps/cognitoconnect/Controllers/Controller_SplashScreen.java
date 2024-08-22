package com.cognitoapps.cognitoconnect.Controllers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.cognitoapps.cognitoconnect.R;

public class Controller_SplashScreen extends AppCompatActivity {

    private ProgressBar pg_loading;
    private TextView lbl_loading ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_splash_screen);


        // Check internet connection
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (isNetworkAvailable()) {
                    // Internet available, redirect to another activity
                    Intent intent = new Intent(Controller_SplashScreen.this, Controller_StartScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Handle no internet case (e.g., display an error message or retry option)
                    Toast.makeText(Controller_SplashScreen.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    // (Optional) Retry logic or alternative functionality
                }}

            }, 2500);}



    private boolean isNetworkAvailable() {


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}