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

public class HistoryAdapter extends ArrayAdapter<HistoryAdapter.History> {
    private final LayoutInflater inflater;

    public int getSelectedItem() {
        return selectedItem;
    }

    // Car.java
    public static class History {
        private final int imgId;
        private final String start;
        private final String end;
        private final String price;

        public History(int imgId, String start, String end, String price) {
            this.imgId = imgId;
            this.start = start;
            this.end = end;
            this.price = price;
        }
        public int getImg() {
            return imgId;
        }
        public String getStart() {
            return start;
        }
        public String getEnd() {
            return end;
        }

        public String getPrice() {
            return price;
        }
    }

    private final List<History> HistoryList;
    private int selectedItem = -1;

    public HistoryAdapter(Context context, List<History> HistoryList) {
        super(context, R.layout.component_history, HistoryList);
        this.HistoryList = HistoryList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.component_history, parent, false);
        }

        History History = HistoryList.get(position);

        ImageView imageView = view.findViewById(R.id.historyImage);
        TextView startTextView = view.findViewById(R.id.historyStart);
        TextView endTextView = view.findViewById(R.id.historyEnd);
        TextView priceTextView = view.findViewById(R.id.historyPrice);

        imageView.setImageResource(History.getImg());
        startTextView.setText(History.getStart());
        endTextView.setText(History.getEnd());
        priceTextView.setText(History.getPrice());

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

