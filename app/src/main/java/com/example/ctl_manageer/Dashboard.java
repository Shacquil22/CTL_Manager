package com.example.ctl_manageer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ctl_manageer.databinding.ActivityDashboardBinding;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
         //   setTheme(R.style.Theme_Dark);
        //} else {
        //    setTheme(R.style.Theme_Light);
        //}

        getSupportActionBar().hide();

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding.bottomNavigationBar.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.homeFragment:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.searchFragment:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.folderFragment:
                    replaceFragment(new FolderFragment());
                    break;
                case R.id.settingsFragment:
                    replaceFragment(new SettingsFragment());
                    break;
                case R.id.profileFragment:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }
}