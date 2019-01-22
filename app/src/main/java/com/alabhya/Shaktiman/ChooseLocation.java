package com.alabhya.Shaktiman;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.alabhya.Shaktiman.apiBackend.ApiClient;
import com.alabhya.Shaktiman.apiBackend.UserManagementService;
import com.alabhya.Shaktiman.models.Location;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

public class ChooseLocation extends AppCompatActivity {

    private NiceSpinner spinnerCity, spinnerState, spinnerLocality;
    private UserManagementService userManagementService;
    private List<Location> cities;
    private List<Location> localities;
    private List<String> cityName = new ArrayList<>();
    private List<String> LocalitiesName = new ArrayList<>();
    private List<String> StateName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        userManagementService = ApiClient.getRetrofitClient().create(UserManagementService.class);

        spinnerCity = findViewById(R.id.citySpinner);
        spinnerLocality = findViewById(R.id.localitySpinner);
        spinnerState = findViewById(R.id.stateSpinner);

        ImageView backButton = findViewById(R.id.choose_location_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StateName.add("Choose State");
        StateName.add("Uttar Pradesh");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, StateName);
        spinnerState.setAdapter(adapter);

        getCities();
        getLocalities();

        Button confirmLocation = findViewById(R.id.confirm_location_button);
        confirmLocation.setOnClickListener(confirmButtonListener);


    }

    private void getCities() {
        final Call<List<Location>> city = userManagementService.getCities();
        city.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                cities = response.body();
                cityName.add("Choose City");
                for (int i = 1; i <= cities.size(); i++) {
                    cityName.add(cities.get(i - 1).getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, cityName);
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
                Log.d("Single", "" + localities.size());
                LocalitiesName.add("Choose Locality");
                for (int i = 1; i <= localities.size(); i++) {
                    LocalitiesName.add(localities.get(i - 1).getName());
                    Log.d("Localities", LocalitiesName.get(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, LocalitiesName);
                spinnerLocality.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }

    View.OnClickListener confirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences sharedPreferences = getSharedPreferences("ConsumerLocationInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                editor.putString("stateId", "1");
                editor.putString("cityId", "" + cities.get(spinnerCity.getSelectedIndex() - 1).getId());
                editor.putString("localityId", "" + localities.get(spinnerLocality.getSelectedIndex() - 1).getId());
                editor.putString("city", cities.get(spinnerCity.getSelectedIndex() - 1).getName());
                editor.putString("locality", localities.get(spinnerLocality.getSelectedIndex() - 1).getName());
                editor.apply();
                finish();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please choose your Location", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
