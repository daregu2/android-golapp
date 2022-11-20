package com.example.golapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.golapp.ui.auth.AuthActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashScreenActivity.this, AuthActivity.class));
        finish();
    }
}