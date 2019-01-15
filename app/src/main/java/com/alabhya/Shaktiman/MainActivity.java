package com.alabhya.Shaktiman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView vendorSignup;
    TextView consumerSignup;
    private static final  String DEFAULT = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        consumerSignup = findViewById(R.id.consumerSignUp);
        consumerSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConsumerLandingActivity.class);
                startActivity(intent);
            }
        });

        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

        vendorSignup = findViewById(R.id.vendorSignup);

        vendorSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProducerLandingActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone",DEFAULT);
        String password = sharedPreferences.getString("password",DEFAULT);
        boolean isProducer = sharedPreferences.getBoolean("isProducer",false);

        if(phone!=DEFAULT && password!=DEFAULT && isProducer){
            Log.d("Single",phone+password);
            startActivity(new Intent(getApplicationContext(),ProducerHomeActivity.class));
            finish();
        }else if(phone!=DEFAULT && password!=DEFAULT && !isProducer){
            Log.d("Single",phone+password);
            startActivity(new Intent(getApplicationContext(),ConsumerHomeActivity.class));
            finish();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action=="finish_activity"){
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
