package com.alabhya.Shaktiman.ProducerMainView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alabhya.Shaktiman.Adapters.ProducerActiveOrderDetailsAdapter;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDataProducer;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDetailsProducer;

import java.util.ArrayList;
import java.util.List;


public class ActiveOrderFragmentProducer extends Fragment {

    private RecyclerView orderRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProducerActiveOrderDetailsAdapter adapter;
    private OrderManagementService orderManagementService;
    private ProgressBar progressBar;
    private List<OrderDataProducer> orderData = new ArrayList<>();
    private Call<OrderDetailsProducer> orderDetailsProducerCall;

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public ActiveOrderFragmentProducer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);

        final SharedPreferences sharedPreferences = context.getSharedPreferences("LoginCredentials", 0);
        orderDetailsProducerCall = orderManagementService
                .getProducerOrders(sharedPreferences.getString("userId", ""));

        orderDetailsProducerCall.enqueue(new Callback<OrderDetailsProducer>() {
            @Override
            public void onResponse(Call<OrderDetailsProducer> call, Response<OrderDetailsProducer> response) {
                Log.d("Single", sharedPreferences.getString("userId", "user Id not found"));
                progressBar.setVisibility(View.GONE);
                OrderDetailsProducer orderDetailsProducer = response.body();

                try {
                    List<OrderDataProducer> orderDataProducer = orderDetailsProducer.getData();

                    for (int i = 0; i < orderDataProducer.size(); i++) {
                        if (orderDataProducer.get(i).getAccepted().equals("0")) {
                            orderData.add(orderDataProducer.get(i));
                        }
                    }

                    if (orderData.size() == 0) {
                        Toast.makeText(context, "No Active orders found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                adapter = new ProducerActiveOrderDetailsAdapter(orderData, context);
                orderRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<OrderDetailsProducer> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accepted_order_fragment_producer, container, false);
        orderRecyclerView = view.findViewById(R.id.recyclerViewChildProducerFragment);
        layoutManager = new LinearLayoutManager(context);
        orderRecyclerView.setLayoutManager(layoutManager);

        progressBar = view.findViewById(R.id.producer_order_list_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orderDetailsProducerCall.cancel();

    }
}
