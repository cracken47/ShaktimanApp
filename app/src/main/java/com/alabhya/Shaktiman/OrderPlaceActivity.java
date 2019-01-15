package com.alabhya.Shaktiman;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.PlaceOrder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPlaceActivity extends AppCompatActivity {

    HashMap<String,String> hashMapLabour = new HashMap<>();
    HashMap<String,String> hashMapMason = new HashMap<>();
    HashMap<String,String> hashMapCombined = new HashMap<>();
    private String userId;
    OrderManagementService orderManagementService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);

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

        toolbar.setTitle("Place Order");

        SharedPreferences sharedPreferences = getSharedPreferences("ProducerQuantity",Context.MODE_PRIVATE);
        Log.d("Single",sharedPreferences.getString("masonId","null"));

        userId = sharedPreferences.getString("userId","");

        try {
            String[] masonId =  sharedPreferences.getString("masonId","null").split(",");
            String[] labourId = sharedPreferences.getString("labourId","null").split(",");

            for(int i=0;i<masonId.length;i++){
                hashMapMason.put("producersId["+i+1+"]",masonId[i]);
            }

            for(int i=0;i<labourId.length;i++){
                hashMapLabour.put("producersId["+i+1+"]",masonId[i]);
            }

            hashMapCombined.putAll(hashMapLabour);
            hashMapCombined.putAll(hashMapMason);


        }catch (Exception e){
            e.printStackTrace();
        }

        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);

        Call<PlaceOrder> placeOrderCall = orderManagementService.placeOrder(userId,"1","1","1",
                                    "some flat","some street","somephone",
                "Test Work","some work date","3","some area",
                "some landmark",hashMapCombined);

        placeOrderCall.enqueue(new Callback<PlaceOrder>() {
            @Override
            public void onResponse(Call<PlaceOrder> call, Response<PlaceOrder> response) {
                PlaceOrder placeOrder = response.body();
                Log.d("Single",placeOrder.getMessage()+" "+placeOrder.getStatus());

                Toast.makeText(getApplicationContext(),placeOrder.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PlaceOrder> call, Throwable t) {

            }
        });
    }
}
