package com.alabhya.Shaktiman.ConsumerMainView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ConsumerUserManagement.ConsumerLandingActivity;
import com.alabhya.Shaktiman.MainActivity;
import com.alabhya.Shaktiman.ProducerUserManagement.ProducerLandingActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.HttpResponse;
import com.google.android.material.textfield.TextInputEditText;

public class ConsumerDashBoardFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    private Context context;
    private Dialog changePasswordDialog;
    private TextInputEditText oldPassword;
    private Dialog passwordDialog;
    private TextInputEditText enterPassword;
    private TextInputEditText reEnterPassword;
    private UserManagementService userManagementService;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = context.getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consumer_dashboard, container, false);
        String name = sharedPreferences.getString("name","");
        String phone = sharedPreferences.getString("phone","null");
        String password = sharedPreferences.getString("password","null");

        TextView Name = view.findViewById(R.id.consumerProfileName);
        TextView Password = view.findViewById(R.id.passwordHidden);
        TextView Phone = view.findViewById(R.id.dashboard_mobile_number);
        ConstraintLayout logout = view.findViewById(R.id.logout_dashboard);
        CardView changeConsumerPassword = view.findViewById(R.id.changeConsumerPassword);
        changeConsumerPassword.setOnClickListener(changePasswordListener);

        Name.setText(name);
        Password.setText(password);
        Phone.setText(phone);

        logout.setOnClickListener(logoutListener);
        return view;

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
                        Toast.makeText(context,httpResponse.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(context,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {
                Toast.makeText(context,"Something Went Wrong",Toast.LENGTH_SHORT).show();
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

    private View.OnClickListener changePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.enter_old_password_dialog,null);
            mBuilder.setView(mView);
            changePasswordDialog = mBuilder.create();
            changePasswordDialog.show();

            oldPassword = mView.findViewById(R.id.oldPassword);
            Button button = mView.findViewById(R.id.oldPasswordbutton);

            button.setOnClickListener(verifyPasswordListener);
        }
    };

    private View.OnClickListener submitPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String password = enterPassword.getText().toString();
            String confirmPassword = reEnterPassword.getText().toString();

            String phone = sharedPreferences.getString("phone","null");
            if (password.equals(confirmPassword)){
                changePassword(phone,password);
            }else reEnterPassword.setError("Password did not match");
        }
    };

    private View.OnClickListener verifyPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String password = sharedPreferences.getString("password","null");
            if(password.equals(oldPassword.getText().toString())){
                changePasswordDialog.hide();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = getLayoutInflater().inflate(R.layout.forgotpassword_enter_password_dialog,null);
                mBuilder.setView(mView);
                passwordDialog = mBuilder.create();
                passwordDialog.show();
                enterPassword = mView.findViewById(R.id.enterPasswordDialog);
                reEnterPassword = mView.findViewById(R.id.reEnterPasswordDialog);

                Button submitPassword = mView.findViewById(R.id.submitPassword);
                submitPassword.setOnClickListener(submitPasswordListener);
            }else {
                Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
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

                    SharedPreferences locationPreferences = context.getSharedPreferences("ConsumerLocationInfo",0);
                    SharedPreferences.Editor editor1 = locationPreferences.edit();
                    editor1.clear();
                    editor1.apply();
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                    Intent intent1 = new Intent("finish_consumer_home_activity");
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
