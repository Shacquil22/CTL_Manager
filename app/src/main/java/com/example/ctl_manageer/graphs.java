package com.example.ctl_manageer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ctl_manageer.databinding.ActivityGraphsListBinding;

public class graphs extends AppCompatActivity {

    ActivityGraphsListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs_list);

        binding = ActivityGraphsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}