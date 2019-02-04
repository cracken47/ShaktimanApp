package com.alabhya.Shaktiman.ConsumerUserManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ConsumerMainView.ConsumerHomeActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OtpVerificationService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.ConsumerSignUp.TokenResponseConsumerSignUp;
import com.alabhya.Shaktiman.models.OtpResponse;
import com.alabhya.Shaktiman.utils.Validator;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsumerSignUpActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText mobileNumber;
    private EditText password;
    private UserManagementService userManagementService;
    private TokenResponseConsumerSignUp tokenResponseConsumerSignUp;
    private Call<TokenResponseConsumerSignUp> consumerSignUpCall;
    private OtpVerificationService otpVerificationService;
    private TextInputEditText otpView;
    private String name;
    private String phone;
    private String  passKey;
    private AlertDialog otpDialog;
    private ProgressBar otpProgressBar;
    private OtpResponse otpResponse;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_sign_up_activity);

        Button signupButton = findViewById(R.id.signUpButton);
        fullName = findViewById(R.id.fullName);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);
        otpVerificationService = ApiClient.getRetrofitClient().create(OtpVerificationService.class);

        //Setting Up Toolbar
        Toolbar signUpToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(signUpToolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        signUpToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        signUpToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signupButton.setOnClickListener(signUpButtonListener);
    }

    /**
     * Function for Consumer Signup using retrofit 2.0
     *
     * @param name fullName of Consumer
     * @param phone mobileNumber of Consumer
     * @param password password of Consumer
     */

    private void consumerSignUp(String name, final String phone, final String password){
        consumerSignUpCall = userManagementService.signUpConsumers(name,phone,password);

        consumerSignUpCall.enqueue(new Callback<TokenResponseConsumerSignUp>() {
            @Override
            public void onResponse(Call<TokenResponseConsumerSignUp> call, Response<TokenResponseConsumerSignUp> response) {
                otpProgressBar.setVisibility(View.GONE);
                tokenResponseConsumerSignUp = response.body();
                try{
                    if(tokenResponseConsumerSignUp.getStatus()==200){
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("phone",phone);
                        editor.putString("password",password);
                        editor.putBoolean("isProducer",false);
                        editor.putString("name",tokenResponseConsumerSignUp.getName());
                        editor.putString("userId",tokenResponseConsumerSignUp.getId().toString());
                        editor.apply();

                        Toast.makeText(getApplicationContext(),tokenResponseConsumerSignUp.getMessage(),Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getApplicationContext(),ConsumerHomeActivity.class));
                        Intent intent = new Intent("finish_consumer_landing_activity");
                        sendBroadcast(intent);
                        Intent i = new Intent("finish_main_activity");
                        sendBroadcast(i);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),tokenResponseConsumerSignUp.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(ConsumerSignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponseConsumerSignUp> call, Throwable t) {
                otpProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Function to get OTP
     *
     * @param phone User Phone Number
     */
    private void getOtp(String phone){
        Call<OtpResponse> getOtpCall = otpVerificationService.getOtp(phone);

        getOtpCall.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                otpProgressBar.setVisibility(View.GONE);
                try {
                    otpResponse = response.body();
                    if(otpResponse.getStatus() == 200){
                        Toast.makeText(getApplicationContext(),otpResponse.getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        otpDialog.hide();
                        Toast.makeText(getApplicationContext(),otpResponse.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                otpProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Function to verify OTP
     *
     * @param otp
     */
    private void verifyOtp(int otp){
        try {
            if(Integer.parseInt(otpView.getText().toString())==otp){
                consumerSignUp(name,phone,passKey);
            }else {
                Toast.makeText(this, "Wrong Otp!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }


    View.OnClickListener signUpButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConsumerSignUpActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_otp_verification,null);
            mBuilder.setView(mView);
            otpDialog = mBuilder.create();

            otpView = mView.findViewById(R.id.pinview);
            otpProgressBar = mView.findViewById(R.id.otpProgressBar);

            Button verifyOtpButton = mView.findViewById(R.id.verify_otp_button);
            verifyOtpButton.setOnClickListener(verifyOtpListener);

            TextView resendButton = mView.findViewById(R.id.resend_otp);
            resendButton.setOnClickListener(resendOtpListener);

            name = fullName.getText().toString().trim();
            phone = mobileNumber.getText().toString().trim();
            passKey = password.getText().toString().trim();

            if(!new Validator().validInput(name)){
                fullName.setError("Please Enter Name");
                Toast.makeText(getApplicationContext(),"Please Enter Name",Toast.LENGTH_SHORT).show();
            }else if(!new Validator().isValidPhone(phone)){
                mobileNumber.setError("Please enter a valid mobile number");
                Toast.makeText(getApplicationContext(),"Please Enter a valid phone number",Toast.LENGTH_SHORT).show();
            }else if(!new Validator().isValidPassword(passKey)){
                password.setError("Please Enter a Valid Password(at least one letter (a-z or A-Z) and one number(0-9)[Length: 6-15]");
                Toast.makeText(getApplicationContext(),"Please Enter a valid password",Toast.LENGTH_SHORT).show();
            }else {
                otpDialog.show();
                otpProgressBar.setVisibility(View.VISIBLE);
                getOtp(phone);
            }
        }
    };

    /**
     * Dialog Button Liener
     */
    View.OnClickListener verifyOtpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            otpProgressBar.setVisibility(View.VISIBLE);
            try {
                int otp = otpResponse.getOtp();
                verifyOtp(otp);
            }catch (NullPointerException e){
                otpView.setError("must not be empty!");
            }
        }
    };

    View.OnClickListener resendOtpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            otpProgressBar.setVisibility(View.VISIBLE);
            getOtp(phone);
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            consumerSignUpCall.cancel();
        }catch (Exception e){
            Log.d("Single",""+e);
        }
    }
}
