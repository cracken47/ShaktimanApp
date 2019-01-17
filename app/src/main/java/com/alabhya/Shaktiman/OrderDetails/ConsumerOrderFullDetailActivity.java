package com.alabhya.Shaktiman.OrderDetails;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsumerOrderFullDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_order_full_detail);

        String Address = getIntent().getStringExtra("flat")+","
                +getIntent().getStringExtra("area")+","
                +getIntent().getStringExtra("locality")+","
                +getIntent().getStringExtra("landmark")+","
                +getIntent().getStringExtra("city");

        TextView orderId =findViewById(R.id.full_detail_consumer_orderId);
        TextView isActive = findViewById(R.id.full_detail_consumer_isActive);
        TextView date = findViewById(R.id.full_detail_consumer_dateAdded);
        TextView address = findViewById(R.id.full_detail_consumer_address);
        TextView state = findViewById(R.id.full_detail_consumer_state);
        TextView contact = findViewById(R.id.full_detail_consumer_contact);
        TextView workDesc = findViewById(R.id.full_detail_consumer_workDesc);
        TextView producers = findViewById(R.id.full_detail_consumer_producers);
        TextView confirmedProducers = findViewById(R.id.full_detail_consumer_confirmedProducers);
        TextView workDate = findViewById(R.id.full_detail_consumer_workDate);

        FloatingActionButton fab = findViewById(R.id.closingFloatingButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
        try {
            address.setText(Address);
            state.setText(getIntent().getStringExtra("state"));
            contact.setText(getIntent().getStringExtra("phoneNumber"));
            workDesc.setText(getIntent().getStringExtra("WorkDesc"));
            producers.setText(getIntent().getStringExtra("producers"));
            confirmedProducers.setText(getIntent().getStringExtra("confirmed"));
            workDate.setText(getIntent().getStringExtra("WorkDate"));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }
}
