package com.alabhya.Shaktiman.ConsumerMainView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.ConsumerSignIn.TokenResponseConsumerSignIn;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsumerHomeActivity extends AppCompatActivity {

    TokenResponseConsumerSignIn tokenResponseConsumerSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ConsumerHomeFragment()).commit();

        registerReceiver(broadcastReceiver, new IntentFilter("finish_consumer_home_activity"));

        SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
        UserManagementService userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);

        Call<TokenResponseConsumerSignIn> signInCall = userManagementService.signInConsumer(
                sharedPreferences.getString("phone",""),
                sharedPreferences.getString("password",""));

        signInCall.enqueue(new Callback<TokenResponseConsumerSignIn>() {
            @Override
            public void onResponse(Call<TokenResponseConsumerSignIn> call, Response<TokenResponseConsumerSignIn> response) {
                tokenResponseConsumerSignIn = response.body();
            }

            @Override
            public void onFailure(Call<TokenResponseConsumerSignIn> call, Throwable t) {

            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.bookings:
                    selectedFragment = new ConsumerBookingFragment();
                    break;
                case R.id.navigation_home:
                    selectedFragment = new ConsumerHomeFragment();
                    break;
                case R.id.profile:
                    selectedFragment = new ConsumerDashBoardFragment();
                    break;
                case R.id.help:
                    selectedFragment = new ConsumerNotificationFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action=="finish_consumer_home_activity"){
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
