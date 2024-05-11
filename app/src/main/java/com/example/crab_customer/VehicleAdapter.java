package com.example.crab_customer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class VehicleAdapter extends ArrayAdapter<VehicleAdapter.Vehicle> {
    private final LayoutInflater inflater;

    public int getSelectedItem() {
        return selectedItem;
    }

    // Car.java
    public static class Vehicle {
        private final int imgId;
        private final String type;
        private final String price;

        public Vehicle(int imgId, String type, String price) {
            this.imgId = imgId;
            this.type = type;
            this.price = price;
        }
        public int getImg() {
            return imgId;
        }
        public String getType() {
            return type;
        }

        public String getPrice() {
            return price;
        }
    }

    private final List<Vehicle> vehicleList;
    private int selectedItem = -1;

    public VehicleAdapter(Context context, List<Vehicle> vehicleList) {
        super(context, R.layout.component_vehicle, vehicleList);
        this.vehicleList = vehicleList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.component_vehicle, parent, false);
        }

        Vehicle vehicle = vehicleList.get(position);

        ImageView imageView = view.findViewById(R.id.vehicleImage);
        TextView typeTextView = view.findViewById(R.id.vehicleType);
        TextView priceTextView = view.findViewById(R.id.vehiclePrice);

        imageView.setImageResource(vehicle.getImg());
        typeTextView.setText(vehicle.getType());
        priceTextView.setText(vehicle.getPrice());

        if (position == selectedItem) {
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.subtle_2));
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }

        return view;
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }

}

