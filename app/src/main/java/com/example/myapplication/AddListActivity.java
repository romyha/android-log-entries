package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;
import java.util.TreeMap;

public class AddListActivity extends AppCompatActivity {
    private String listName;
    private EditText listNameText;
    private Button showListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            getSupportActionBar().setTitle("Create Shopping List");
        } else {
            actionBar.setTitle("Create Shopping List");
        }

        final Button saveListButton = findViewById(R.id.saveListButton);
        showListButton = findViewById(R.id.showListButton);
        listNameText = findViewById(R.id.listName);
        listNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveListButton.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        boolean listExists = false;
        for (Map.Entry<String, ?> l : lists.entrySet()) {
            if (listName.equals(l.getValue())) {
                listExists = true;
                break;
            }
        }
        if (!listExists) {
            TreeMap<String, ?> sortedLists = new TreeMap<>(lists);

            SharedPreferences.Editor editor = listsPreferences.edit();
            int lastKey = sortedLists.size() > 0 ? Integer.parseInt(sortedLists.lastKey()) : 0;
            int nextId = lastKey + 1;
            editor.putString(Integer.toString(nextId), listName);
            editor.commit();

            for (Map.Entry<String, ?> list : sortedLists.entrySet()) {
                Log.i("liiiiists", list.getValue().toString());
            }
            showListButton.setEnabled(true);
        } else {
            Toast.makeText(this, listName + " already exists", Toast.LENGTH_SHORT).show();
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
