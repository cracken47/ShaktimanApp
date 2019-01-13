package com.alabhya.Shaktiman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.alabhya.Shaktiman.Adapters.ProducerAdapter;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.ApiInterface;
import com.alabhya.Shaktiman.models.Producers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProducerAdapter producerAdapter;
    private List<Producers> producers;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.drawable.ic_sort_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle("Producers List");

        recyclerView = findViewById(R.id.producerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getRetrofitClient().create(ApiInterface.class);

        Call<List<Producers>> call = apiInterface.getProducers();

        call.enqueue(new Callback<List<Producers>>() {
            @Override
            public void onResponse(Call<List<Producers>> call, Response<List<Producers>> response) {
                producers = response.body();
                producerAdapter = new ProducerAdapter(producers);
                recyclerView.setAdapter(producerAdapter);
            }

            @Override
            public void onFailure(Call<List<Producers>> call, Throwable t) {

            }
        });
    }
}