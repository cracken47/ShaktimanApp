package com.alabhya.Shaktiman.ProducerMainView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabhya.Shaktiman.R;

import org.angmarch.views.NiceSpinner;


public class ProducerHomeFragment extends Fragment {


    private NiceSpinner chooseLabour,chooseMason;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producer_home, container, false);
        return view;
    }
}
