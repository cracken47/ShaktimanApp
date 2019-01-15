package com.alabhya.Shaktiman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.ConsumerSignIn.TokenResponseConsumerSignIn;
import com.alabhya.Shaktiman.utils.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsumerLoginActivity extends AppCompatActivity {

    private UserManagementService userManagementService;
    private TokenResponseConsumerSignIn tokenResponseConsumerSignIn;
    private EditText mobileNumber;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_login_activity);

        Button loginButton = findViewById(R.id.loginButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);


        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle("Consumer SignIn");

        loginButton.setOnClickListener(loginButtonListener);
    }

    private void consumerSignIn(final String phone, final String password){
        Call<TokenResponseConsumerSignIn> consumerSignInCall =
                userManagementService.signInConsumer(phone,password);

        consumerSignInCall.enqueue(new Callback<TokenResponseConsumerSignIn>() {
            @Override
            public void onResponse(Call<TokenResponseConsumerSignIn> call, Response<TokenResponseConsumerSignIn> response) {
                tokenResponseConsumerSignIn = response.body();

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

                    startActivity(new Intent(getApplicationContext(),ConsumerHomeActivity.class));
                    Intent intent = new Intent("finish_activity");
                    sendBroadcast(intent);
                    finish();
                }else{
                            Toast.makeText(getApplicationContext(),tokenResponseConsumerSignIn.getMessage()
                            ,Toast.LENGTH_SHORT).show();
                }
                Log.d("Single",tokenResponseConsumerSignIn.getId()+tokenResponseConsumerSignIn.getName());
            }

            @Override
            public void onFailure(Call<TokenResponseConsumerSignIn> call, Throwable t) {
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
            }else if(!new Validator().isValidPassword(passKey)){
                Toast.makeText(getApplicationContext(),"Please Enter valid Password(Length:6-12)",
                        Toast.LENGTH_SHORT).show();
            }else {
                Log.d("Single",phone+" "+passKey);
                try {
                    consumerSignIn(phone,passKey);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
