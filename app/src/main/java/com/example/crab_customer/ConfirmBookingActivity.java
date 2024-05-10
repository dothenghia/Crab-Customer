package com.example.crab_customer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

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
import com.google.maps.DirectionsApi;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.util.List;

public class ConfirmBookingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private  GoogleMap myMap;
    private String apiKey = "AIzaSyCMXUt6IZ2KmeHUcfUvXAZSUYtgWHazBCI";
    private LatLng destinationLatLng;
    private LatLng pickupLatLng;
    String destinationName;
    String pickupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        destinationName = getIntent().getStringExtra("destination_name");
        destinationLatLng = getIntent().getParcelableExtra("destination_latlng");
        pickupName = getIntent().getStringExtra("pickup_name");
        pickupLatLng = getIntent().getParcelableExtra("pickup_latlng");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);

    }
    private BitmapDescriptor getBitmapDescriptor(int drawableResourceId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableResourceId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void Direction(LatLng start, LatLng end, @NonNull GoogleMap googleMap){
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
                myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        Direction(pickupLatLng,destinationLatLng,myMap);

    }

}