package com.alabhya.Shaktiman.PlaceOrders;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.ConsumerMainView.ConsumerHomeActivity;
import com.alabhya.Shaktiman.MainActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.OrderManagementService;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.HttpResponse;
import com.alabhya.Shaktiman.utils.Validator;


import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPlaceActivity extends AppCompatActivity {

    private HashMap<String,String> hashMapLabour = new HashMap<>();
    private HashMap<String,String> hashMapMason = new HashMap<>();
    private HashMap<String,String> hashMapCombined = new HashMap<>();
    private OrderManagementService orderManagementService;
    private UserManagementService userManagementService;
    private EditText flat;
    private EditText streetName;
    private EditText area;
    private EditText landmark;
    private EditText contactNumber;
    private EditText workDesc;
    private TextView workDate;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Button placeOrderButton;
    private long mLastClicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);
        flat = findViewById(R.id.place_order_flat);
        streetName = findViewById(R.id.place_order_street);
        area = findViewById(R.id.place_order_area);
        landmark = findViewById(R.id.place_order_landmark);
        contactNumber = findViewById(R.id.place_order_contact);
        workDesc = findViewById(R.id.place_order_workDesc);
        workDate = findViewById(R.id.place_order_workDate);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.drawable.ic_sort_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle("Place Order");

        SharedPreferences sharedPreferences = getSharedPreferences("ProducerQuantity",Context.MODE_PRIVATE);

        String[] masonId =  sharedPreferences.getString("masonId","null").split(",");
        String[] labourId = sharedPreferences.getString("labourId","null").split(",");

        for(int i=0;i<masonId.length;i++){
            hashMapCombined.put("producersId["+i+"]",masonId[i]);
            Log.d("Single",""+hashMapCombined.get("producersId[0]"));
        }

        for(int i=0;i<labourId.length;i++){
            hashMapLabour.put("producersId["+i+masonId.length+"]",labourId[i]);
            Log.d("Single","labour"+labourId[i]);
        }

        hashMapCombined.putAll(hashMapLabour);
        hashMapCombined.putAll(hashMapMason);

        Log.d("Single","Size"+hashMapCombined.size());

        orderManagementService = ApiClient.getRetrofitClient().create(OrderManagementService.class);
        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);


        placeOrderButton.setOnClickListener(placeOrderButtonListener);


        workDate.setOnClickListener(dateOfBirthListener);

        //Update date using the dialog of Calendar Instance
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date;


                if (month < 10 && dayOfMonth >= 10) {
                    date = year + "-0" + month + "-" + dayOfMonth;
                } else if (dayOfMonth < 10 && month >= 10) {
                    date = year + "-" + month + "-0" + dayOfMonth;
                } else if (month < 10 && dayOfMonth < 10) {
                    date = year + "-0" + month + "-0" + dayOfMonth;
                } else {
                    date = year + "-" + month + "-" + dayOfMonth;
                }
                workDate.setText(date);
            }
        };
    }

    private void placeOrder( String userId, final String stateId, final String cityId,
                             String localityId, String flat, String streetName,
                             String contact, String workDesc, String workDate, String producerQuantity,
                             String area,String landmark,HashMap<String,String> hashMapProducerId
                            )
    {
        Call<HttpResponse> placeOrderCall = orderManagementService.placeOrder(userId,stateId,cityId,localityId,
                                          flat,streetName,contact,workDesc,workDate,
                                          producerQuantity,area,landmark,hashMapProducerId);

        placeOrderCall.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                HttpResponse httpResponse = response.body();

                Toast.makeText(getApplicationContext(), httpResponse.getMessage(),Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),ConsumerHomeActivity.class));
                SharedPreferences quantity = getSharedPreferences("ProducerQuantity",Context.MODE_PRIVATE);
                SharedPreferences.Editor quantityEditor = quantity.edit();
                quantityEditor.clear();
                quantityEditor.apply();
                Intent consumerHome = new Intent("finish_consumer_home_activity");
                sendBroadcast(consumerHome);
                Intent masonList = new Intent("finish_mason_list_activity");
                sendBroadcast(masonList);
                Intent labourList = new Intent("finish_labour_list_activity");
                sendBroadcast(labourList);

                SharedPreferences sharedPreferences = getSharedPreferences("ProducerQuantity",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                finish();
            }

            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {

            }
        });
    }


    View.OnClickListener dateOfBirthListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    OrderPlaceActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    onDateSetListener,
                    year, month, day
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    };

    View.OnClickListener placeOrderButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(OrderPlaceActivity.this);
            mBuilder.setMessage("Are you sure?").setPositiveButton("Yes",dialogClickListener)
                    .setNegativeButton("No",dialogClickListener).show();


        }
    };

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i){
                case DialogInterface.BUTTON_POSITIVE:
                {
                    String userid,stateid,Flat,street,contact,workdesc,workdate;
                    String producerQuant,Area,Landmark;
                    HashMap<String,String> hashMap;

                    stateid = "1";
                    Flat = flat.getText().toString().trim();
                    street = streetName.getText().toString().trim();
                    contact = contactNumber.getText().toString().trim();
                    workdesc = workDesc.getText().toString().trim();
                    workdate = workDate.getText().toString().trim();
                    Area = area.getText().toString().trim();
                    Landmark = landmark.getText().toString().trim();
                    hashMap = hashMapCombined;
                    producerQuant = ""+hashMapCombined.size();
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
                    userid = sharedPreferences.getString("userId","n/a");
                    Log.d("Single",userid);

                    Validator validator = new Validator();

                    if(!validator.validInput(street)){
                        streetName.setError("Street name must not be empty!");
                        Toast.makeText(getApplicationContext(), "Please Enter Street Name", Toast.LENGTH_SHORT).show();
                    }else if(!validator.validInput(Area)){
                        area.setError("Area must not be empty!");
                        Toast.makeText(getApplicationContext(), "Please Enter Area", Toast.LENGTH_SHORT).show();
                    }else if(!validator.isValidPhone(contact)){
                        contactNumber.setError("Please Enter a valid Mobile Number");
                        Toast.makeText(getApplicationContext(), "Please Enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
                    }else if(!validator.validInput(workdesc)){
                        workDesc.setError("Work description must not be empty!");
                        Toast.makeText(getApplicationContext(), "Please Enter Work Description", Toast.LENGTH_SHORT).show();
                    }else if (!validator.validInput(workdate)){
                        Toast.makeText(getApplicationContext(), "Please choose a Work date", Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            SharedPreferences sharedPreferences1 = getSharedPreferences("ConsumerLocationInfo",0);
                            String cityId = sharedPreferences1.getString("cityId","0");
                            String localityId = sharedPreferences1.getString("localityId","0");
                            placeOrder(userid,stateid,cityId,localityId,Flat,street,contact,workdesc,workdate
                                    ,producerQuant,Area,Landmark,hashMap);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Please Choose City and Locality", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                case DialogInterface.BUTTON_NEGATIVE:
                {

                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
