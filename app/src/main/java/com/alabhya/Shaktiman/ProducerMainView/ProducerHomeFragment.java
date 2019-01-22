package com.alabhya.Shaktiman.ProducerMainView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.OrderDetails.ProducerOrderFullDetailsActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDataProducer;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDetailsProducer;

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
    private CardView cardView;
    private Button viewDetails;
    private ProgressBar progressBar;
    private TextView Active;
    private Context context;
    private OrderDataProducer orderData;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginCredentials",0);
        getOrders(sharedPreferences.getString("userId",""));
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
        cardView = view.findViewById(R.id.producer_fragment_home_cardView);
        progressBar =view.findViewById(R.id.producer_home_progressBar);
        Active = view.findViewById(R.id.isActiveProducerOrder);

        progressBar.setVisibility(View.VISIBLE);

        viewDetails = view.findViewById(R.id.producer_viewDetailsButton);

        viewDetails.setEnabled(false);

        viewDetails.setOnClickListener(viewDetailsListener);

        return view;
    }

    private void getOrders(String userId){
        Call<OrderDetailsProducer> orderDetailsProducerCall = orderManagementService.getProducerOrders(userId);

        orderDetailsProducerCall.enqueue(new Callback<OrderDetailsProducer>() {
            @Override
            public void onResponse(Call<OrderDetailsProducer> call, Response<OrderDetailsProducer> response) {
                progressBar.setVisibility(View.GONE);
                orderDetailsProducer = response.body();
                Log.d("Single",orderDetailsProducer.getStatus().toString());
                orderDataProducers = orderDetailsProducer.getData();

                viewDetails.setEnabled(true);


                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
                SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

                try {

                    for (int i=orderDataProducers.size()-1;i>=0;i--) {
                        if (!orderDataProducers.get(i).getAccepted().equals("2")) {
                            orderData = orderDataProducers.get(i);
                            break;
                        }
                    }
                    Date date = input.parse(orderData.getAddedAt());
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
                    int isActive = Integer.parseInt(orderData.getIsActive());
                    int isAccepted = Integer.parseInt(orderData.getAccepted());

                    if (isActive == 1 && isAccepted==1){
                        producers.setText("Accepted");
                        cardView.setCardBackgroundColor(Color.parseColor("#81c784"));
                    }else producers.setText("To Accept");
                    orderId.setText(String.format("Order Id: %s", orderData.getId()));
                    workDate.setText(String.format("Work Date: %s", orderData.getWorkDate()));
                    contact.setText(String.format("Contact No: %s", orderData.getContactPhone()));
                    locality.setText(String.format("Locality: %s", orderData.getLocalityName()));

                }catch (Exception e){
                    Toast.makeText(getActivity(),"No Orders Found!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsProducer> call, Throwable t) {

            }
        });
    }

    private View.OnClickListener viewDetailsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {



            Intent intent = new Intent(context,ProducerOrderFullDetailsActivity.class);
            intent.putExtra("state",orderData.getStateName());
            intent.putExtra("city",orderData.getCityName());
            intent.putExtra("id",orderData.getId());
            intent.putExtra("consumer",orderData.getConsumer());
            intent.putExtra("locality",orderData.getLocalityName());
            intent.putExtra("isActive",orderData.getIsActive());
            intent.putExtra("flat",orderData.getFlat());
            intent.putExtra("area",orderData.getArea());
            intent.putExtra("landmark",orderData.getLandmark());
            intent.putExtra("addedAt",orderData.getAddedAt());
            intent.putExtra("WorkDate",orderData.getWorkDate());
            intent.putExtra("producers",orderData.getProducersQuantity());
            intent.putExtra("confirmed",orderData.getConfirmedProducers());
            intent.putExtra("WorkDesc",orderData.getWorkDescription());
            intent.putExtra("phoneNumber",orderData.getContactPhone());
            intent.putExtra("accepted",orderData.getAccepted());
            context.startActivity(intent);


        }
    };
}
