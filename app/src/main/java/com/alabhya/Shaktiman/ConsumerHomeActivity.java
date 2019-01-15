package com.alabhya.Shaktiman;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.ConsumerSignIn.TokenResponseConsumerSignIn;
import com.facebook.shimmer.ShimmerFrameLayout;

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

        SharedPreferences sharedPreferences = getSharedPreferences("Logincredential",Context.MODE_PRIVATE);
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
                case R.id.orders:
                    selectedFragment = new ConsumerDashBoardFragment();
                    break;
                case R.id.profile:
                    selectedFragment = new ConsumerNotificationFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };


}
