package com.alabhya.Shaktiman;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ConsumerSignUpActivity extends AppCompatActivity {
    Toolbar signUpToolbar;
    Button signupButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_sign_up_activity);

        //Setting Up Toolbar
        signUpToolbar = findViewById(R.id.toolbar);
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

        signupButton = findViewById(R.id.button5);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConsumerSignUpActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_otp_verification,null);
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }
}
