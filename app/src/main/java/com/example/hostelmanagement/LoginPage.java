package com.example.hostelmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginPage extends AppCompatActivity {

    public enum LoginState {
        NONE,
        USER,
        ADMIN
    }
    EditText username;
    EditText password;

    private ArrayList<String> user_a = new ArrayList<>();
    private ArrayList<String> pass_a = new ArrayList<>();

    private ArrayList<String> user = new ArrayList<>();
    private ArrayList<String> pass = new ArrayList<>();

    LoginState loginState=LoginState.ADMIN;

    String getJson;

    public static boolean admin_L = false;
    public static boolean user_L = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        References();

    }

    void References() {

        username = findViewById(R.id.user_name);
        password = findViewById(R.id.password);

        loginState = LoginState.USER;
        Requests(ConnectionServer.user);


        loginState = LoginState.ADMIN;
        Requests(ConnectionServer.admin);


    }

    void Requests(String request_name) {
        Request index = new Request.Builder().url(request_name).build();
        Call(index, loginState);
    }



    public void Login(View view) {

        Admin();
        User();
    }

    private void User() {
        for (int i = 0; i < user.size(); i++) {
            if (user.get(i).equals(username.getText().toString()) && pass.get(i).equals(password.getText().toString())) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startActivity(intent);
                user_L = true;
            }
        }
    }

    public void Admin() {
        for (int i = 0; i < user_a.size(); i++) {
            if (user_a.get(i).equals(username.getText().toString()) && pass_a.get(i).equals(password.getText().toString())) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startActivity(intent);
                admin_L = true;
            }
        }
    }

    public void Call(Request request, LoginState loginState) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                TextView textView = findViewById(R.id.text_view);
//                textView.setText("Not Connected!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                TextView textView = findViewById(R.id.text_view);
//                textView.setText("Connected");
                getJson = response.body().string();
//                Log.d("ASD",getJson);
                switch (loginState) {
                    case NONE:
                        break;
                    case ADMIN:
                        Log.d("ASD","Admin");
                        getAdminJsonArrayData();
                        break;
                    case USER:
                        Log.d("ASD","USer");
                        getUserJsonArrayData();
                        break;
                    default:
                }
            }
        });
    }

    void getAdminJsonArrayData() {
        int lengthOfArray = 0;
        try {
            JSONObject jsonObject = new JSONObject(getJson);
            //TODO:GET JSON HOSTEL NAME AND TYPE
            while (lengthOfArray < jsonObject.getJSONArray("Admin").length()) {
                JSONObject jsonArray = (JSONObject) jsonObject.getJSONArray("Admin").get(lengthOfArray);

                user_a.add((String) jsonArray.get("user"));
                pass_a.add((String) jsonArray.get("pass"));

                lengthOfArray++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getUserJsonArrayData() {
        int lengthOfArray = 0;
        try {
            JSONObject jsonObject = new JSONObject(getJson);

            //TODO:GET JSON HOSTEL NAME AND TYPE
            while (lengthOfArray < jsonObject.getJSONArray("User").length()) {
                JSONObject jsonArray = (JSONObject) jsonObject.getJSONArray("User").get(lengthOfArray);
                user.add((String) jsonArray.get("user"));
                pass.add((String) jsonArray.get("pass"));
                lengthOfArray++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}