package com.alabhya.Shaktiman.ProducerMainView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.OrderDetails.ProducerOrderFullDetailsActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDataProducer;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDetailsProducer;

import org.angmarch.views.NiceSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProducerHomeFragment extends Fragment {

    private TextView orderId;
    private TextView orderDate;
    private TextView contact;
    private TextView workDate;
    private TextView locality;
    private TextView producers;
    private OrderManagementService orderManagementService;
    private List<OrderDataProducer> orderDataProducers;
    private OrderDetailsProducer orderDetailsProducer;
    private Context context;
    private CardView cardView;
    private Button viewDetails;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("LoginCredentials",0);
        getOrders(sharedPreferences.getString("userId",""));
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producer_home, container, false);

        orderId = view.findViewById(R.id.producer_orderId);
        orderDate = view.findViewById(R.id.producer_orderDate);
        contact  = view.findViewById(R.id.producer_contact);
        workDate = view.findViewById(R.id.producer_workDate);
        locality = view.findViewById(R.id.producer_locality);
        producers = view.findViewById(R.id.producer_producerQuant);
        cardView = view.findViewById(R.id.cardView6);

        viewDetails = view.findViewById(R.id.producer_viewDetailsButton);

        viewDetails.setOnClickListener(viewDetailsListener);

        return view;
    }

    private void getOrders(String userId){
        Call<OrderDetailsProducer> orderDetailsProducerCall = orderManagementService.getProducerOrders(userId);

        orderDetailsProducerCall.enqueue(new Callback<OrderDetailsProducer>() {
            @Override
            public void onResponse(Call<OrderDetailsProducer> call, Response<OrderDetailsProducer> response) {
                orderDetailsProducer = response.body();
                Log.d("Single",orderDetailsProducer.getStatus().toString());
                orderDataProducers = orderDetailsProducer.getData();


                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
                SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

                try {
                    Date date = input.parse(orderDataProducers.get(orderDataProducers.size()-1).getAddedAt());
                    String formatted = output.format(date);
                    orderDate.setText(formatted);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(orderDataProducers.size()==0){
                    cardView.setVisibility(View.GONE);
                    viewDetails.setVisibility(View.GONE);
                }


                try {
                    orderId.setText(orderDataProducers.get(orderDataProducers.size()-1).getId());
                    workDate.setText(orderDataProducers.get(orderDataProducers.size()-1).getWorkDate());
                    contact.setText(orderDataProducers.get(orderDataProducers.size()-1).getContactPhone());
                    locality.setText(orderDataProducers.get(orderDataProducers.size()-1).getLocalityName());
                    producers.setText(orderDataProducers.get(orderDataProducers.size()-1).getProducersQuantity());
                }catch (Exception e){
                    Toast.makeText(getActivity(),"No Orders Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsProducer> call, Throwable t) {

            }
        });
    }

    View.OnClickListener viewDetailsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            OrderDataProducer orderDataProducer = orderDataProducers.get(orderDataProducers.size()-1);
            Intent intent = new Intent(getActivity(),ProducerOrderFullDetailsActivity.class);
            intent.putExtra("state",orderDataProducer.getStateName());
            intent.putExtra("city",orderDataProducer.getCityName());
            intent.putExtra("id",orderDataProducer.getId());
            intent.putExtra("consumer",orderDataProducer.getConsumer());
            intent.putExtra("locality",orderDataProducer.getLocalityName());
            intent.putExtra("isActive",orderDataProducer.getIsActive());
            intent.putExtra("flat",orderDataProducer.getFlat());
            intent.putExtra("area",orderDataProducer.getArea());
            intent.putExtra("landmark",orderDataProducer.getLandmark());
            intent.putExtra("addedAt",orderDataProducer.getAddedAt());
            intent.putExtra("WorkDate",orderDataProducer.getWorkDate());
            intent.putExtra("producers",orderDataProducer.getProducersQuantity());
            intent.putExtra("confirmed",orderDataProducer.getConfirmedProducers());
            intent.putExtra("WorkDesc",orderDataProducer.getWorkDescription());
            intent.putExtra("phoneNumber",orderDataProducer.getContactPhone());
            intent.putExtra("accepted",orderDataProducer.getAccepted());
            getActivity().startActivity(intent);
        }
    };
}
