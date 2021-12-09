package com.example.wrapper.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wrapper.Adapter.SyncHistoryAdapter;
import com.example.wrapper.Models.Pojo.SyncedDataList;
import com.example.wrapper.R;
import com.example.wrapper.ViewModel.SyncDataLogViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Md.harun or rashid on 09,December,2021
 * BABL, Bangladesh,
 */


public class SyncDataLogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SyncDataLogViewModel syncDataLogViewModel;
    private ArrayList<String> upTableNames;
    private SyncHistoryAdapter adapter;
    private ArrayList<SyncedDataList> arrayList;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_data_log);

        setupToolbar();
        initView();
        initVariables();

        upTableNames=syncDataLogViewModel.getSqliteUpTableNames(SyncDataLogActivity.this);
        arrayList = syncDataLogViewModel.getSyncStatusData(upTableNames,SyncDataLogActivity.this);

        Log.e("tableNames",new Gson().toJson(arrayList));

        adapter = new SyncHistoryAdapter(getApplicationContext(),arrayList,SyncDataLogActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }


    private void initVariables() {
        syncDataLogViewModel = ViewModelProviders.of(SyncDataLogActivity.this).get(SyncDataLogViewModel.class);
        upTableNames = new ArrayList<>();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclearView);

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.SyncDataLogActivity_toolbar);
        toolbar.setTitle("Sync History");
        setSupportActionBar(toolbar);
    }
}
