package com.alabhya.Shaktiman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.ConsumerSignUp.TokenResponseConsumerSignUp;
import com.alabhya.Shaktiman.utils.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsumerSignUpActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText mobileNumber;
    private EditText password;
    private UserManagementService userManagementService;
    private TokenResponseConsumerSignUp tokenResponseConsumerSignUp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_sign_up_activity);

        Button signupButton = findViewById(R.id.signUpButton);
        fullName = findViewById(R.id.fullName);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);

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
        Call<TokenResponseConsumerSignUp> consumerSignUpCall =
                userManagementService.signUpConsumers(name,phone,password);

        consumerSignUpCall.enqueue(new Callback<TokenResponseConsumerSignUp>() {
            @Override
            public void onResponse(Call<TokenResponseConsumerSignUp> call, Response<TokenResponseConsumerSignUp> response) {
                tokenResponseConsumerSignUp = response.body();
                if(tokenResponseConsumerSignUp.getStatus()==200){
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("phone",phone);
                    editor.putString("password",password);
                    editor.putBoolean("isProducer",false);
                    editor.putString("name",tokenResponseConsumerSignUp.getName());
                    editor.putString("userId",tokenResponseConsumerSignUp.getId().toString());
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(),ConsumerHomeActivity.class));
                    Intent intent = new Intent("finish_activity");
                    sendBroadcast(intent);
                    finish();
                }
                Log.d("Single",tokenResponseConsumerSignUp.getName()+tokenResponseConsumerSignUp.getId());
            }

            @Override
            public void onFailure(Call<TokenResponseConsumerSignUp> call, Throwable t) {

            }
        });
    }


    View.OnClickListener signUpButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConsumerSignUpActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_otp_verification,null);
            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();*/
            String name = fullName.getText().toString().trim();
            String phone = mobileNumber.getText().toString().trim();
            String passKey = password.getText().toString().trim();

            if(!new Validator().validInput(name)){
                Toast.makeText(getApplicationContext(),"Please enter Name",Toast.LENGTH_SHORT).show();
            }else if(!new Validator().isValidPhone(phone)){
                Toast.makeText(getApplicationContext(),"Please enter a valid mobile number",Toast.LENGTH_SHORT).show();
            }if(!new Validator().isValidPassword(passKey)){
                Toast.makeText(getApplicationContext(),"Please enter a valid Password(Length:6-12)",Toast.LENGTH_SHORT).show();
            }else {
                consumerSignUp(name,phone,passKey);
            }
        }
    };
}
