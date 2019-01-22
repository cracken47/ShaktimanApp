package com.alabhya.Shaktiman.ProducerMainView;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabhya.Shaktiman.Adapters.ProducerOrderViewPagerAdapter;
import com.alabhya.Shaktiman.R;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;


public class ProducerOrdersFragment extends Fragment {


    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producer_orders, container, false);
        tabLayout = view.findViewById(R.id.tab_producer_order);
        viewPager = view.findViewById(R.id.child_fragment_order_container);
        ProducerOrderViewPagerAdapter producerOrderViewPagerAdapter = new ProducerOrderViewPagerAdapter(getChildFragmentManager());

        producerOrderViewPagerAdapter.addFragment(new ActiveOrderFragmentProducer(),"Active Orders");
        producerOrderViewPagerAdapter.addFragment(new AcceptedOrderFragmentProducer(),"Accepted Orders");

        viewPager.setAdapter(producerOrderViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Single","OnDestroy Called");
    }
}
