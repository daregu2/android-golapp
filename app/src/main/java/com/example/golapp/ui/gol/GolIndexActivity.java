package com.example.golapp.ui.gol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.golapp.databinding.ActivityGolIndexBinding;
import com.example.golapp.databinding.ActivityMainBinding;

public class GolIndexActivity extends AppCompatActivity {

    ActivityGolIndexBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGolIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}