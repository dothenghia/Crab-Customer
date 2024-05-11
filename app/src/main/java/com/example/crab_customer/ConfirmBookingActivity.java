package com.example.crab_customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.maps.DirectionsApi;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmBookingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private  GoogleMap myMap;
    private String apiKey = "AIzaSyDoOZOf36HTmcY98rqkNPHkctY7pCLmGfU";
    private LatLng destinationLatLng;
    private LatLng pickupLatLng;
    String destinationName;
    String pickupName;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private double distance;
    private ListView carListView;
    private Button confirmButton;
    private VehicleAdapter adapter;
    private List<VehicleAdapter.Vehicle> vehicleList;
    DocumentReference DiaChiDonRef, DiaChiDenRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        destinationName = getIntent().getStringExtra("destination_name");
        destinationLatLng = getIntent().getParcelableExtra("destination_latlng");
        pickupName = getIntent().getStringExtra("pickup_name");
        pickupLatLng = getIntent().getParcelableExtra("pickup_latlng");
        checkAddressExisted(destinationLatLng, destinationName, documentReference -> DiaChiDenRef =  documentReference);
        checkAddressExisted(pickupLatLng, pickupName, documentReference -> DiaChiDonRef = documentReference);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);

        /*View bottomSheet = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Handle bottom sheet state changes here
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Handle bottom sheet slide offset changes here
            }
        });*/
        carListView = findViewById(R.id.vehicleListView);
        confirmButton = findViewById(R.id.confirmBtn);

        // Create dummy data

    }
    private BitmapDescriptor getBitmapDescriptor(int drawableResourceId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableResourceId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public double Direction(LatLng start, LatLng end, @NonNull GoogleMap googleMap){
        myMap = googleMap;
        myMap.addMarker(new MarkerOptions().position(start).title("start").icon(getBitmapDescriptor(R.drawable.marker_start)));
        myMap.addMarker(new MarkerOptions().position(end).title("end").icon(getBitmapDescriptor(R.drawable.marker_end)));
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
        DirectionsResult directionsResult;
        try {
            directionsResult = DirectionsApi.newRequest(context)
                    .origin(new com.google.maps.model.LatLng(start.latitude, start.longitude))
                    .destination(new com.google.maps.model.LatLng(end.latitude, end.longitude))
                    .mode(TravelMode.DRIVING) // or TravelMode.WALKING for walking directions
                    .await();
            if (directionsResult != null) {
                List<LatLng> path = PolyUtil.decode(directionsResult.routes[0].overviewPolyline.getEncodedPath());
                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(path)
                        .width(15)
                        .color(Color.rgb(0,129,76));
                myMap.addPolyline(polylineOptions);

                // Set camera bounds to include entire route
                LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                for (LatLng latLng : path) {
                    boundsBuilder.include(latLng);
                }
                LatLngBounds bounds = boundsBuilder.build();
                myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                double distance = SphericalUtil.computeLength(path)/1000;
                return distance;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        distance = Direction(pickupLatLng,destinationLatLng,myMap);
        vehicleList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        vehicleList.add(new VehicleAdapter.Vehicle(R.drawable.bike,"Bike", decimalFormat.format(Math.round(7000*distance/10)*10)+"đ"));
        vehicleList.add(new VehicleAdapter.Vehicle(R.drawable.small,"Small", decimalFormat.format(Math.round(12000*distance/10)*10)+"đ"));
        vehicleList.add(new VehicleAdapter.Vehicle(R.drawable.medium,"Medium", decimalFormat.format(Math.round(15000*distance/10)*10)+"đ"));

        // Set up the adapter
        adapter = new VehicleAdapter(this, vehicleList);
        carListView.setAdapter(adapter);

        // Handle item click
        carListView.setOnItemClickListener((parent, view, position, id) -> {
            adapter.setSelectedItem(position);
        });

        // Handle confirm button click
        confirmButton.setOnClickListener(v -> {
            if (adapter.getSelectedItem() != -1) {
                VehicleAdapter.Vehicle selectedCar = vehicleList.get(adapter.getSelectedItem());
                Toast.makeText(ConfirmBookingActivity.this, "Selected car: " + selectedCar.getType(), Toast.LENGTH_SHORT).show();
                Map<String, Object> chuyenXe = new HashMap<>();
                chuyenXe.put("DiaChiDon", DiaChiDonRef);
                chuyenXe.put("DiaChiDen", DiaChiDenRef);
                String giaTien = selectedCar.getPrice().replace("đ", "");
                giaTien = giaTien.replace(",","");
                chuyenXe.put("GiaTien", Integer.parseInt(giaTien));
                chuyenXe.put("HinhThucDatXe", "app");
                switch (selectedCar.getType()) {
                    case "Bike":
                        chuyenXe.put("IDLoaiXe", "1");
                        break;
                    case "Small":
                        chuyenXe.put("IDLoaiXe", "2");
                        break;
                    case "Medium":
                        chuyenXe.put("IDLoaiXe", "3");
                        break;
                }
                chuyenXe.put("IDTaiXe", "");
                DocumentReference ChuyenXeRef = db.collection("ChuyenXe").document();
                ChuyenXeRef.set(chuyenXe)
                        .addOnSuccessListener(aVoid -> {
                            navigateToRequestActivity();
                            Log.d("db", "Document added successfully!");
                        })
                        .addOnFailureListener(e -> {
                            Log.w("db", "Error adding document", e);
                        });
            } else {
                Toast.makeText(ConfirmBookingActivity.this, "Please select a car", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkAddressExisted(LatLng latLng, String name, OnAddressCheckedListener listener) {
        Query query = db.collection("DiaChi")
                .whereEqualTo("KinhDo", latLng.longitude)
                .whereEqualTo("ViDo", latLng.latitude);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    Log.d("db", "Address already exists in database");
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    listener.onAddressChecked(documentSnapshot.getReference());
                } else {
                    addAddressToDatabase(latLng, name, listener);
                }
            } else {
                Log.w("db", "Error getting documents: ", task.getException());
            }
        });
    }

    private void addAddressToDatabase(LatLng latLng, String name, OnAddressCheckedListener listener) {
        DocumentReference DiaChiRef = db.collection("DiaChi").document();
        Map<String, Object> data = new HashMap<>();
        data.put("KinhDo", latLng.longitude);
        data.put("TenDiaChi", name);
        data.put("TenDiaChiArray", Arrays.asList(name.split("\\s+")));
        data.put("ViDo", latLng.latitude);
        DiaChiRef.set(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d("db", "Document added successfully!");
                    listener.onAddressChecked(DiaChiRef);
                })
                .addOnFailureListener(e -> {
                    Log.w("db", "Error adding document", e);
                });
    }
    interface OnAddressCheckedListener {
        void onAddressChecked(DocumentReference documentReference);
    }
    private void navigateToRequestActivity() {
        Intent intent = new Intent(ConfirmBookingActivity.this, RequestDriverActivity.class);
        intent.putExtra("pickup_name", pickupLatLng);
        intent.putExtra("pickup_latlng", pickupName);
        intent.putExtra("destination_name", destinationName);
        intent.putExtra("destination_latlng", destinationLatLng);
        startActivity(intent);
    }
}