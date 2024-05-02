package com.example.crab_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.crab_customer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.mainNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_item2) {
                replaceFragment(new HistoryFragment());
            }
            else if (item.getItemId() == R.id.menu_item3) {
                replaceFragment(new ProfileFragment());
            }
            else replaceFragment(new HomeFragment());
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }
}