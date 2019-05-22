package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button serverTransmitButton;
    private Button clientReceiveButton;
    private Button serverUDPButton;
    private Button clientUDPButton;
    private int PICKFILE_REQUEST_CODE = 100;
    private String filePath="";
    private String wholePath="";
    private Button changeName;
    private String m_Text = "";
    private int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE},
                    ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }

        TextView welcome = findViewById(R.id.editText2);
        TextView lastLog = findViewById(R.id.textView4);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGS", MODE_PRIVATE);
        String lastSavedLog = (sharedPreferences.getString("1", ""));

        lastLog.setText("Last " + lastSavedLog);
        welcome.setSingleLine(false);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format( new Date()   );
        welcome.setText("Hi Hatem, nice to meet you!\nToday is the " + dateString + "\nThis was your last log:");

        // TCP
        serverTransmitButton = (Button) findViewById(R.id.button_TCP_server);
        serverTransmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Start Server Clicked", "yipee");

                new openServerTask().execute();
                Log.i("socketConnection", "JAY, we got a connection!");

                //open a file manager to let user choose desired file.
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        clientReceiveButton = (Button) findViewById(R.id.button_TCP_client);
        clientReceiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.i("Read Button Clicked", "yipee");
                /**
                 * Create a client socket with the host,
                 * port, and timeout information.
                 */
                new ConnectTask().execute();
            }
        });
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
