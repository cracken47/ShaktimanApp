package com.alabhya.Shaktiman.ProducerUserManagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import com.alabhya.Shaktiman.ProducerMainView.ProducerHomeActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.ProducerSignIn.TokenResponseProducerLogin;
import com.alabhya.Shaktiman.utils.Validator;

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

        registerReceiver(broadcastReceiver, new IntentFilter("finish_producer_landing_activity"));

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);

        signIn = findViewById(R.id.loginButton);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);

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

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
