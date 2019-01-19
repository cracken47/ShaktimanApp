package com.alabhya.Shaktiman.ConsumerMainView;

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
import android.widget.Toast;

import com.alabhya.Shaktiman.Adapters.ConsumerOrderDetailsAdapter;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDataConsumer;
import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDetailsConsumer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConsumerBookingFragment extends Fragment {

    private RecyclerView bookingRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ConsumerOrderDetailsAdapter adapter;
    private OrderManagementService orderManagementService;
    private ProgressBar progressBar;
    private Call<OrderDetailsConsumer> orderDetailsConsumerCall;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;


        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);

        final SharedPreferences sharedPreferences = context.getSharedPreferences("LoginCredentials",0);
        orderDetailsConsumerCall = orderManagementService.getConsumerOrders(sharedPreferences.getString("userId",""));

        orderDetailsConsumerCall.enqueue(new Callback<OrderDetailsConsumer>() {
            @Override
            public void onResponse(Call<OrderDetailsConsumer> call, Response<OrderDetailsConsumer> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("Single",sharedPreferences.getString("userId","user Id not found"));
                OrderDetailsConsumer orderDetailsConsumer = response.body();
                try {
                    List<OrderDataConsumer> orderDataConsumer = orderDetailsConsumer.getData();
                    if(orderDataConsumer.size()==0){
                        Toast.makeText(context,"No Orders Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(context,"Something went Wron!",Toast.LENGTH_SHORT).show();
                }

                adapter = new ConsumerOrderDetailsAdapter(orderDetailsConsumer,context);
                bookingRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<OrderDetailsConsumer> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_consumer_booking, container, false);
        bookingRecyclerView = view.findViewById(R.id.bookinRecyclerView);
        layoutManager = new LinearLayoutManager(this.getActivity());
        bookingRecyclerView.setLayoutManager(layoutManager);
        progressBar = view.findViewById(R.id.consumer_booking_progressBar);

        progressBar.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        orderDetailsConsumerCall.cancel();
    }
}
