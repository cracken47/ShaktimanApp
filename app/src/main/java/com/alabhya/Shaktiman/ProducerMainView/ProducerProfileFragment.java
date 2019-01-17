package com.alabhya.Shaktiman.ProducerMainView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabhya.Shaktiman.MainActivity;
import com.alabhya.Shaktiman.R;


public class ProducerProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_producer_profile, container, false);
        String name = sharedPreferences.getString("name","");
        String phone = sharedPreferences.getString("phone","null");
        String password = sharedPreferences.getString("password","null");

        TextView Name = view.findViewById(R.id.producerProfileName);
        TextView Password = view.findViewById(R.id.producer_passwordHidden);
        TextView Phone = view.findViewById(R.id.dashboard_producer_mobile_number);
        ConstraintLayout logout = view.findViewById(R.id.producer_logout_dashboard);

        Name.setText(name);
        Password.setText(password);
        Phone.setText(phone);

        logout.setOnClickListener(logoutListener);
        return view;
    }

    View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
        }
    };
}
