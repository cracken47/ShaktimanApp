package com.alabhya.Shaktiman.ProducerMainView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alabhya.Shaktiman.Adapters.ProducerOrderDetailsAdapter;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDetailsProducer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProducerOrdersFragment extends Fragment {

    private RecyclerView orderRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProducerOrderDetailsAdapter adapter;
    private OrderManagementService orderManagementService;
    private ProgressBar progressBar;
    private Call<OrderDetailsProducer> orderDetailsProducerCall;

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);

        final SharedPreferences sharedPreferences = context.getSharedPreferences("LoginCredentials",0);
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
