package com.alabhya.Shaktiman.ProducerUserManagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ConsumerUserManagement.ConsumerLandingActivity;
import com.alabhya.Shaktiman.ProducerMainView.ProducerHomeActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OtpVerificationService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.HttpResponse;
import com.alabhya.Shaktiman.models.ProducerSignIn.TokenResponseProducerLogin;
import com.alabhya.Shaktiman.utils.Validator;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerLandingActivity extends AppCompatActivity {

    private TextView signUp;
    private EditText mobileNumber;
    private EditText password;
    private UserManagementService userManagementService;;
    private Button signIn;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_landing);

        signUp = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.producer_signin_progressBar);
        progressBar.setVisibility(View.GONE);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProducerSignupActivity.class);
                startActivity(intent);
            }
        });

        ImageView backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        otpVerificationService = ApiClient.getRetrofitClient().create(OtpVerificationService.class);

        registerReceiver(broadcastReceiver, new IntentFilter("finish_producer_landing_activity"));

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);

        signIn = findViewById(R.id.loginButton);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);

        TextView forgotPassword = findViewById(R.id.profucer_landing_forgot_password);
        forgotPassword.setOnClickListener(forgotPasswordListener);

        signIn.setOnClickListener(signInButtonListener);


    }

    private void producerSignIn(final String phone, final String password){
        Call<TokenResponseProducerLogin> signIn = userManagementService.signInProducer(phone,password);

        signIn.enqueue(new Callback<TokenResponseProducerLogin>() {
            @Override
            public void onResponse(Call<TokenResponseProducerLogin> call, Response<TokenResponseProducerLogin> response) {
                progressBar.setVisibility(View.GONE);
               TokenResponseProducerLogin tokenResponse = response.body();
               if (response.isSuccessful() && !TextUtils.isEmpty(tokenResponse.getName())){
                   SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedPreferences.edit();

                   editor.putString("phone",phone);
                   editor.putString("password",password);
                   editor.putBoolean("isProducer",true);
                   editor.putString("userId",""+tokenResponse.getId());
                   editor.putString("name",tokenResponse.getName());
                   editor.apply();
                   Log.d("Single","Login Data"+phone+password+tokenResponse.getStatus());
                   startActivity(new Intent(getApplicationContext(),ProducerHomeActivity.class));
                   Intent intent = new Intent("finish_main_activity");
                   sendBroadcast(intent);
                   finish();
               }else if (tokenResponse.getStatus()==400){
                   Toast.makeText(getApplicationContext(),tokenResponse.getMessage(),Toast.LENGTH_LONG).show();
               }
                Log.d("Single",tokenResponse.getMessage()+tokenResponse.getStatus()+tokenResponse.getId());
            }

            @Override
            public void onFailure(Call<TokenResponseProducerLogin> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    View.OnClickListener signInButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressBar.setVisibility(View.VISIBLE);
            String phone = mobileNumber.getText().toString().trim();
            String passKey = password.getText().toString().trim();

            if(!new Validator().isValidPhone(phone)){
                mobileNumber.setError("Please Enter valid Mobile Number");
                Toast.makeText(getApplicationContext(),"Please Enter valid Mobile Number",Toast.LENGTH_SHORT).show();
            }else if(!new Validator().isValidPassword(passKey)){
                password.setError("Please Enter a Valid Password(at least one letter (a-z or A-Z) and one number(0-9)[Length: 6-15]");
                Toast.makeText(getApplicationContext(),"Please Enter a valid password",Toast.LENGTH_SHORT).show();
            }else {
                Log.d("Single",phone+" "+passKey);
                try {
                    producerSignIn(phone,passKey);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action=="finish_producer_landing_activity"){
                finish();
            }
        }
    };

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

    private View.OnClickListener forgotPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProducerLandingActivity.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProducerLandingActivity.this);
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
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProducerLandingActivity.this);
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
