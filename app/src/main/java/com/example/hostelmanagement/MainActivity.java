package com.example.hostelmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements StudentRegistrationFragment.IStudentCredentials,
        WardenRecyclerViewAdapter.IAcceptandRequestForStudentRecord, ComplaintFragment.IComplaintListener, RoomRecyclerViewAdapter.OnDeleteComplaint {


    enum FragmentState {
        ROOM,
        REGISTRATION,
        COMPLAINTS,
        WARDEN
    }

    enum FectchingState {
        index,
        Rooms,
        Hostels,
        Students,
        Warden,
        Complaint
    }


    private String getJson;


    private ArrayList<String> hostelname = new ArrayList<>();
    private ArrayList<Integer> hosteltype = new ArrayList<>();
    private ArrayList<String> roomName = new ArrayList<>();
    private ArrayList<String> complaintDetails = new ArrayList<>();
    public static ArrayList<Long> std_rollno = new ArrayList<>();
    private ArrayList<String> std_hostel_name = new ArrayList<>();
    public static ArrayList<String> std_room_name = new ArrayList<>();
    private ArrayList<String> std_name = new ArrayList<>();
    private ArrayList<String> std_prog = new ArrayList<>();


    private ArrayList<Long> std_rollno_w = new ArrayList<>();
    private ArrayList<String> std_hostel_name_w = new ArrayList<>();
    private ArrayList<String> std_room_name_w = new ArrayList<>();
    private ArrayList<String> std_name_w = new ArrayList<>();
    private ArrayList<String> std_prog_w = new ArrayList<>();
    private ArrayList<String> wardendataconcentenate = new ArrayList<>();

    public static ArrayList<Long> std_rollno_c = new ArrayList<>();
    private ArrayList<String> std_complain = new ArrayList<>();
    private ArrayList<String> std_room_name_c = new ArrayList<>();
    public static ArrayList<String> date_time = new ArrayList<>();
    private ArrayList<String> complaintdataconcetenate = new ArrayList<>();

    private Button room_btn;
    private Button registration_btn;
    private Button complaint_btn;
    private Button warden_btn;


    private FragmentState fragmentState;
    private FectchingState fectchingState;

    public static boolean create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EstablishingConnectionWithServer();
        References();
        Listeners();
    }

    void References() {
        room_btn = findViewById(R.id.room_btn);
        registration_btn = findViewById(R.id.registration_btn);
        complaint_btn = findViewById(R.id.complain_btn);
        warden_btn = findViewById(R.id.warden_btn);
        if(LoginPage.admin_L)
        {
            room_btn.setVisibility(View.INVISIBLE);
            registration_btn.setVisibility(View.INVISIBLE);
            complaint_btn.setVisibility(View.INVISIBLE);
            warden_btn.setVisibility(View.VISIBLE);
        }
        if(LoginPage.user_L)
        {
            room_btn.setVisibility(View.VISIBLE);
            registration_btn.setVisibility(View.VISIBLE);
            complaint_btn.setVisibility(View.VISIBLE);
            warden_btn.setVisibility(View.INVISIBLE);
        }
        fragmentState = FragmentState.ROOM;
        fectchingState = FectchingState.index;
//        complaintDetails.add("ASDDS1");
//        complaintDetails.add("ASDDS2");
//        complaintDetails.add("ASDDS3");
//        complaintDetails.add("ASDDS4");
//        complaintDetails.add("ASDDS");
//        complaintDetails.add("ASDDS");
//        complaintDetails.add("ASDDS");
//        complaintDetails.add("ASDDS");
//        complaintDetails.add("ASDDS");
        FragmentHandler();
    }

    void Listeners() {
        room_btn.setOnClickListener(v -> {
            fragmentState = FragmentState.ROOM;
            FragmentHandler();
//            InsertingRecord();
        });

        registration_btn.setOnClickListener(v -> {
            fragmentState = FragmentState.REGISTRATION;
            FragmentHandler();
        });

        complaint_btn.setOnClickListener(v -> {
            fragmentState = FragmentState.COMPLAINTS;
            FragmentHandler();
            create = true;
        });
        warden_btn.setOnClickListener(v -> {
            fragmentState = FragmentState.WARDEN;
            FragmentHandler();
        });

    }

    private void CloseFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment).commit();
//            Log.d("ASD", "Fragment is Close:");
        }
    }

    private void DisplayFragment(Fragment fragment) {
//        Log.d("ASD","Fragment is open");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment).commit();
    }

    void FragmentHandler() {

        RoomFragment roomFragment = RoomFragment.newInstance(complaintdataconcetenate);
        StudentRegistrationFragment registrationFragment = StudentRegistrationFragment.newInstance(hostelname, hosteltype, roomName);
        ComplaintFragment complaintFragment = ComplaintFragment.newInstance(std_rollno, std_room_name);
        WardenFragment wardenFragment = WardenFragment.newInstance(wardendataconcentenate);
//        Log.d("ASD", String.valueOf(wardendataconcentenate.size()));

        switch (fragmentState) {
            case ROOM:
                CloseFragment();
                DisplayFragment(roomFragment);
                break;
            case COMPLAINTS:
                CloseFragment();
                DisplayFragment(complaintFragment);
                break;
            case REGISTRATION:
                CloseFragment();
                DisplayFragment(registrationFragment);
                break;
            case WARDEN:
                CloseFragment();
                DisplayFragment(wardenFragment);
                break;
            default:
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EstablishingConnectionWithServer();
    }

    void EstablishingConnectionWithServer() {

        fectchingState = FectchingState.index;
        Requests(ConnectionServer.indexpage);
        fectchingState = FectchingState.Hostels;
        Requests(ConnectionServer.hostel);
        fectchingState = FectchingState.Rooms;
        Requests(ConnectionServer.room);
        fectchingState = FectchingState.Students;
        Requests(ConnectionServer.student);
        fectchingState = FectchingState.Warden;
        Requests(ConnectionServer.warden);
        fectchingState = FectchingState.Complaint;
        Requests(ConnectionServer.complaint);
    }

    void Requests(String request_name) {
        Request index = new Request.Builder().url(request_name).build();
        AsynchronousCall(index, fectchingState);
    }

    void AsynchronousCall(Request request, FectchingState fectchingState) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("Not Connected!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("Connected");
                getJson = response.body().string();
                switch (fectchingState) {
                    case index:
                        break;
                    case Hostels:
                        getHostelJsonArrayData();
                        break;
                    case Rooms:
                        getRoomJsonArrayData();
                        break;
                    case Students:
                        getStudentJsonArrayData();
                        break;
                    case Warden:
                        getStudentWardenJsonArrayData();
                        break;
                    case Complaint:
                        getStudentComplainJsonArrayData();
                        break;
                    default:
                }

            }
        });
    }


    //region ListsOFData

    void getHostelJsonArrayData() {
        int lengthOfArray = 0;
        try {
            JSONObject jsonObject = new JSONObject(getJson);
            //TODO:GET JSON HOSTEL NAME AND TYPE
            while (lengthOfArray < jsonObject.getJSONArray("Hostel").length()) {
                JSONObject jsonArray = (JSONObject) jsonObject.getJSONArray("Hostel").get(lengthOfArray);
                hostelname.add((String) jsonArray.get("hostel_name"));
                hosteltype.add((Integer) jsonArray.get("hostel_type"));
                lengthOfArray++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getRoomJsonArrayData() {
        int lengthOfArray = 0;
        try {
            JSONObject jsonObject = new JSONObject(getJson);
            //TODO:GET JSON HOSTEL NAME AND TYPE
            while (lengthOfArray < jsonObject.getJSONArray("Room").length()) {
                JSONObject jsonArray = (JSONObject) jsonObject.getJSONArray("Room").get(lengthOfArray);
                roomName.add((String) jsonArray.get("room_id"));
                lengthOfArray++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getStudentJsonArrayData() {
        int lengthOfArray = 0;
        try {
            JSONObject jsonObject = new JSONObject(getJson);
            //TODO:GET JSON HOSTEL NAME AND TYPE
            while (lengthOfArray < jsonObject.getJSONArray("Student").length()) {
                JSONObject jsonArray = (JSONObject) jsonObject.getJSONArray("Student").get(lengthOfArray);
                std_hostel_name.add((String) jsonArray.get("hostel_name"));
                std_rollno.add((Long) jsonArray.get("id"));
                std_name.add((String) jsonArray.get("name"));
                std_prog.add((String) jsonArray.get("prog"));
                std_room_name.add((String) jsonArray.get("room_id"));
                lengthOfArray++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getStudentWardenJsonArrayData() {
        int lengthOfArray = 0;
        try {
            JSONObject jsonObject = new JSONObject(getJson);
            //TODO:GET JSON HOSTEL NAME AND TYPE
            while (lengthOfArray < jsonObject.getJSONArray("HostelWarden").length()) {
                JSONObject jsonArray = (JSONObject) jsonObject.getJSONArray("HostelWarden").get(lengthOfArray);
//                String hn= (String) jsonArray.get("hostel_name");
//                Long id= (Long) jsonArray.get("id");
//                String prog= (String) jsonArray.get("prog");
//                String rid= (String) jsonArray.get("room_id");
//                String sn= (String) jsonArray.get("name");
                std_hostel_name_w.add((String) jsonArray.get("hostel_name"));
                std_rollno_w.add((Long) jsonArray.get("id"));
                std_name_w.add((String) jsonArray.get("name"));
                std_prog_w.add((String) jsonArray.get("prog"));
                std_room_name_w.add((String) jsonArray.get("room_id"));
                Log.d("ASD", std_hostel_name_w + "" + std_rollno_w);
                wardendataconcentenate.add(std_rollno_w.get(lengthOfArray) + "-" + std_name_w.get(lengthOfArray) + "-" + std_prog_w.get(lengthOfArray) + "-"
                        + std_hostel_name_w.get(lengthOfArray) + "-" + std_room_name_w.get(lengthOfArray));
                lengthOfArray++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getStudentComplainJsonArrayData() {
        int lengthOfArray = 0;
        try {
            JSONObject jsonObject = new JSONObject(getJson);
            //TODO:GET JSON HOSTEL NAME AND TYPE
            while (lengthOfArray < jsonObject.getJSONArray("Complaints").length()) {
                JSONObject jsonArray = (JSONObject) jsonObject.getJSONArray("Complaints").get(lengthOfArray);
                std_complain.add((String) jsonArray.get("complaint"));
                std_rollno_c.add((Long) jsonArray.get("sid"));
                std_room_name_c.add((String) jsonArray.get("srid"));
                date_time.add((String) jsonArray.get("dateTime"));

//                Log.d("ASD",std_hostel_name_w+""+std_rollno_w);
                complaintdataconcetenate.add(std_rollno_c.get(lengthOfArray) + "\n" + std_room_name_c.get(lengthOfArray) + "\n" +
                        date_time.get(lengthOfArray) + "\n" + std_complain.get(lengthOfArray));
                lengthOfArray++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //endregion


    @Override
    public void ADDStudentData(String name, long rollno, String hostelSelect, String roomSelect, String programSelect) {
//        Log.d("ASD", name + "/" + rollno + "/" + programSelect + "/" + roomSelect + "/" + hostelSelect);
        RequestBody formBody = new FormBody.Builder().add(Student.std_roll_no, String.valueOf(rollno)).add(Student.std_name, name)
                .add(Student.std_prog, programSelect).add(Student.std_room_id, roomSelect).add(Student.std_hostel_name, hostelSelect).build();
        AsynchronousPost(ConnectionServer.warden, formBody);

    }

    @Override
    public void AcceptStudentData(int position) {
        RequestBody formBody = new FormBody.Builder().add(Student.std_roll_no, String.valueOf(std_rollno_w.get(position))).
                add(Student.std_name, std_name_w.get(position)).add(Student.std_prog, std_prog_w.get(position))
                .add(Student.std_room_id, std_room_name_w.get(position)).add(Student.std_hostel_name, std_hostel_name_w.get(position)).build();
        AsynchronousPost(ConnectionServer.student, formBody);
        AsynchronousDelete(ConnectionServer.warden, position);

    }

    @Override
    public void DeclineStudentData(int position) {
        AsynchronousDelete(ConnectionServer.warden, position);
    }


    @Override
    public void AddComplaint(long roll_no, String room_id, String complaint, String date_time) {
        Log.d("ASD", roll_no + " " + room_id + " " + complaint + " " + date_time);
        RequestBody formBody = new FormBody.Builder().add(Student.std_roll_no, String.valueOf(roll_no)).
                add(Student.std_room_id, room_id).add(Student.std_complaint, complaint)
                .add(Student.date_Time, date_time).build();
        Log.d("ASD", "ADDCompl");
        AsynchronousPost(ConnectionServer.complaint, formBody);
    }

    @Override
    public void UpdateComplaint(String datetoUpdate, long roll_no, String room_id, String complaint, String date_time) {
//        Log.d("ASD",datetoUpdate+" "+roll_no+" "+room_id+" "+room_id+" "+complaint+" "+date_time);
//        Log.d("ASD",position+"----"+std_rollno_c.get(position)+" "+std_room_name_c.get(position)+" "+std_complain.get(position)+" "+this.date_time.get(position));
        AsynchronousDeleteComplaint(ConnectionServer.complaint, datetoUpdate);

        RequestBody formBody = new FormBody.Builder().add(Student.std_roll_no, String.valueOf(roll_no)).
                add(Student.std_room_id, room_id).add(Student.std_complaint, complaint)
                .add(Student.date_Time, date_time).build();
        AsynchronousPost(ConnectionServer.complaint, formBody);
    }

    @Override
    public void DeleteComplaint(String dateto) {
        AsynchronousDeleteComplaint(ConnectionServer.complaint, dateto);
    }

    void AsynchronousPost(String conectionurl, RequestBody formBody) {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(conectionurl).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("Not Connected!");
//                Log.d("ASD", e.getMessage() + " " + call.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("Connected");
            }
        });
    }

    void AsynchronousDelete(String conectionurl, int position) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(conectionurl + "/" + std_rollno_w.get(position)).delete().build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("Not Connected!");
//                Log.d("ASD", e.getMessage() + " " + call.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("Connected");

            }
        });
    }

    void AsynchronousDeleteComplaint(String conectionurl, String dateto) {
        Log.d("ASD", dateto);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(conectionurl + "/" + dateto).delete().build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("Not Connected!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("Connected");
            }
        });
    }

}