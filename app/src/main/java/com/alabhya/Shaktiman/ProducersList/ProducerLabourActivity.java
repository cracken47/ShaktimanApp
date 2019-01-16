package com.alabhya.Shaktiman.ProducersList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alabhya.Shaktiman.Adapters.ProducerLabourAdapter;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.Producers;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerLabourActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProducerLabourAdapter producerLabourAdapter;
    private List<Producers> producers;

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

        Button continueLabour = findViewById(R.id.continueLabour);
        continueLabour.setOnClickListener(continueLabourButtonListener);

        UserManagementService userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);

        Call<List<Producers>> call = userManagementService.getProducersLabours();

        call.enqueue(new Callback<List<Producers>>() {
            @Override
            public void onResponse(Call<List<Producers>> call, Response<List<Producers>> response) {
                producers = response.body();
                producerLabourAdapter = new ProducerLabourAdapter(producers,getApplicationContext());
                Log.d("Single",""+producers);
                recyclerView.setAdapter(producerLabourAdapter);
            }

            @Override
            public void onFailure(Call<List<Producers>> call, Throwable t) {

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
