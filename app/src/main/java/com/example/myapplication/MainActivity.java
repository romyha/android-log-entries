package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView welcome = findViewById(R.id.editText2);
        TextView lastLog = findViewById(R.id.textView4);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGS", MODE_PRIVATE);
        String lastSavedLog = (sharedPreferences.getString("1", ""));

        lastLog.setText("Last " + lastSavedLog);
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
