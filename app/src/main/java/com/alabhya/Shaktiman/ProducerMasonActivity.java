package com.alabhya.Shaktiman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.alabhya.Shaktiman.Adapters.ProducerMasonAdapter;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.Producers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerMasonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProducerMasonAdapter producerMasonAdapter;
    private List<Producers> producers;
    private UserManagementService userManagementService;
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

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);

        Call<List<Producers>> call = userManagementService.getProducersMasons();

        call.enqueue(new Callback<List<Producers>>() {
            @Override
            public void onResponse(Call<List<Producers>> call, Response<List<Producers>> response) {
                producers = response.body();
                producerMasonAdapter = new ProducerMasonAdapter(producers,getApplicationContext());
                Log.d("Single",""+producers);
                recyclerView.setAdapter(producerMasonAdapter);
            }

            @Override
            public void onFailure(Call<List<Producers>> call, Throwable t) {

            }
        });
    }

    View.OnClickListener continueButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),OrderPlaceActivity.class));
        }
    };
}
