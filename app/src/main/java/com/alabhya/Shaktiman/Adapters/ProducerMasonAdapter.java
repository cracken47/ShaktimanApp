package com.alabhya.Shaktiman.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.models.Location;
import com.alabhya.Shaktiman.models.Producer;
import com.alabhya.Shaktiman.utils.AgeCalculator;
import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.List;


public class ProducerMasonAdapter extends RecyclerView.Adapter<ProducerMasonAdapter.MyViewHolder>{
    private List<Producer> producers;
    private Context context;
    private int count = 0;
    List<Location> localities,cities;
    HashMap<Integer,String> userId = new HashMap<>();

    public ProducerMasonAdapter(List<Producer> producers, List<Location> localities, List<Location> cities, Context ctx){
        this.producers = producers;
        this.context = ctx;
        this.localities = localities;
        this.cities = cities;
    }
    @NonNull
    @Override
    public ProducerMasonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_list,parent,false);
        return new ProducerMasonAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProducerMasonAdapter.MyViewHolder holder, int position) {
        Producer prodModel = producers.get(position);
        holder.Name.setText(producers.get(position).getName());
        String age = new AgeCalculator().getAge(producers.get(position).getDob());
        holder.Age.setText("Age: "+age);
        if(prodModel.isChecked()){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }

        String localityId = producers.get(position).getLocaltyId();
        String localityName;
        String cityName="";
        String cityId = producers.get(position).getCityId();

        for (int i=0;i<cities.size();i++){
            String city = cities.get(i).getId();
            if (city.equals(cityId)){
                cityName = cities.get(i).getName();
                break;
            }
        }
        for (int i=0;i<localities.size();i++){
            String locality = localities.get(i).getId();
            if(locality.equals(localityId)){
                localityName = localities.get(i).getName();
                holder.Locality.setText(localityName+", "+cityName);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return producers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name;
        public CheckBox checkBox;
        public TextView Age;
        public TextView Locality;
        SharedPreferences sharedPreferences = context.getSharedPreferences("ProducerQuantity",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int masonQuantity = sharedPreferences.getInt("masonQuantity",0);
        public MyViewHolder(View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.profileName);
            checkBox = itemView.findViewById(R.id.labourCheckbox);
            Age = itemView.findViewById(R.id.AgeTextView);
            Locality = itemView.findViewById(R.id.localityTextView);

            editor.putBoolean("isAllMasonSelected",false);
            editor.apply();
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b && count <masonQuantity){
                        editor.putBoolean("isAllMasonSelected",false);
                        editor.apply();
                        count++;
                        Log.d("Single",""+masonQuantity);
                        producers.get(getAdapterPosition()).setChecked(true);
                        userId.put(getAdapterPosition(),producers.get(getAdapterPosition()).getId());

                        if (count==masonQuantity) {
                            editor.putBoolean("isAllMasonSelected",true);
                            editor.apply();
                            String userid = Joiner.on(',').join(userId.values());
                            editor.putString("masonId",userid);
                            editor.apply();
                            Log.d("Single", sharedPreferences.getString("masonId", "def"));
                        }
                    }else if(!b) {
                        editor.putBoolean("isAllMasonSelected",false);
                        editor.apply();
                        userId.remove(getAdapterPosition());
                        count--;
                    }else {
                        checkBox.setChecked(false);
                        producers.get(getAdapterPosition()).setChecked(false);
                    }
                }
            });
        }
    }
}
