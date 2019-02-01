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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alabhya.Shaktiman.Adapters.ProducerLabourAdapter;
import com.alabhya.Shaktiman.PlaceOrders.OrderPlaceActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.ProducerManagementService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.Location;
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
    private int labourQuantity;
    private int masonQuantity;
    private SharedPreferences sharedPreferences;
    private UserManagementService userManagementService;
    private List<Location> localities;
    private List<Location> cities;
    private Button continueLabour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_labour);
        labourQuantity = getIntent().getIntExtra("labourQuantity", 0);
        masonQuantity = getIntent().getIntExtra("masonQuantity", 0);

        sharedPreferences = getSharedPreferences("ProducerQuantity",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("labourQuantity", labourQuantity);
        editor.putInt("masonQuantity", masonQuantity);
        editor.apply();

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);
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

        continueLabour = findViewById(R.id.continueLabour);
        continueLabour.setEnabled(false);
        continueLabour.setOnClickListener(continueLabourButtonListener);

        SharedPreferences sharedPreferences1 = getSharedPreferences("ConsumerLocationInfo",0);

        String state = sharedPreferences1.getString("stateId",DEFAULT);
        String city = sharedPreferences1.getString("cityId",DEFAULT);
        String locality = sharedPreferences1.getString("localityId",DEFAULT);

        Log.d("Single","Ids"+state+city+locality);
        getLabour(state,city,locality);
    }

    private void getLabour(String state, String city, String locality){
        producerManagementService = ApiClient.getRetrofitClient().create(ProducerManagementService.class);

        Call<List<Producer>> call = producerManagementService.getLabour(state,city,locality);

        call.enqueue(new Callback<List<Producer>>() {
            @Override
            public void onResponse(Call<List<Producer>> call, Response<List<Producer>> response) {
                producers = response.body();
                if (producers.size()==0 && masonQuantity!=0){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isEmpty",true);
                    editor.apply();
                    continueLabour.setEnabled(true);
                }
                if (producers.size()!=0){
                    continueLabour.setEnabled(true);
                    getLocalities();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProducerLabourActivity.this, "Sorry! No Labours found for current location!", Toast.LENGTH_SHORT).show();
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
                producerLabourAdapter = new ProducerLabourAdapter(producers,localities,cities,getApplicationContext());
                Log.d("Single","locale size"+localities.size());
                recyclerView.setAdapter(producerLabourAdapter);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }

    View.OnClickListener continueLabourButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isAllSelected = sharedPreferences.getBoolean("isAllSelected",false);

            Log.d("Single",""+isAllSelected);

            if (!isAllSelected && !(labourQuantity==0) && producers.size()!=0){
                Toast.makeText(ProducerLabourActivity.this, "Please Choose Labours", Toast.LENGTH_SHORT).show();
                return;
            }
            if(masonQuantity == 0){
                Intent intent = new Intent(getApplicationContext(), OrderPlaceActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(),ProducerMasonActivity.class);
                startActivity(intent);
            }
        }
    };


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
        Log.d("Single","On destroy called baby");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAllSelected",false);
        editor.clear();
        editor.apply();
        super.onDestroy();
    }
}
