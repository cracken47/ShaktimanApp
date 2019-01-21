package com.alabhya.Shaktiman.ProducersList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.alabhya.Shaktiman.Adapters.ProducerLabourAdapter;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.ProducerManagementService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.Producer;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerLabourActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProducerLabourAdapter producerLabourAdapter;
    private List<Producer> producers;
    private ProgressBar progressBar;
    private static final String DEFAULT = "00000000000000";
    private ProducerManagementService producerManagementService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_labour);
        int labourQuantity = getIntent().getIntExtra("labourQuantity", 0);
        int masonQuantity = getIntent().getIntExtra("masonQuantity", 0);

        SharedPreferences sharedPreferences = getSharedPreferences("ProducerQuantity",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("labourQuantity", labourQuantity);
        editor.putInt("masonQuantity", masonQuantity);
        editor.apply();

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        toolbar.setTitle("Labours List");

        registerReceiver(broadcastReceiver, new IntentFilter("finish_labour_list_activity"));

        recyclerView = findViewById(R.id.producerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.producer_labour_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Button continueLabour = findViewById(R.id.continueLabour);
        continueLabour.setOnClickListener(continueLabourButtonListener);

        SharedPreferences sharedPreferences1 = getSharedPreferences("ConsumerLocationInfo",0);

        String state = sharedPreferences1.getString("stateId",DEFAULT);
        String city = sharedPreferences1.getString("cityId",DEFAULT);
        String locality = sharedPreferences1.getString("localityId",DEFAULT);

        getLabour(state,city,locality);
    }

    private void getLabour(String state, String city, String locality){
        producerManagementService = ApiClient.getRetrofitClient().create(ProducerManagementService.class);

        Call<List<Producer>> call = producerManagementService.getLabour(state,city,locality);

        call.enqueue(new Callback<List<Producer>>() {
            @Override
            public void onResponse(Call<List<Producer>> call, Response<List<Producer>> response) {
                progressBar.setVisibility(View.GONE);
                producers = response.body();
                producerLabourAdapter = new ProducerLabourAdapter(producers,getApplicationContext());
                Log.d("Single",""+producers);
                recyclerView.setAdapter(producerLabourAdapter);
            }

            @Override
            public void onFailure(Call<List<Producer>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    View.OnClickListener continueLabourButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),ProducerMasonActivity.class);
            startActivity(intent);
        }
    };

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action=="finish_labour_list_activity"){
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
