package com.example.crab_customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {



    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ListView historyListView = view.findViewById(R.id.historyListView);
        HistoryAdapter adapter;
        List<HistoryAdapter.History> HistoryList = new ArrayList<>();

        HistoryList.add(new HistoryAdapter.History(R.drawable.bike,"Trường Đại học Khoa học Tự nhiên", "Khoa Y Đại học Quốc gia TPHCM", "30,000đ"));
        HistoryList.add(new HistoryAdapter.History(R.drawable.small,"Trung tâm Thương mại NowZone", "Hùng Vương Plaza", "178,000đ"));
        HistoryList.add(new HistoryAdapter.History(R.drawable.medium,"Công viên Gia Định", "LotteMart Gò Vấp", "50,000đ"));

        adapter = new HistoryAdapter(getContext(), HistoryList);
        historyListView.setAdapter(adapter);
        return view;
    }
}