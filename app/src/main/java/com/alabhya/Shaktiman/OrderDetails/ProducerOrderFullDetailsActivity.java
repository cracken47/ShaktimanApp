package com.alabhya.Shaktiman.OrderDetails;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.SystemClock;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alabhya.Shaktiman.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ProducerMainView.ProducerHomeActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.models.HttpResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerOrderFullDetailsActivity extends AppCompatActivity {

    private OrderManagementService orderManagementService;
    HttpResponse httpResponse;
    private long mLastClicked = 0;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_order_full_details);

        int isAccepted = Integer.parseInt(getIntent().getStringExtra("accepted"));
        int activeState = Integer.parseInt(getIntent().getStringExtra("isActive"));

        String Address = getIntent().getStringExtra("flat") + ","
                + getIntent().getStringExtra("area") + ","
                + getIntent().getStringExtra("locality") + ","
                + getIntent().getStringExtra("landmark") + ","
                + getIntent().getStringExtra("city");

        TextView orderId = findViewById(R.id.full_detail_producer_orderId);
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
        TextView orderDetailsHeading = findViewById(R.id.orderDetailsProducer);

        FloatingActionButton floatingActionButton = findViewById(R.id.producer_full_detail_floatingActionButton);

        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);


        Button fab = findViewById(R.id.closingFloatingButton);


        if (activeState == 1 && isAccepted == 0) {
            fab.setEnabled(true);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (activeState == 1 && isAccepted == 1) {
            isActive.setText("Accepted");
            ConstraintLayout constraintLayout = findViewById(R.id.producer_order_full_detail_head);
            constraintLayout.setBackgroundColor(Color.parseColor("#4caf50"));
            orderDetailsHeading.setBackgroundColor(Color.parseColor("#4caf50"));
            fab.setVisibility(View.GONE);
            isActive.setTextSize(14);
        } else if (activeState == 0 && isAccepted == 1) {
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(openDialogListener);


        String inputPattern = "yyyy-MM-dd HH:mm:ss.SSSSSS";
        SimpleDateFormat input = new SimpleDateFormat(inputPattern);
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

        Date d = null;
        try {
            d = input.parse(getIntent().getStringExtra("addedAt"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);

        orderId.setText(String.format("Order Id: %s", getIntent().getStringExtra("id")));
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

    private void acceptOrder(String userId, String orderId, String date) {
        Call<HttpResponse> placeOrderCall = orderManagementService.acceptOrder(userId, orderId, date);

        placeOrderCall.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                httpResponse = response.body();
                Toast.makeText(getApplicationContext(), httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ProducerHomeActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {

            }
        });
    }


    private View.OnClickListener openDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProducerOrderFullDetailsActivity.this);
            mBuilder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    };

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case DialogInterface.BUTTON_POSITIVE: {
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials", 0);
                    String userId = sharedPreferences.getString("userId", "");
                    String orderId = getIntent().getStringExtra("id");
                    String date = getIntent().getStringExtra("WorkDate");

                    acceptOrder(userId, orderId, date);
                }

                case DialogInterface.BUTTON_NEGATIVE: {

                }
            }
        }
    };


}
