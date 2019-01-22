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

public class ProducerLabourAdapter extends RecyclerView.Adapter<ProducerLabourAdapter.MyViewHolder> {

    private List<Producer> producers;
    private Context context;
    private int count=0;
    private List<Location> localities;
    private List<Location> cities;
    HashMap<Integer,String> userId = new HashMap<>();
    public ProducerLabourAdapter(List<Producer> producers, List<Location> localities, List<Location> cities, Context ctx){
        this.producers = producers;
        this.localities = localities;
        this.cities = cities;
        this.context = ctx;
        Log.d("Single","Adapter called");
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_list,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Producer prodModel = producers.get(position);
        holder.Name.setText(producers.get(position).getName());
        String age = new AgeCalculator().getAge(producers.get(position).getDob());
        holder.Age.setText("Age: "+age);
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

        if(prodModel.isChecked()){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
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
        int labourQuantity = sharedPreferences.getInt("labourQuantity",0);
        public MyViewHolder(View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.profileName);
            checkBox = itemView.findViewById(R.id.labourCheckbox);
            Age = itemView.findViewById(R.id.AgeTextView);
            Locality = itemView.findViewById(R.id.localityTextView);
            editor.putBoolean("isAllSelected",false);
            editor.apply();
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b && count <labourQuantity){
                        editor.putBoolean("isAllSelected",false);
                        editor.apply();
                        count++;
                        Log.d("Single",""+labourQuantity);
                        producers.get(getAdapterPosition()).setChecked(true);
                        userId.put(getAdapterPosition(),producers.get(getAdapterPosition()).getId());

                        if (count==labourQuantity) {
                            editor.putBoolean("isAllSelected",true);
                            String userid = Joiner.on(',').join(userId.values());
                            editor.putString("labourId",userid);
                            editor.apply();
                            Log.d("Single","if Nill"+producers.get(getAdapterPosition()).getLocaltyId());
                        }
                    }else if(!b) {
                        editor.putBoolean("isAllSelected",false);
                        editor.apply();
                        userId.remove(getAdapterPosition());
                        Log.d("Single",count-1+""+sharedPreferences.getBoolean("isAllSelected",true)+"");
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
