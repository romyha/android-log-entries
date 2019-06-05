package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ViewLogsActivity extends AppCompatActivity {
    public static final String TAG = "";
    public Map<String, ?> entries;
    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);
        LinearLayout viewLogsView = findViewById(R.id.logsLayout);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(AddLogActivity.EXTRA_MESSAGE);
        final SharedPreferences sharedPreferences = getSharedPreferences("LOGS", MODE_PRIVATE);
        entries = sharedPreferences.getAll();

        String logs = "";

        String[] logsArray = new String[entries.size()];
        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            logsArray[Integer.parseInt(entry.getKey())-1] = entry.getValue().toString();
            //logs += entry.getKey() + " " + entry.getValue().toString() + "\n";
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }

        // TODO:
        // find a way to sort entries Map without this array, because deleting an entry will cause
        // ArrayIndexOutOfBounds Exception
        int i = 1;
        for (String log : logsArray) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            logs += Integer.toString(i) + " " + log + "\n";
            i++;
            Button logRemoveButton = new Button(this);
            TextView logTextView = new TextView(this);
            logTextView.setId(i-1);
            logTextView.setText(log);
            logRemoveButton.setId(i-1);
            logRemoveButton.setText("Remove " + (i-1));
            final int id_ = logRemoveButton.getId();
            viewLogsView.addView(logTextView);
            viewLogsView.addView(logRemoveButton);
            logRemoveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(Integer.toString(id_));
                    editor.commit();
                    Toast.makeText(view.getContext(),
                            "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            .show();
                }
            });

        }

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView2);
        textView.setText(logs);
    }

    public void sendViaBluetooth(View v) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
        } else {
            enableBluetooth();
        }
    }

    public void enableBluetooth() {
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Collection<?> entries;
        Log.i("activityResult", Integer.toString(requestCode));
        if (resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("*/*");

            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "test.txt");
            try {
                f.createNewFile();
                if(f.exists())
                {
                    try (FileWriter writer = new FileWriter(f.getPath());
                         BufferedWriter bw = new BufferedWriter(writer)) {
                        SharedPreferences sharedPreferences = getSharedPreferences("LOGS", MODE_PRIVATE);
                        entries = sharedPreferences.getAll().values();

                        if (entries.isEmpty()) {
                            Toast.makeText(this, "No entries found to send.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String logs = "";
                        for (Object log : entries) {
                            bw.write(log.toString());
                            bw.newLine();
                            //Log.d("VALUE", s.toString());
                        }
                        Log.i("file", Arrays.toString(entries.toArray()));
                        bw.write(logs);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //File f = new File(file.toUri());
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", f));

            PackageManager pm = getPackageManager();
            List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);

            if (appsList.size() > 0) {
                String packageName = null;
                String className = null;
                boolean found = false;

                for (ResolveInfo info : appsList) {
                    packageName = info.activityInfo.packageName;
                    if (packageName.equals("com.android.bluetooth")) {
                        className = info.activityInfo.name;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(this, "Bluetooth havn't been found",
                            Toast.LENGTH_LONG).show();
                } else {
                    intent.setClassName(packageName, className);
                    startActivity(intent);
                }
            }
        } else {
            Toast.makeText(this, "Bluetooth is cancelled", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
