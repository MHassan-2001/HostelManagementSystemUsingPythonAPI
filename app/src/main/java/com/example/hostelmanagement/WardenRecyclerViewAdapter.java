package com.example.hostelmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WardenRecyclerViewAdapter extends RecyclerView.Adapter<WardenRecyclerViewAdapter.RVHolder> {

    private ArrayList<String> arrayList;
    private IAcceptandRequestForStudentRecord request;

    public WardenRecyclerViewAdapter(java.util.ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public WardenRecyclerViewAdapter.RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view, parent, false);
        WardenRecyclerViewAdapter.RVHolder rvHolder = new WardenRecyclerViewAdapter.RVHolder(view);
        request = (IAcceptandRequestForStudentRecord) parent.getContext();
        return rvHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull WardenRecyclerViewAdapter.RVHolder holder, int position) {
        holder.accept_btn.setText("Accept");
        holder.decline_btn.setText("Decline");
        holder.textView.setText(arrayList.get(position));
        holder.decline_btn.setOnClickListener(v -> {
            request.DeclineStudentData(position);
        });

        holder.accept_btn.setOnClickListener(v -> {
            request.AcceptStudentData(position);

        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RVHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public TextView textView;
        public Button accept_btn;
        public Button decline_btn;

        public RVHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.complain_txt);
            accept_btn = itemView.findViewById(R.id.update);
            decline_btn = itemView.findViewById(R.id.delete);

        }
    }

    interface IAcceptandRequestForStudentRecord {
        void AcceptStudentData(int position);

        void DeclineStudentData(int position);
    }
}
