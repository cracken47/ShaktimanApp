package com.alabhya.Shaktiman;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class ConsumerBookingFragment extends Fragment {

    private RecyclerView bookingRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_consumer_booking, container, false);

        bookingRecyclerView = view.findViewById(R.id.bookinRecyclerView);
        layoutManager = new LinearLayoutManager(this.getActivity());
        bookingRecyclerView.setLayoutManager(layoutManager);

        return view;
    }
}
