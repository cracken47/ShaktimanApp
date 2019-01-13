package com.alabhya.Shaktiman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.ApiInterface;
import com.alabhya.Shaktiman.models.VendorSignIn.TokenResponseLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorLandingActivity extends AppCompatActivity {

    private TextView signUp;
    private EditText mobileNumber;
    private EditText password;
    private ApiInterface apiInterface;;
    private Button signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_registration);

        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VendorSignupActivity.class);
                startActivity(intent);
            }
        });

        apiInterface = ApiClient.getRetrofitClient().create(ApiInterface.class);

        signIn = findViewById(R.id.loginButton);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                producerSignIn(mobileNumber.getText().toString().trim(),password.getText().toString().trim());
            }
        });


    }

    private void producerSignIn(final String phone, final String password){
        Call<TokenResponseLogin> signIn = apiInterface.signInProducer(phone,password);

        signIn.enqueue(new Callback<TokenResponseLogin>() {
            @Override
            public void onResponse(Call<TokenResponseLogin> call, Response<TokenResponseLogin> response) {
               TokenResponseLogin tokenResponse = response.body();
               if (response.isSuccessful() && !TextUtils.isEmpty(tokenResponse.getName())){
                   SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedPreferences.edit();

                   editor.putString("phone",phone);
                   editor.putString("password",password);
                   editor.putBoolean("isProducer",true);
                   editor.apply();
                   Log.d("Single","Login Data"+phone+password);

                   startActivity(new Intent(getApplicationContext(),HomeActivity.class));
               }
                Log.d("Single",tokenResponse.getMessage()+tokenResponse.getStatus()+tokenResponse.getId());
            }

            @Override
            public void onFailure(Call<TokenResponseLogin> call, Throwable t) {
                Log.d("Single","Error Occured");
            }
        });
    }
}
