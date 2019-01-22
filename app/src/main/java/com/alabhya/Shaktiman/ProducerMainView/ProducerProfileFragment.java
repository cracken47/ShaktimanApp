package com.alabhya.Shaktiman.ProducerMainView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ConsumerUserManagement.ConsumerLandingActivity;
import com.alabhya.Shaktiman.MainActivity;
import com.alabhya.Shaktiman.OrderDetails.ProducerOrderFullDetailsActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.HttpResponse;
import com.alabhya.Shaktiman.models.ProducerSignIn.TokenResponseProducerLogin;


public class ProducerProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private Context context;
    private OrderManagementService orderManagementService;
    private Call<HttpResponse> setAvailabiltyCall;
    private UserManagementService userManagementService;
    private Call<TokenResponseProducerLogin> signinCall;
    private TokenResponseProducerLogin tokenResponseProducerLogin;
    private String isAvailable;
    private Switch availabilitySwitch;
    private TextView availabilityTextView;
    private TextView designationTextview;
    private ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = context.getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);
        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);
        String phone = sharedPreferences.getString("phone", "null");
        String password = sharedPreferences.getString("password", "null");
        signIn(phone, password);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_producer_profile, container, false);
        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("phone", "null");
        String password = sharedPreferences.getString("password", "null");

        availabilityTextView = view.findViewById(R.id.textView5);
        progressBar = view.findViewById(R.id.availabilityProgressBar);

        availabilitySwitch = view.findViewById(R.id.switch1);

        designationTextview = view.findViewById(R.id.designation_producer_textView);


        availabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                    mBuilder.setMessage("Set your availability as available?").setPositiveButton("Yes", dialogAvailabilityClickListener)
                            .setNegativeButton("No", dialogAvailabilityClickListener).show();

                }
            }
        });

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

    private DialogInterface.OnClickListener dialogAvailabilityClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case DialogInterface.BUTTON_POSITIVE: {
                    String userId = sharedPreferences.getString("userId", "");
                    setAvailabiltyCall = orderManagementService.setAvailabilty(userId);

                    setAvailabiltyCall.enqueue(new Callback<HttpResponse>() {
                        @Override
                        public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                        }

                        @Override
                        public void onFailure(Call<HttpResponse> call, Throwable t) {

                        }
                    });
                }

                case DialogInterface.BUTTON_NEGATIVE: {
                    availabilitySwitch.setChecked(false);
                }
            }
        }
    };

    private void signIn(String phone, String password) {
        signinCall = userManagementService.signInProducer(phone, password);
        signinCall.enqueue(new Callback<TokenResponseProducerLogin>() {
            @Override
            public void onResponse(Call<TokenResponseProducerLogin> call, Response<TokenResponseProducerLogin> response) {
                tokenResponseProducerLogin = response.body();
                progressBar.setVisibility(View.GONE);
                isAvailable = tokenResponseProducerLogin.getIsAvailable();
                if (isAvailable.equals("1")) {
                    availabilitySwitch.setVisibility(View.GONE);
                    availabilityTextView.setText("Available");
                    availabilityTextView.setTextColor(Color.parseColor("#4caf50"));
                } else {
                    availabilitySwitch.setChecked(false);
                    availabilityTextView.setText("Not Available");
                    availabilitySwitch.setVisibility(View.VISIBLE);
                }

                if (tokenResponseProducerLogin.getStatus() == 200) {
                    if (tokenResponseProducerLogin.getIsLabour().equals("0")) {
                        designationTextview.setText("Mason");
                    } else if (tokenResponseProducerLogin.getIsLabour().equals("1")) {
                        designationTextview.setText("Labour");
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenResponseProducerLogin> call, Throwable t) {

            }
        });
    }


    private View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            mBuilder.setMessage("Are you sure?").setPositiveButton("Yes",dialogClickListener)
                    .setNegativeButton("No",dialogClickListener).show();
        }
    };


    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i){
                case DialogInterface.BUTTON_POSITIVE:
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    Intent intent1 = new Intent("finish_producer_home_activity");
                    context.sendBroadcast(intent1);
                    getActivity().finish();
                }

                case DialogInterface.BUTTON_NEGATIVE:
                {

                }
            }
        }
    };

}
