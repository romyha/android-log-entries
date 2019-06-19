package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 1;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }

        TextView welcome = findViewById(R.id.editText2);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGS", MODE_PRIVATE);

        welcome.setSingleLine(false);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format( new Date()   );
        welcome.setText("Hi Hatem, nice to meet you!\nToday is the " + dateString + "\nThis was your last log:");
    }

    public void openAddLogActivity(View view) {
        Intent intent = new Intent(this, AddLogActivity.class);
        startActivity(intent);
    }

    public void openViewLogsActivity(View view) {
        Intent intent = new Intent(this, ViewLogsActivity.class);
        startActivity(intent);
    }
}
