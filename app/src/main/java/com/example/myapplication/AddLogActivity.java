package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Map;
import java.util.TreeMap;

public class AddLogActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);


    }

    /** Called when the user taps the addLog button */
    public void addLog(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ViewLogsActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        //EditText idEdit = (EditText) findViewById(R.id.editText3);
        String message = editText.getText().toString();
        //String id = idEdit.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGS", MODE_PRIVATE);
        Map<String, ?> entries = sharedPreferences.getAll();
        int size = entries.size();
        TreeMap<String, ?> sortedEntries = new TreeMap<>(entries);
        Log.i("logid", Integer.toString(size+1));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int nextId = Integer.parseInt(sortedEntries.lastKey()) + 1;
        editor.putString(Integer.toString(nextId), message);
        editor.commit();

        startActivity(intent);
    }
}
