package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AddLogActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);


    }

    /** Called when the user taps the Send button */
    public void addLog(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ViewLogsActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText idEdit = (EditText) findViewById(R.id.editText3);
        String message = editText.getText().toString();
        String id = idEdit.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGS", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(id, message);
        editor.commit();

        startActivity(intent);
    }
}
