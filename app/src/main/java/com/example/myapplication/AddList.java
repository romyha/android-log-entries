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

public class AddList extends AppCompatActivity {
    private String listName;
    private EditText listNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        listNameText = findViewById(R.id.listName);
    }

    public void openShowListActivity(View view) {
        Intent intent = new Intent(this, ViewListActivity.class);

        intent.putExtra("listName", listName);
        startActivity(intent);
    }

    public void saveList(View view) {
        listName = listNameText.getText().toString();
        SharedPreferences listsPreferences = getSharedPreferences("LISTS", MODE_PRIVATE);
        Map<String, ?> lists = listsPreferences.getAll();
        TreeMap<String, ?> sortedLists = new TreeMap<>(lists);

        SharedPreferences.Editor editor = listsPreferences.edit();
        int lastKey = sortedLists.size() > 0 ? Integer.parseInt(sortedLists.lastKey()) : 0;
        int nextId = lastKey + 1;
        editor.putString(Integer.toString(nextId), listName);
        editor.commit();

        for (Map.Entry<String, ?> list : sortedLists.entrySet()) {
            Log.i("liiiiists", list.getValue().toString());
        }
    }

    public void addItem(View view) {
        EditText itemText = findViewById(R.id.item);
        String item = itemText.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences(listName, MODE_PRIVATE);
        Map<String, ?> items = sharedPreferences.getAll();
        TreeMap<String, ?> sortedItems = new TreeMap<>(items);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        int lastKey = sortedItems.size() > 0 ? Integer.parseInt(sortedItems.lastKey()) : 0;
        int nextId = lastKey + 1;
        editor.putString(Integer.toString(nextId), item);
        editor.commit();

        itemText.setText("");
    }
}
