package com.alabhya.Shaktiman.ProducerMainView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alabhya.Shaktiman.Adapters.ConsumerOrderDetailsAdapter;
import com.alabhya.Shaktiman.Adapters.ProducerOrderDetailsAdapter;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDetailsConsumer;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDetailsProducer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProducerOrdersFragment extends Fragment {

    private RecyclerView orderRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProducerOrderDetailsAdapter adapter;
    private OrderManagementService orderManagementService;
    Context context;
    private ProgressBar progressBar;
    private Call<OrderDetailsProducer> orderDetailsProducerCall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getApplicationContext();

        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);

        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("LoginCredentials",0);
        orderDetailsProducerCall = orderManagementService
                .getProducerOrders(sharedPreferences.getString("userId",""));

        orderDetailsProducerCall.enqueue(new Callback<OrderDetailsProducer>() {
            @Override
            public void onResponse(Call<OrderDetailsProducer> call, Response<OrderDetailsProducer> response) {
                Log.d("Single",sharedPreferences.getString("userId","user Id not found"));
                progressBar.setVisibility(View.GONE);
                OrderDetailsProducer orderDetailsProducer = response.body();
                adapter = new ProducerOrderDetailsAdapter(orderDetailsProducer,context);
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
        View view = inflater.inflate(R.layout.fragment_producer_orders, container, false);
        orderRecyclerView = view.findViewById(R.id.producerBookingRecyclerView);
        layoutManager = new LinearLayoutManager(this.getActivity());
        orderRecyclerView.setLayoutManager(layoutManager);

        progressBar = view.findViewById(R.id.producer_order_list_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orderDetailsProducerCall.cancel();
        Log.d("Single","OnDestroy Called");
    }
}
