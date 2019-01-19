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
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.ConsumerSignIn.TokenResponseConsumerSignIn;
import com.alabhya.Shaktiman.utils.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsumerLandingActivity extends Activity {
    private UserManagementService userManagementService;
    private TokenResponseConsumerSignIn tokenResponseConsumerSignIn;
    private EditText mobileNumber;
    private EditText password;
    private ProgressBar progressBar;
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

    View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phone = mobileNumber.getText().toString().trim();
            String passKey = password.getText().toString().trim();

            if(!new Validator().isValidPhone(phone)){
                Toast.makeText(getApplicationContext(),"Please Enter valid Mobile Number",Toast.LENGTH_SHORT).show();
                mobileNumber.setError("Please Enter valid Mobile Number");
            }else if(!new Validator().isValidPassword(passKey)){
                password.setError("Please Enter a Valid Password(at least one letter (a-z or A-Z) and one number(0-9)[Length: 6-15]");
                Toast.makeText(getApplicationContext(),"Please Enter a valid password",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}
