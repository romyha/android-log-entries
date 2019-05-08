package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Collection;
import java.util.Map;

public class ViewLogsActivity extends AppCompatActivity {
    public Collection<?> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(AddLogActivity.EXTRA_MESSAGE);
        SharedPreferences sharedPreferences = getSharedPreferences("LOGS", MODE_PRIVATE);
        entries = sharedPreferences.getAll().values();
        String logs = "";
        for (Object log : entries) {
            logs += log.toString() + "\n";
            //Log.d("VALUE", s.toString());
        }

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView2);
        textView.setText(logs);
    }
}
