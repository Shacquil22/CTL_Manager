package com.example.ctl_manageer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }

        getSupportActionBar().hide();

        //Initialize handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Redirect to main activity or landing screen
                startActivity(new Intent(MainActivity.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }, 3000);
    }
}