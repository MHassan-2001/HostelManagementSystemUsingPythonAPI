package com.example.hostelmanagement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class StudentRegistrationFragment extends Fragment {

    private Spinner hostelNameSpinner;
    private Spinner roomNameSpinner;
    private Spinner programSpinner;

    private DatePicker datePicker;

    private TextView hostelType;
    private TextView hostelName;

    private Button submitBtn;


    private EditText name;
    private EditText rollNo;
    private RadioGroup radioGroup;

    private String[] programOffering = {"BS-CS", "MS-CS", "BBA", "Economics", "BS-Physics", "BS-Math"};
    //    private String[] roomsName = {"BS-CS", "MS-CS", "BBA", "Economics", "BS-Physics", "BS-Math"};
//    private String[] hostelsName = {"BS-CS", "MS-CS", "BBA", "Economics", "BS-Physics", "BS-Math"};
    private String programSelect;
    private String hostelSelect;
    private String roomSelect;
    private int gender;
    private final int MALE = 0;
    private final int FEMALE = 1;

    public static boolean handler = false;

    private IStudentCredentials studentCredentials;

    private List<String> roomsName = new ArrayList<>();
    private List<String> hostelsName = new ArrayList<>();
    private ArrayList<Integer> hostelsType = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_registration_fragment, container, false);
        Referencing(view);
        AssignValue(view);
        return view;
    }


    void Referencing(View view) {
        rollNo = view.findViewById(R.id.roll_no_Et);
        name = view.findViewById(R.id.user_name);
        hostelNameSpinner = view.findViewById(R.id.hostel_name);
        roomNameSpinner = view.findViewById(R.id.room_no);
        programSpinner = view.findViewById(R.id.select_prog);
        datePicker = view.findViewById(R.id.DatePicker_id);
        hostelName = view.findViewById(R.id.hostel_name_tv);
        hostelType = view.findViewById(R.id.hostel_type_tv);
        submitBtn = view.findViewById(R.id.submit_btn);
        radioGroup = view.findViewById(R.id.radio_group);
//        roomsName.add("ASD");
//        roomsName.add("ASD");
//        roomsName.add("ASD");
//        roomsName.add("ASD");
//        roomsName.add("ASD");
//        hostelsName.add("ASD!");
//        hostelsName.add("ASD!");
//        hostelsName.add("ASD!");
//        hostelsName.add("ASD!");
//        hostelsName.add("ASD!");
    }

    void AssignValue(View view) {


        ArrayAdapter roomAvailable = new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, roomsName);
        roomNameSpinner.setAdapter(roomAvailable);

        roomNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roomSelect = roomsName.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ArrayAdapter hostelAvailable = new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, hostelsName);
        hostelNameSpinner.setAdapter(hostelAvailable);

        ArrayAdapter programAvailable = new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, programOffering);
        programSpinner.setAdapter(programAvailable);

        programSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                programSelect = programOffering[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hostelNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type_int = String.valueOf(hostelsType.get(position));
                String type_str = type_int.equals("0") ? "Boys" : "Girls";
                hostelName.setText(hostelsName.get(position));
                hostelType.setText(type_str);
                hostelSelect = hostelsName.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            View radioBtn = view.findViewById(checkedId);
            int index = radioGroup.indexOfChild(radioBtn);
            switch (index) {
                case MALE:
                    gender = 0;
                    break;
                case FEMALE:
                    gender = 1;
                    break;
                default:

            }
        });

        submitBtn.setOnClickListener(v -> {
//            Log.d("ASD", "BtnClicked");

            GetDataFromInputFields();
        });

    }

    private void GetDataFromInputFields() {
        if (!name.getText().toString().isEmpty() && !rollNo.getText().toString().isEmpty()) {
            studentCredentials.ADDStudentData(name.getText().toString(), Long.parseLong(rollNo.getText().toString()),
                    hostelSelect, roomSelect, programSelect);
        } else {
            Toast.makeText(getContext(), "Please submit all Attributes", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context != null)
            studentCredentials = (IStudentCredentials) context;
    }


    public static StudentRegistrationFragment newInstance(ArrayList<String> hostelsName, ArrayList<Integer> hostelsType, ArrayList<String> roomName) {
        StudentRegistrationFragment fragment = new StudentRegistrationFragment();
        fragment.hostelsName=hostelsName;
        fragment.hostelsType=hostelsType;
        fragment.roomsName=roomName;
        return fragment;
    }

    interface IStudentCredentials {
        void ADDStudentData(String name, long rollno, String hostelSelect, String roomSelect, String programSelect);
    }
}
