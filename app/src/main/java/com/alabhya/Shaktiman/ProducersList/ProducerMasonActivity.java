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
import android.widget.Toast;


import com.alabhya.Shaktiman.Adapters.ProducerLabourAdapter;
import com.alabhya.Shaktiman.Adapters.ProducerMasonAdapter;
import com.alabhya.Shaktiman.ConsumerMainView.ConsumerHomeActivity;
import com.alabhya.Shaktiman.PlaceOrders.OrderPlaceActivity;
import com.alabhya.Shaktiman.ProducerMainView.ProducerHomeActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.ProducerManagementService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.Location;
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
    private int masonQuantity;
    private Button continueProducer;
    private UserManagementService userManagementService;
    List<Location> cities;
    List<Location> localities;
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

        continueProducer = findViewById(R.id.continueProducer);
        continueProducer.setEnabled(false);
        continueProducer.setOnClickListener(continueButtonListener);
        toolbar.setNavigationIcon(R.drawable.ic_sort_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle("Masons List");

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);
        recyclerView = findViewById(R.id.producerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.activity_producer_mason_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        registerReceiver(broadcastReceiver, new IntentFilter("finish_mason_list_activity"));

        SharedPreferences sharedPreferences = getSharedPreferences("ProducerQuantity",Context.MODE_PRIVATE);
        int labourQuantity = sharedPreferences.getInt("labourQuantity",0);

        if (labourQuantity == 0){
            masonQuantity = getIntent().getIntExtra("masonQuantity", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("masonQuantity", masonQuantity);
            editor.apply();
        }


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
                producers = response.body();
                continueProducer.setEnabled(true);

                if (producers.size()!=0){
                    getLocalities();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProducerMasonActivity.this, "Sorry! No masons found for current location!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producer>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    /**
     * A function to get available Localities from the database for spinners data.
     *
     * @throws ArrayIndexOutOfBoundsException While getting localityId
     */

    private void getLocalities() {
        final Call<List<Location>> locality = userManagementService.getLocalities();
        locality.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                localities = response.body();
                getCities();
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }

    private void getCities() {
        final Call<List<Location>> city = userManagementService.getCities();
        city.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                progressBar.setVisibility(View.GONE);
                cities = response.body();
                producerMasonAdapter = new ProducerMasonAdapter(producers,localities,cities,getApplicationContext());
                Log.d("Single","locale size"+localities.size());
                recyclerView.setAdapter(producerMasonAdapter);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }


    View.OnClickListener continueButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ProducerQuantity",0);


            boolean isEmpty = sharedPreferences.getBoolean("isEmpty",true);

            if (isEmpty && producers.size()==0){
                Toast.makeText(ProducerMasonActivity.this, "Sorry! No Producers Found for your location!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ConsumerHomeActivity.class));
                Intent intent = new Intent("finish_labour_list_activity");
                Intent intent1 = new Intent("finish_consumer_home_activity");
                sendBroadcast(intent);
                sendBroadcast(intent1);
                finish();
            }else {
                masonQuantity = sharedPreferences.getInt("masonQuantity",100000);
                boolean isAllMasonSelected = sharedPreferences.getBoolean("isAllMasonSelected",false);
                if (!isAllMasonSelected && !(masonQuantity==0)){
                    Toast.makeText(ProducerMasonActivity.this, "Please Choose Masons", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(getApplicationContext(),OrderPlaceActivity.class));
            }
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
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ProducerQuantity",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAllMasonSelected",false);
        editor.clear();
        editor.apply();
        super.onDestroy();
    }
}
