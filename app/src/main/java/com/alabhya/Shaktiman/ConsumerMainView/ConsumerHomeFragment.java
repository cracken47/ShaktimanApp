package com.alabhya.Shaktiman.ConsumerMainView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ChooseLocation;
import com.alabhya.Shaktiman.ProducersList.ProducerLabourActivity;
import com.alabhya.Shaktiman.ProducersList.ProducerMasonActivity;
import com.alabhya.Shaktiman.R;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;


public class ConsumerHomeFragment extends Fragment {
    private TextView location;
    private String DEFAULT = "Default";
    private NiceSpinner chooselabour,choosemason;
    private Context context;
    private SharedPreferences sharedPreferences;
    private Button proceed;

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
    public void onStart() {
        super.onStart();
        sharedPreferences = context.getSharedPreferences("ConsumerLocationInfo",0);
        String cityName = sharedPreferences.getString("city",DEFAULT);
        String localityName = sharedPreferences.getString("locality",DEFAULT);
        String Location;
        Log.d("Single",cityName+localityName);
        try {
            if (cityName.equals(DEFAULT) || localityName.equals(DEFAULT)){
                Location = "No location selected";
                location.setText(Location);
            }else {
                Location = localityName+", "+cityName;
                location.setText(Location);
            }
        }catch (NullPointerException e){
            Log.d("Single",""+e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_consumer_home, container, false);

        proceed = view.findViewById(R.id.proceed);
        chooselabour = view.findViewById(R.id.chooseLabour);
        choosemason = view.findViewById(R.id.chooseMason);
        location = view.findViewById(R.id.location_textView);

        ArrayList<String> labourData = new ArrayList<>();
        ArrayList<String> masonData = new ArrayList<>();

        labourData.add("Quantity of Labour");
        masonData.add("Quantity of Mason");

        for (int i=1;i<=20;i++){
            labourData.add(""+i);
            masonData.add(""+i);
        }

        LinearLayout linearLayout = view.findViewById(R.id.choose_location_view);
        linearLayout.setOnClickListener(locationButtonListener);

        ArrayAdapter<String > labourAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,labourData);
        chooselabour.setAdapter(labourAdapter);
        ArrayAdapter<String > masonAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,masonData);
        choosemason.setAdapter(masonAdapter);
        proceed.setOnClickListener(proceedButtonListener);
        return view;

    }

    private View.OnClickListener locationButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(context, ChooseLocation.class));
        }
    };

    private View.OnClickListener proceedButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String cityName = sharedPreferences.getString("city",DEFAULT);
            String localityName = sharedPreferences.getString("locality",DEFAULT);
            Log.d("Single",cityName+localityName);
            try {
                if (cityName.equals(DEFAULT) || localityName.equals(DEFAULT)){
                    Toast.makeText(context, "Please Choose Location First!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (chooselabour.getSelectedIndex()==0 && choosemason.getSelectedIndex()==0){
                    Toast.makeText(context, "Please Choose quantity of mason and labour!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }catch (NullPointerException e){
                Log.d("Single",""+e);
            }
            Log.d("Single","");

            if (chooselabour.getSelectedIndex()==0){
                Intent intent = new Intent(getContext(), ProducerMasonActivity.class);
                intent.putExtra("labourQuantity",chooselabour.getSelectedIndex());
                intent.putExtra("masonQuantity",choosemason.getSelectedIndex());
                startActivity(intent);
            }else {
                Intent intent = new Intent(getContext(),ProducerLabourActivity.class);
                intent.putExtra("labourQuantity",chooselabour.getSelectedIndex());
                intent.putExtra("masonQuantity",choosemason.getSelectedIndex());
                startActivity(intent);
            }
        }
    };

}
