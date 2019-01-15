package com.alabhya.Shaktiman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;


public class ConsumerHomeFragment extends Fragment {


    private NiceSpinner chooseLabour,chooseMason;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_consumer_home, container, false);

        Button proceed = view.findViewById(R.id.proceed);
        chooseLabour = view.findViewById(R.id.chooseLabour);
        chooseMason = view.findViewById(R.id.chooseMason);

        ArrayList<String> labourData = new ArrayList<>();
        ArrayList<String> masonData = new ArrayList<>();

        labourData.add("Quantity of Labour");
        masonData.add("Quantity of Mason");

        for (int i=1;i<=20;i++){
            labourData.add(""+i);
            masonData.add(""+i);
        }

        ArrayAdapter<String > labourAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,labourData);
        chooseLabour.setAdapter(labourAdapter);
        ArrayAdapter<String > masonAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,masonData);
        chooseMason.setAdapter(masonAdapter);
        proceed.setOnClickListener(proceedButtonListener);
        return view;

    }

    View.OnClickListener proceedButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("Single","");
            Intent intent = new Intent(getContext(),ProducerLabourActivity.class);
            intent.putExtra("labourQuantity",chooseLabour.getSelectedIndex());
            intent.putExtra("masonQuantity",chooseMason.getSelectedIndex());
            startActivity(intent);
        }
    };





}
