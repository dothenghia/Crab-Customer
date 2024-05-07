package com.example.crab_customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.crab_customer.IntroActivity;
import com.example.crab_customer.R;

public class ProfileFragment extends Fragment {

    Button logoutButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutButton = rootView.findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(v -> {
            // Clear login state
            clearLoginState();
            // Redirect to login activity
            Intent intent = new Intent(getActivity(), IntroActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return rootView;
    }

    private void clearLoginState() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("loginState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLoggedIn");
        editor.apply();
    }
}
