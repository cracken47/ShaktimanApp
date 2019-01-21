package com.alabhya.Shaktiman.ProducerUserManagement;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ConsumerUserManagement.ConsumerSignUpActivity;
import com.alabhya.Shaktiman.ProducerMainView.ProducerHomeActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OtpVerificationService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.HttpResponse;
import com.alabhya.Shaktiman.models.Location;
import com.alabhya.Shaktiman.models.ProducerSignup.TokenResponseProducerSignUp;
import com.alabhya.Shaktiman.utils.Validator;
import com.goodiebag.pinview.Pinview;
import com.google.android.material.textfield.TextInputEditText;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerSignupActivity extends AppCompatActivity {

    private static final String TAG = "Single";

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private UserManagementService userManagementService;
    private List<Location> cities;
    private List<Location> localities;
    private List<String> cityName = new ArrayList<>();
    private List<String> LocalitiesName = new ArrayList<>();
    private List<String> StateName = new ArrayList<>();
    private int citySize, localitySize;
    private String age;

    private EditText fullName;
    private EditText password;
    private EditText mobileNumber;
    private EditText aadhar;
    private TextView dob;
    private NiceSpinner spinnerCity, spinnerState, spinnerLocality;
    private RadioGroup isLabour;
    private String islabour;
    private AlertDialog otpDialog;
    private ProgressBar otpProgressBar;
    private OtpVerificationService otpVerificationService;
    private TextInputEditText otpView;
    private String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_signup);

        fullName = findViewById(R.id.fullName);
        password = findViewById(R.id.password);
        mobileNumber = findViewById(R.id.mobileNumber);
        aadhar = findViewById(R.id.aadhar);
        spinnerCity = findViewById(R.id.citySpinner);
        spinnerLocality = findViewById(R.id.localitySpinner);
        spinnerState = findViewById(R.id.stateSpinner);
        isLabour = findViewById(R.id.producer_isLabour_radioGroup);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        Button signUpButton = findViewById(R.id.signUpButton);

        isLabour.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.labourRadioButton:
                        islabour = "1";
                        break;
                    case R.id.masonRadioButton:
                        islabour = "0";

                }

                Log.d("Single",islabour);
            }
        });

        /*   **************************
             ***TOOLBAR SECTION CODE***
             **************************
         */
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
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

        toolbar.setTitle("Producer SignUp");

        /*   **************************
             ***APICALL SECTION CODE***
             **************************
         */

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);
        otpVerificationService = ApiClient.getRetrofitClient().create(OtpVerificationService.class);
        // @GET request getCities.php
        getCities();

        // @GET request getLocalities.php
        getLocalities();

        // Add State
        StateName.add("Choose State");
        StateName.add("Uttar Pradesh");
        spinnerState.attachDataSource(StateName);

        /*  **************************
            ***  DOB SECTION CODE  ***
            **************************
         */
        dob = findViewById(R.id.dateOfBirth);
        dob.setOnClickListener(dateOfBirthListener);

        //Update date using the dialog of Calendar Instance
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date;
                age = getAge(year,month,dayOfMonth);

                SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("age",age);
                editor.apply();
                Log.d(TAG,age);

                if (month < 10 && dayOfMonth >= 10) {
                    date = year + "-0" + month + "-" + dayOfMonth;
                } else if (dayOfMonth < 10 && month >= 10) {
                    date = year + "-" + month + "-0" + dayOfMonth;
                } else if (month < 10 && dayOfMonth < 10) {
                    date = year + "-0" + month + "-0" + dayOfMonth;
                } else {
                    date = year + "-" + month + "-" + dayOfMonth;
                }
                dob.setText(date);
            }
        };

        /*
         * Function call for Signup on Button Click
         */
        signUpButton.setOnClickListener(signUpButtonListener);


    }


    /**
     * A function to SignUp Producer using Retrofit 2.0 Api
     *
     * @param fullName   Full name of Producer
     * @param password   Password of Producer
     * @param phone      Mobile Number of Producer
     * @param stateId    State Id of Producer
     * @param cityId     City Id of Producer
     * @param localityId Locality id of Producer
     * @param dob        Date of Birth of Producer
     * @param adhar      Aadhar Number of Producer
     * @param isLabour   Designation of Producer
     *
     * @throws ArrayIndexOutOfBoundsException While getting cityId and localityId
     */

    private void producerSignUp(String fullName, final String password, final String phone,
                                String stateId, String cityId, String localityId,
                                String dob, String adhar, String isLabour) {
        Call<TokenResponseProducerSignUp> producerSignUp = userManagementService.signUpProducer(fullName, password, phone,
                stateId, cityId, localityId, dob, adhar, isLabour);
        Log.d("Single", "Log Error ");

        producerSignUp.enqueue(new Callback<TokenResponseProducerSignUp>() {
            @Override
            public void onResponse(Call<TokenResponseProducerSignUp> call, Response<TokenResponseProducerSignUp> response) {
                TokenResponseProducerSignUp tokenResponseProducerSignUp = response.body();
                if (tokenResponseProducerSignUp.getStatus() == 200) {
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("phone",phone);
                    editor.putString("password",password);
                    editor.putBoolean("isProducer",true);
                    editor.putString("name",tokenResponseProducerSignUp.getName());
                    editor.putString("userId",""+tokenResponseProducerSignUp.getId());
                    editor.apply();
                    Log.d("Single","Login Data"+phone+password);
                    startActivity(new Intent(getApplicationContext(), ProducerHomeActivity.class));
                    Intent intent = new Intent("finish_main_activity");
                    sendBroadcast(intent);
                    Intent intent_two = new Intent("finish_producer_landing_activity");
                    sendBroadcast(intent_two);
                    finish();
                    TokenResponseProducerSignUp response1 = response.body();
                    Log.d("Single", "" + response1.getName() + response1.getStatus());
                }else {
                    Toast.makeText(getApplicationContext(),tokenResponseProducerSignUp.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponseProducerSignUp> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * A function to get available cities from the database for spinners data.
     *
     * @throws ArrayIndexOutOfBoundsException While getting cityId
     */

    private void getCities() {
        final Call<List<Location>> city = userManagementService.getCities();
        city.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                cities = response.body();
                citySize = cities.size();
                cityName.add("Choose City");
                for (int i = 1; i <= citySize; i++) {
                    cityName.add(cities.get(i - 1).getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, cityName);
                spinnerCity.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }

    /**
     * A function to get available Localities from the database for spinners data.
     *
     * @throws ArrayIndexOutOfBoundsException While getting localityId
     */

    private void getLocalities() {
        final Call<List<Location>> locality = userManagementService.getLocalities();
        locality.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                localities = response.body();
                localitySize = localities.size();
                Log.d("Single", "" + localities.size());
                LocalitiesName.add("Choose Locality");
                for (int i = 1; i <= localitySize; i++) {
                    LocalitiesName.add(localities.get(i - 1).getName());
                    Log.d("Localities", LocalitiesName.get(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, LocalitiesName);
                spinnerLocality.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }


    //On Click Listener for signUp Button
    View.OnClickListener signUpButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProducerSignupActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_otp_verification,null);
            mBuilder.setView(mView);
            otpDialog = mBuilder.create();

            otpView = mView.findViewById(R.id.pinview);
            otpProgressBar = mView.findViewById(R.id.otpProgressBar);

            Button verifyOtpButton = mView.findViewById(R.id.verify_otp_button);
            verifyOtpButton.setOnClickListener(verifyOtpListener);

            TextView resendButton = mView.findViewById(R.id.resend_otp);
            resendButton.setOnClickListener(resendOtpListener);

            String fullname = fullName.getText().toString().trim();
            boolean isvalidName = new Validator().validInput(fullname);
            Log.d(TAG, "" + isvalidName + fullname);
            String pass = password.getText().toString().trim();
            boolean isValidPassword = new Validator().isValidPassword(pass);
            phone = mobileNumber.getText().toString().trim();
            boolean isValidPhone = new Validator().isValidPhone(phone);
            String dateOfBirth = dob.getText().toString().trim();
            boolean isValidDob = new Validator().validInput(dateOfBirth);
            String adhar = aadhar.getText().toString().trim();
            boolean isValidAdhar = new Validator().isvalidAadhar(adhar);
            boolean isValidLabour = new Validator().validInput(islabour);

            if (!isvalidName) {
                fullName.setError("Name must not be empty!");
                Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
            } else if (!isValidPassword) {
                password.setError("Please Enter a Valid Password(at least one letter (a-z or A-Z) and one number(0-9)[Length: 6-15]");
                Toast.makeText(getApplicationContext(),"Please Enter a valid password",Toast.LENGTH_SHORT).show();
            } else if (!isValidPhone) {
                mobileNumber.setError("Please Enter a valid Phone Number");
                Toast.makeText(getApplicationContext(), "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
            } else if (!isValidDob) {
                dob.setError("Please Enter your Date of Birth");
                Toast.makeText(getApplicationContext(), "Please Enter your Date of Birth", Toast.LENGTH_SHORT).show();
            } else if (!isValidAdhar) {
                aadhar.setError("Please enter a valid Aadhar Number");
                Toast.makeText(getApplicationContext(), "Please Enter a valid Aadhar Number", Toast.LENGTH_SHORT).show();
            }else if (!isValidLabour) {
                Toast.makeText(getApplicationContext(), "Please Choose! Are You a mason or labour", Toast.LENGTH_SHORT).show();
            }else {
                 getOtp(phone);
                 otpDialog.show();
            }
        }
    };

    View.OnClickListener dateOfBirthListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    ProducerSignupActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    onDateSetListener,
                    year, month, day
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    };

    View.OnClickListener resendOtpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            otpProgressBar.setVisibility(View.VISIBLE);
            getOtp(phone);
        }
    };

    View.OnClickListener verifyOtpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            otpProgressBar.setVisibility(View.VISIBLE);
            try {
                String otp = otpView.getText().toString();
                verifyOtp(phone,otp);
            }catch (Exception e){
                otpView.setError("must not be empty!");
            }
        }
    };

    /**
     * Function to get Age From DOB
     *
     * @param year Year Of Birth
     * @param month Month Of Birth
     * @param day Day Of Birth
     *
     * @return Age
     */

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

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
    private void verifyOtp(String phone, String otp){
        Call<HttpResponse> verifyOtpCall = otpVerificationService.verifyOtp(phone,otp);

        verifyOtpCall.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                otpProgressBar.setVisibility(View.GONE);
                try {
                    HttpResponse httpResponse = response.body();
                    Toast.makeText(getApplicationContext(),httpResponse.getMessage(),Toast.LENGTH_LONG).show();
                    if (httpResponse.getStatus() == 200){
                        otpDialog.hide();
                        String fullname = fullName.getText().toString().trim();
                        String pass = password.getText().toString().trim();
                        String phone = mobileNumber.getText().toString().trim();
                        String stateId = "1";
                        String dateOfBirth = dob.getText().toString().trim();
                        String adhar = aadhar.getText().toString().trim();

                        try {
                            String cityId = "" + cities.get(spinnerCity.getSelectedIndex() - 1).getId();
                            String localityId = "" + localities.get(spinnerLocality.getSelectedIndex() - 1).getId();
                            producerSignUp(fullname, pass, phone, stateId, cityId, localityId, dateOfBirth, adhar, islabour);
                        }catch (ArrayIndexOutOfBoundsException e){
                            Toast.makeText(getApplicationContext(), "Please Choose City and Locality", Toast.LENGTH_SHORT).show();
                        }
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


}