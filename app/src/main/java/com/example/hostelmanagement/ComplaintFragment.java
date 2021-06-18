package com.example.hostelmanagement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ComplaintFragment extends Fragment {


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
    String currentDateandTime = sdf.format(new Date());

    private IComplaintListener listenerComplaint;

    private ArrayList<Long> std_roll_no=new ArrayList<>();
    private ArrayList<String> std_room_no=new ArrayList<>();

    private TextView rollno;
    private Button complain;
    private TextView complaintText;

    private String datetoUpdate;

    public static boolean update;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);
        Referencing(view);
        MatchingRollNo();
        return view;
    }

    private void Referencing(View view) {
        rollno = view.findViewById(R.id.roll_no_for_complaint);
        complain = view.findViewById(R.id.submit_complain);
        complaintText = view.findViewById(R.id.complain_txt_et);
    }

    private void MatchingRollNo() {
        int val;
        complain.setOnClickListener(v -> {
//            Log.d("ASD","ASDC");
            for (int values = 0; values < std_roll_no.size(); values++) {
//                Log.d("ASD", std_roll_no.size()+" "+std_room_no.size());
                if (std_roll_no.get(values).compareTo(Long.parseLong(rollno.getText().toString())) == 0 && !update) {
                    listenerComplaint.AddComplaint(std_roll_no.get(values), std_room_no.get(values),
                            complaintText.getText().toString(),
                            currentDateandTime);

                }
//                Log.d("ASD",update+""+std_room_no.get(values)+" "+rollno.getText().toString());
                if (update && (std_roll_no.get(values).compareTo(Long.parseLong(rollno.getText().toString())) == 0)) {
//                    Log.d("ASD", "SSSS");
                    listenerComplaint.UpdateComplaint(datetoUpdate,
                           std_roll_no.get(values), std_room_no.get(values), complaintText.getText().toString(), currentDateandTime);
                }
            }
            update=false;
        });
    }

    public void UpdateRecord(String roll) {
        update=true;
        datetoUpdate=roll;
        std_roll_no=MainActivity.std_rollno;
        std_room_no=MainActivity.std_room_name;
//        Log.d("ASD", String.valueOf(update));
//        Log.d("ASD", String.valueOf(std_roll_no.size()));
    }

    public static ComplaintFragment newInstance(ArrayList<Long> roll_no, ArrayList<String> room_no) {
        ComplaintFragment fragment = new ComplaintFragment();
        fragment.std_roll_no = roll_no;
        fragment.std_room_no = room_no;
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IComplaintListener) {
            listenerComplaint = (IComplaintListener) context;
        }
    }

    interface IComplaintListener {

        void AddComplaint(long roll_no, String room_id, String complaint, String date_time);

        void UpdateComplaint(String position, long roll_no, String room_id, String complaint, String date_time);

    }

}