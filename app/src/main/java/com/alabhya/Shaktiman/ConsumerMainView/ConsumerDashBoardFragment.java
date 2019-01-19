package com.alabhya.Shaktiman.ConsumerMainView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabhya.Shaktiman.MainActivity;
import com.alabhya.Shaktiman.R;

public class ConsumerDashBoardFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = context.getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consumer_dashboard, container, false);
        String name = sharedPreferences.getString("name","");
        String phone = sharedPreferences.getString("phone","null");
        String password = sharedPreferences.getString("password","null");

        TextView Name = view.findViewById(R.id.consumerProfileName);
        TextView Password = view.findViewById(R.id.passwordHidden);
        TextView Phone = view.findViewById(R.id.dashboard_mobile_number);
        ConstraintLayout logout = view.findViewById(R.id.logout_dashboard);

        Name.setText(name);
        Password.setText(password);
        Phone.setText(phone);

        logout.setOnClickListener(logoutListener);
        return view;

    }

    private View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            SharedPreferences locationPreferences = context.getSharedPreferences("ConsumerLocationInfo",0);
            SharedPreferences.Editor editor1 = locationPreferences.edit();
            editor1.clear();
            editor1.apply();
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
            Intent i = new Intent("finish_consumer_home_activity");
            context.sendBroadcast(i);
            getActivity().finish();
        }
    };
}
