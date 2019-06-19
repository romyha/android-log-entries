package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import java.util.TreeMap;

public class ViewListActivity extends AppCompatActivity {
    public static final String TAG = "";
    public Map<String, ?> entries;
    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;
    private String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.activity_view_logs);
        final LinearLayout viewLogsView = findViewById(R.id.logsLayout);
        viewLogsView.setOrientation(LinearLayout.VERTICAL);

                // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        listName = intent.getStringExtra("listName");
        final SharedPreferences sharedPreferences = getSharedPreferences(listName, MODE_PRIVATE);
        entries = sharedPreferences.getAll();
        TreeMap<String, ?> sortedEntries = new TreeMap<>(entries);

        for (Map.Entry<String, ?> entry : sortedEntries.entrySet()) {
            final LinearLayout logLayout = new LinearLayout(this);
            viewLogsView.addView(logLayout);
            logLayout.setId((Integer.parseInt(entry.getKey())));
            logLayout.setLayoutParams(viewParams);
            logLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 20, 30, 0);
            Button logRemoveButton = new Button(this);
            logRemoveButton.setId(Integer.parseInt(entry.getKey()));
            logRemoveButton.setText("X");
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(60, 60);
            buttonParams.setMargins(10, 20, 30, 0);
            logRemoveButton.setLayoutParams(buttonParams);
            //logRemoveButton.getLayoutParams().width = 200;

            final TextView logTextView = new TextView(this);
            logTextView.setLayoutParams(params);

            logTextView.setId(Integer.parseInt(entry.getKey()));
            logTextView.setText(entry.getKey() + " " + entry.getValue().toString());
            logTextView.getLayoutParams().width = 400;

            Drawable roundDrawable = getResources().getDrawable(R.drawable.round_button);
            roundDrawable.setColorFilter(Color.rgb(153, 10, 0), PorterDuff.Mode.SRC_ATOP);

            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                logRemoveButton.setBackgroundDrawable(roundDrawable);
            } else {
                logRemoveButton.setBackground(roundDrawable);
            }
            logRemoveButton.setTextColor(Color.WHITE);

            //logRemoveButton.setBackgroundColor(Color.RED);
            //logRemoveButton.invalidate();
            final int id_ = logRemoveButton.getId();
            logLayout.addView(logTextView);
            logLayout.addView(logRemoveButton);

            logRemoveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(Integer.toString(id_));
                    editor.commit();
                    viewLogsView.removeView(logLayout);
                    Toast.makeText(view.getContext(),
                            "Removed item " + id_, Toast.LENGTH_SHORT)
                            .show();
                }
            });

        }
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
                        SharedPreferences sharedPreferences = getSharedPreferences(listName, MODE_PRIVATE);
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
