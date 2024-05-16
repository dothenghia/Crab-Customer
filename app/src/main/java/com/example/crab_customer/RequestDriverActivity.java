package com.example.crab_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class RequestDriverActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private  LatLng pickupLatLng, destinationLatLng;
    private String pickupName, destinationName, carType, price;
    private SupportMapFragment mapFragment;
    private AlertDialog alertDialog1, alertDialog2;
    private Handler mHandler1 = new Handler();
    private Handler mHandler2 = new Handler();
    private Runnable mRunnable1, mRunnable2;
    LinearLayout waitingLayout, infoLayout, driverLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_driver);
        destinationName = getIntent().getStringExtra("destination_name");
        destinationLatLng = getIntent().getParcelableExtra("destination_latlng");
        pickupName = getIntent().getStringExtra("pickup_name");
        pickupLatLng = getIntent().getParcelableExtra("pickup_latlng");
        carType = getIntent().getStringExtra("car_type");
        price = getIntent().getStringExtra("price");
        TextView pick_up = findViewById(R.id.pick_up);
        TextView destination = findViewById(R.id.destination);
        TextView cash = findViewById(R.id.cash);
        pick_up.setText(pickupName);
        destination.setText(destinationName);
        cash.setText(price+"đ");
        waitingLayout = findViewById(R.id.waitingLayout);
        infoLayout = findViewById(R.id.infoLayout);
        driverLayout = findViewById(R.id.driverLayout);
        ImageView car_type = findViewById(R.id.carType);
        if (carType.equals("Bike") ) car_type.setImageResource(R.drawable.bike);
        else if (carType.equals("Small")) car_type.setImageResource(R.drawable.small);
        else car_type.setImageResource(R.drawable.medium);
        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map4);
        mapFragment.getMapAsync(this);
        mRunnable1 = () -> showDriverDialog();
        mHandler1.postDelayed(mRunnable1,5000);
        mRunnable2 = () -> {
            // Hiển thị dialog hoàn thành chuyến xe
            showTripCompletedDialog();
        };
        mHandler2.postDelayed(mRunnable2, 15000);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickupLatLng,18f));

    }

    private void showDriverDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.driver_information, null);
        Button okButton1 = viewDialog.findViewById(R.id.ok_button1);
        okButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
                waitingLayout.setVisibility(View.GONE);
                infoLayout.setVisibility(View.GONE);
                driverLayout.setVisibility(View.VISIBLE);
            }
        });

        builder.setView(viewDialog);
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
    private void showTripCompletedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.trip_completed_dialog,null);
        TextView dialogTitle = dialogView.findViewById(R.id.title_trip_completed);
        Button okButton = dialogView.findViewById(R.id.ok_button);
        TextView dialogMessage = dialogView.findViewById(R.id.message1);
        dialogTitle.setText("Trip Completed");
        dialogMessage.setText("Thank you for using our service.");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
                Intent intent = new Intent(RequestDriverActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setView(dialogView);
        alertDialog2 = builder.create();
        alertDialog2.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Loại bỏ bộ đếm thời gian khi Activity bị hủy
        mHandler1.removeCallbacks(mRunnable1);
        mHandler2.removeCallbacks(mRunnable2);
    }
}