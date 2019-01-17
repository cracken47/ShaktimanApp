package com.alabhya.Shaktiman.ProducerMainView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alabhya.Shaktiman.ProducerMainView.ProducerHomeFragment;
import com.alabhya.Shaktiman.ProducerMainView.ProducerOrdersFragment;
import com.alabhya.Shaktiman.ProducerMainView.ProducerProfileFragment;
import com.alabhya.Shaktiman.R;

public class ProducerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_home);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        registerReceiver(broadcastReceiver, new IntentFilter("finish_producer_home_activity"));

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ProducerHomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new ProducerHomeFragment();
                    break;
                case R.id.profile:
                    selectedFragment = new ProducerOrdersFragment();
                    break;
                case R.id.help:
                    selectedFragment = new ProducerProfileFragment();
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
            if (action=="finish_producer_home_activity"){
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
