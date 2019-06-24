package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 1;
    ArrayList<String> listItems=new ArrayList<String>();
    //ArrayAdapter<String> adapter;
    ShoppingListsAdapter adapter;
    private ListView list;
    public Map<String, ?> entries;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onRestart()
    {  // After a pause OR at startup
        super.onRestart();
        buildList();
    }

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

        sharedPreferences = getSharedPreferences("LISTS", MODE_PRIVATE);
        entries = sharedPreferences.getAll();

        welcome.setSingleLine(false);
        welcome.setText("Hi YOU! Here's what you need to get:");

        buildList();
    }

    public void buildList() {
        list = findViewById(R.id.shoppingLists);

        listItems.clear();
        entries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            listItems.add(entry.getValue().toString());
        }
        adapter = new ShoppingListsAdapter(listItems, this, sharedPreferences);
        list.setAdapter(adapter);

        final Intent viewListIntent = new Intent(this, ViewListActivity.class);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                viewListIntent.putExtra("listName", selectedItem);
                startActivity(viewListIntent);
            }
        });}

    public void openAddListActivity(View view) {
        Intent intent = new Intent(this, AddListActivity.class);
        startActivity(intent);
    }
}
