package com.alabhya.Shaktiman.ProducersList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


import com.alabhya.Shaktiman.Adapters.ProducerMasonAdapter;
import com.alabhya.Shaktiman.PlaceOrders.OrderPlaceActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.ProducerManagementService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.Producer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerMasonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProducerMasonAdapter producerMasonAdapter;
    private List<Producer> producers;
    private ProducerManagementService producerManagementService;
    private static final String DEFAULT = "00000000000000";
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Button continueProducer = findViewById(R.id.continueProducer);
        continueProducer.setOnClickListener(continueButtonListener);
        toolbar.setNavigationIcon(R.drawable.ic_sort_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle("Masons List");

        recyclerView = findViewById(R.id.producerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.activity_producer_mason_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        registerReceiver(broadcastReceiver, new IntentFilter("finish_mason_list_activity"));

        SharedPreferences sharedPreferences1 = getSharedPreferences("ConsumerLocationInfo",0);

        String state = sharedPreferences1.getString("stateId",DEFAULT);
        String city = sharedPreferences1.getString("cityId",DEFAULT);
        String locality = sharedPreferences1.getString("localityId",DEFAULT);

        getMason(state,city,locality);
    }



    private void getMason(String state, String city, String locality){
        producerManagementService = ApiClient.getRetrofitClient().create(ProducerManagementService.class);

        Call<List<Producer>> call = producerManagementService.getMason(state,city,locality);

        call.enqueue(new Callback<List<Producer>>() {
            @Override
            public void onResponse(Call<List<Producer>> call, Response<List<Producer>> response) {
                progressBar.setVisibility(View.GONE);
                producers = response.body();
                producerMasonAdapter = new ProducerMasonAdapter(producers,getApplicationContext());
                Log.d("Single",""+producers);
                recyclerView.setAdapter(producerMasonAdapter);
            }

            @Override
            public void onFailure(Call<List<Producer>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }


    View.OnClickListener continueButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),OrderPlaceActivity.class));
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action=="finish_mason_list_activity"){
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
