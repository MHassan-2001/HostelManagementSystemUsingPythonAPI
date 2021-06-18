package com.example.hostelmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WardenFragment extends Fragment {
    RecyclerView recyclerView;
    private ArrayList<String> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warden, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_w);

        WardenRecyclerViewAdapter recycleView = new WardenRecyclerViewAdapter(data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recycleView);
        return view;
    }

    public static WardenFragment newInstance(ArrayList<String> details) {
        WardenFragment wardenFragment = new WardenFragment();
        wardenFragment.data=details;
        return wardenFragment;
    }
}