package com.alabhya.Shaktiman.OrderDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ConsumerUserManagement.ConsumerSignUpActivity;
import com.alabhya.Shaktiman.ProducerMainView.ProducerHomeActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.PlaceOrder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerOrderFullDetailsActivity extends AppCompatActivity {

    private OrderManagementService orderManagementService;
    PlaceOrder placeOrder;
    private long mLastClicked = 0;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_order_full_details);

        int isAccepted = Integer.parseInt(getIntent().getStringExtra("accepted"));
        int activeState = Integer.parseInt(getIntent().getStringExtra("isActive"));

        String Address = getIntent().getStringExtra("flat")+","
                +getIntent().getStringExtra("area")+","
                +getIntent().getStringExtra("locality")+","
                +getIntent().getStringExtra("landmark")+","
                +getIntent().getStringExtra("city");

        TextView orderId =findViewById(R.id.full_detail_producer_orderId);
        TextView isActive = findViewById(R.id.full_detail_producer_isActive);
        TextView date = findViewById(R.id.full_detail_producer_dateAdded);
        TextView address = findViewById(R.id.full_detail_producer_address);
        TextView state = findViewById(R.id.full_detail_producer_state);
        TextView contact = findViewById(R.id.full_detail_producer_contact);
        TextView workDesc = findViewById(R.id.full_detail_producer_workDesc);
        TextView producers = findViewById(R.id.full_detail_producer_producers);
        TextView confirmedProducers = findViewById(R.id.full_detail_producer_confirmedProducers);
        TextView workDate = findViewById(R.id.full_detail_producer_workDate);
        TextView consumer = findViewById(R.id.full_detail_producer_name);
        TextView orderDetailsHeading =findViewById(R.id.orderDetailsProducer);

        FloatingActionButton floatingActionButton = findViewById(R.id.producer_full_detail_floatingActionButton);

        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);


        Button fab = findViewById(R.id.closingFloatingButton);



        if(activeState == 1 && isAccepted==0){
            fab.setEnabled(true);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (activeState == 1 && isAccepted == 1){
            isActive.setText("Accepted");
            ConstraintLayout constraintLayout = findViewById(R.id.producer_order_full_detail_head);
            constraintLayout.setBackgroundColor(Color.parseColor("#4caf50"));
            orderDetailsHeading.setBackgroundColor(Color.parseColor("#4caf50"));
            fab.setVisibility(View.GONE);
            isActive.setTextSize(14);
        }else if(activeState==0 && isAccepted == 1){
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(openDialogListener);


        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

        Date d = null;
        try
        {
            d = input.parse(getIntent().getStringExtra("addedAt"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(d);

        orderId.setText("Order Id: "+getIntent().getStringExtra("id"));
        date.setText(formatted);
        address.setText(Address);
        state.setText(getIntent().getStringExtra("state"));
        contact.setText(getIntent().getStringExtra("phoneNumber"));
        workDesc.setText(getIntent().getStringExtra("WorkDesc"));
        producers.setText(getIntent().getStringExtra("producers"));
        confirmedProducers.setText(getIntent().getStringExtra("confirmed"));
        workDate.setText(getIntent().getStringExtra("WorkDate"));
        consumer.setText(getIntent().getStringExtra("consumer"));
    }

    private void acceptOrder(String userId, String orderId, String date){
        Call<PlaceOrder> placeOrderCall = orderManagementService.acceptOrder(userId,orderId,date);

        placeOrderCall.enqueue(new Callback<PlaceOrder>() {
            @Override
            public void onResponse(Call<PlaceOrder> call, Response<PlaceOrder> response) {
                placeOrder = response.body();
                Toast.makeText(getApplicationContext(),placeOrder.getMessage(),Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),ProducerHomeActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<PlaceOrder> call, Throwable t) {

            }
        });
    }

    View.OnClickListener acceptOrderButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (SystemClock.elapsedRealtime()-mLastClicked<1000){
                return;
            }
            mLastClicked = SystemClock.elapsedRealtime();
            SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",0);
            String userId = sharedPreferences.getString("userId","");
            String orderId = getIntent().getStringExtra("id");
            String date = getIntent().getStringExtra("WorkDate");

            acceptOrder(userId,orderId,date);
        }
    };

    View.OnClickListener openDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProducerOrderFullDetailsActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.are_you_sure_dialog,null);
            mBuilder.setView(mView);

            Button acceptButton,cancelButton;

            acceptButton = mView.findViewById(R.id.accept_dialog_button);
            cancelButton = mView.findViewById(R.id.cancel_dialog_button);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                }
            });

            acceptButton.setOnClickListener(acceptOrderButtonListener);
        }
    };
}
