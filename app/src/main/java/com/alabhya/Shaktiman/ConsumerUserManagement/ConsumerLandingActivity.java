package com.alabhya.Shaktiman.ConsumerUserManagement;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ConsumerMainView.ConsumerHomeActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OtpVerificationService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.ConsumerSignIn.TokenResponseConsumerSignIn;
import com.alabhya.Shaktiman.models.HttpResponse;
import com.alabhya.Shaktiman.utils.Validator;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsumerLandingActivity extends Activity {
    private UserManagementService userManagementService;
    private TokenResponseConsumerSignIn tokenResponseConsumerSignIn;
    private EditText mobileNumber;
    private EditText password;
    private ProgressBar progressBar;
    private AlertDialog otpDialog;
    private AlertDialog phoneDialog;
    private AlertDialog passwordDialog;
    private String phoneForgotPassword;
    private OtpVerificationService otpVerificationService;
    private ProgressBar otpProgressBar;
    private TextInputEditText phoneInputEditText;
    private TextInputEditText otpView;
    private String otp;
    private TextInputEditText enterPassword;
    private TextInputEditText reEnterPassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counsumer_landing_activity);

        Button loginButton = findViewById(R.id.consumer_landing_loginButton);
        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);
        mobileNumber = findViewById(R.id.consumer_landing_mobileNumber);
        password = findViewById(R.id.consumer_landing_password);
        progressBar = findViewById(R.id.consumer_signin_progressBar);
        progressBar.setVisibility(View.GONE);
        TextView signUp = findViewById(R.id.consumer_landing_signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ConsumerSignUpActivity.class);
                startActivity(i);
            }
        });

        TextView forgotPassword = findViewById(R.id.consumer_landing_forgotPassword);
        forgotPassword.setOnClickListener(forgotPasswordListener);


        otpVerificationService = ApiClient.getRetrofitClient().create(OtpVerificationService.class);

        registerReceiver(broadcastReceiver, new IntentFilter("finish_consumer_landing_activity"));
        loginButton.setOnClickListener(loginButtonListener);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action=="finish_consumer_landing_activity"){
                finish();
            }
        }
    };

    private void consumerSignIn(final String phone, final String password){
        Call<TokenResponseConsumerSignIn> consumerSignInCall =
                userManagementService.signInConsumer(phone,password);

        consumerSignInCall.enqueue(new Callback<TokenResponseConsumerSignIn>() {
            @Override
            public void onResponse(Call<TokenResponseConsumerSignIn> call, Response<TokenResponseConsumerSignIn> response) {
                tokenResponseConsumerSignIn = response.body();
                progressBar.setVisibility(View.GONE);

                if (tokenResponseConsumerSignIn.getStatus()==200){
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("phone",phone);
                    editor.putString("password",password);
                    editor.putString("userId",tokenResponseConsumerSignIn.getId());
                    editor.putString("name",tokenResponseConsumerSignIn.getName());
                    editor.putBoolean("isProducer",false);
                    editor.apply();
                    Log.d("Single","Login Data"+phone+password);

                    startActivity(new Intent(getApplicationContext(), ConsumerHomeActivity.class));
                    Intent intent = new Intent("finish_consumer_landing_activity");
                    sendBroadcast(intent);
                    Intent i = new Intent("finish_main_activity");
                    sendBroadcast(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),tokenResponseConsumerSignIn.getMessage()
                            ,Toast.LENGTH_SHORT).show();
                }
                Log.d("Single",tokenResponseConsumerSignIn.getId()+tokenResponseConsumerSignIn.getName());
            }

            @Override
            public void onFailure(Call<TokenResponseConsumerSignIn> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Something went wrong..",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePassword(String phone,String password){
        Call<HttpResponse> changePasswordCall = userManagementService.updatePassword(phone,password);

        changePasswordCall.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                try {
                    HttpResponse httpResponse = response.body();
                    if(httpResponse.getStatus()==200){
                        passwordDialog.hide();
                        Toast.makeText(getApplicationContext(),httpResponse.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phone = mobileNumber.getText().toString().trim();
            String passKey = password.getText().toString().trim();

            if(!new Validator().isValidPhone(phone)){
                Toast.makeText(getApplicationContext(),"Please Enter valid Mobile Number",Toast.LENGTH_SHORT).show();
                mobileNumber.setError("Please Enter valid Mobile Number");
            }else if(!new Validator().validInput(passKey)){
                password.setError("Password must not be empty!");
            }else {
                Log.d("Single",phone+" "+passKey);
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    consumerSignIn(phone,passKey);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private View.OnClickListener forgotPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConsumerLandingActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.forgot_password_enter_phonenumber_dialog,null);
            mBuilder.setView(mView);
            phoneDialog = mBuilder.create();
            phoneInputEditText = mView.findViewById(R.id.dialogPhone);
            phoneDialog.show();

            Button submitPhone = mView.findViewById(R.id.phoneSubmitButton);
            submitPhone.setOnClickListener(submitPhoneListener);
        }
    };

    private View.OnClickListener submitPhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            phoneForgotPassword = phoneInputEditText.getText().toString();
            phoneDialog.hide();
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConsumerLandingActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_otp_verification,null);
            mBuilder.setView(mView);
            otpDialog = mBuilder.create();

            otpView = mView.findViewById(R.id.pinview);
            otpDialog.show();

            Log.d("Single",phoneForgotPassword);
            getOtp(phoneForgotPassword);
            otpProgressBar = mView.findViewById(R.id.otpProgressBar);

            Button verifyOtpButton = mView.findViewById(R.id.verify_otp_button);
            verifyOtpButton.setOnClickListener(verifyOtpListener);
        }
    };

    private View.OnClickListener verifyOtpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            otpDialog.hide();
            progressBar.setVisibility(View.VISIBLE);
            otp = otpView.getText().toString();
            verifyOtp(phoneForgotPassword,otp);
        }
    };

    private View.OnClickListener submitPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String password = enterPassword.getText().toString();
            String confirmPassword = reEnterPassword.getText().toString();

            if (password.equals(confirmPassword)){
                changePassword(phoneForgotPassword,password);
            }else reEnterPassword.setError("Password did not match");
        }
    };

    /**
     * Function to get OTP
     *
     * @param phone User Phone Number
     */
    private void getOtp(String phone){
        Call<HttpResponse> getOtpCall = otpVerificationService.getOtp(phone);

        getOtpCall.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                otpProgressBar.setVisibility(View.GONE);
                try {
                    HttpResponse httpResponse = response.body();
                    if(httpResponse.getStatus() == 200){
                        Toast.makeText(getApplicationContext(),httpResponse.getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        otpDialog.hide();
                        Toast.makeText(getApplicationContext(),httpResponse.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {
                otpProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Function to get
     * @param phone
     * @param otp
     */
    private void verifyOtp(final String phone, String otp){
        Call<HttpResponse> verifyOtpCall = otpVerificationService.verifyOtp(phone,otp);

        verifyOtpCall.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                otpProgressBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                try {
                    HttpResponse httpResponse = response.body();
                    Toast.makeText(getApplicationContext(),httpResponse.getMessage(),Toast.LENGTH_LONG).show();
                    if (httpResponse.getStatus() == 200){
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConsumerLandingActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.forgotpassword_enter_password_dialog,null);
                        mBuilder.setView(mView);
                        passwordDialog = mBuilder.create();
                        passwordDialog.show();
                        enterPassword = mView.findViewById(R.id.enterPasswordDialog);
                        reEnterPassword = mView.findViewById(R.id.reEnterPasswordDialog);

                        Button submitPassword = mView.findViewById(R.id.submitPassword);
                        submitPassword.setOnClickListener(submitPasswordListener);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {
                otpProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}
