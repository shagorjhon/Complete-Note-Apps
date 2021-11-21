package com.example.noteapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.input_show.InputActivity;
import com.example.input_show.ShowActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    RecyclerView recyclerView;
    Database database;
    FloatingActionButton floatingActionButton;
    Adapter adapter;
    boolean contextModel = false;
    Toolbar toolbar;
    TextView itemCounter;
    int counter = 0;
    ArrayList<ModelClass>selectionList=new ArrayList<>();
    ArrayList<ModelClass>arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerID);
        floatingActionButton = findViewById(R.id.fabID);
        toolbar=findViewById(R.id.toolbarID);
        itemCounter = findViewById(R.id.itemCounterID);
        arrayList = new ArrayList<>();

        setSupportActionBar(toolbar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        database = new Database(MainActivity.this);
        arrayList = database.getData();

        setAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String value = getIntent().getStringExtra("key");
        if (value!=null)
        {
            setAdapter();
        }
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            removeContext();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setAdapter();
        }
        else if (item.getItemId()==R.id.delete)
        {
            for (int i = 0; i<selectionList.size(); i++)
            {
                boolean delete = database.deleteData(selectionList.get(i));
                if (delete)
                {
                    Toast.makeText(this, "delete success !", Toast.LENGTH_SHORT).show();
                    this.recreate();
                }else {
                    Toast.makeText(this, "delete failed !", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                removeContext();
            }
        }
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void removeContext() {
        contextModel=false;
        toolbar.getMenu().clear();
        itemCounter.setVisibility(View.GONE);
        counter = 0;
        selectionList.clear();
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapter() {
            adapter = new Adapter(MainActivity.this, database.getData());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
    }

    private void addItem() {
        Intent intent  = new Intent(MainActivity.this, InputActivity.class);
        startActivity(intent);
    }

    public void itemClick(View v, int adapterPosition) {
        int i = adapterPosition;

        ModelClass modelClass = database.getData().get(adapterPosition);
            Intent intent=new Intent(MainActivity.this, ShowActivity.class);
            intent.putExtra("data",modelClass);
            intent.putExtra("position",i);
            startActivity(intent);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public boolean onLongClick(View v) {
        contextModel=true;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.context_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter.notifyDataSetChanged();
        itemCounter.setText("Select");
        itemCounter.setVisibility(View.VISIBLE);
        setTitle("");
        return true;
    }

    public void checkboxClick(View v, int adapterPosition) {
        if (((CheckBox)v).isChecked())
        {
            counter++;
            updateText();
            selectionList.add(arrayList.get(adapterPosition));
        }else {
            counter--;
            updateText();
            selectionList.remove(arrayList.get(adapterPosition));
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateText() {
        itemCounter.setText(counter+" Item selected");
    }
}