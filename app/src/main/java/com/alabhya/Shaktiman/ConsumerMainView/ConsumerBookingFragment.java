package com.alabhya.Shaktiman.ConsumerMainView;

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

import com.alabhya.Shaktiman.Adapters.ConsumerOrderDetailsAdapter;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDetailsConsumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConsumerBookingFragment extends Fragment {

    private RecyclerView bookingRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ConsumerOrderDetailsAdapter adapter;
    private OrderManagementService orderManagementService;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_consumer_booking, container, false);
        bookingRecyclerView = view.findViewById(R.id.bookinRecyclerView);
        layoutManager = new LinearLayoutManager(this.getActivity());
        bookingRecyclerView.setLayoutManager(layoutManager);

        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);

        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("LoginCredentials",0);
        Call<OrderDetailsConsumer> orderDetailsConsumerCall = orderManagementService
                .getConsumerOrders(sharedPreferences.getString("userId",""));

        orderDetailsConsumerCall.enqueue(new Callback<OrderDetailsConsumer>() {
            @Override
            public void onResponse(Call<OrderDetailsConsumer> call, Response<OrderDetailsConsumer> response) {
                Log.d("Single",sharedPreferences.getString("userId","user Id not found"));
                OrderDetailsConsumer orderDetailsConsumer = response.body();
                adapter = new ConsumerOrderDetailsAdapter(orderDetailsConsumer,context);
                bookingRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<OrderDetailsConsumer> call, Throwable t) {

            }
        });

        return view;
    }
}
