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
import com.alabhya.Shaktiman.models.Producer;
import com.alabhya.Shaktiman.utils.AgeCalculator;
import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.List;

public class ProducerLabourAdapter extends RecyclerView.Adapter<ProducerLabourAdapter.MyViewHolder> {

    private List<Producer> producers;
    private String localityName;
    private Context context;
    int count = 0;
    private String age;
    HashMap<Integer,String> userId = new HashMap<>();

    public ProducerLabourAdapter(List<Producer> producers, Context ctx){
        this.producers = producers;
        this.context = ctx;
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
        if(Integer.parseInt(age)>=1) {
            holder.Age.setText("Age:  "+age);
        }else holder.Age.setText("");
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
        int labourQuantity = sharedPreferences.getInt("labourQuantity",0);
        public MyViewHolder(View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.profileName);
            checkBox = itemView.findViewById(R.id.labourCheckbox);
            Age = itemView.findViewById(R.id.AgeTextView);
            Locality = itemView.findViewById(R.id.localityTextView);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b && count <labourQuantity){
                        count++;
                        Log.d("Single",""+labourQuantity);
                        producers.get(getAdapterPosition()).setChecked(true);
                        userId.put(getAdapterPosition(),producers.get(getAdapterPosition()).getId());

                        if (count==labourQuantity) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String userid = Joiner.on(',').join(userId.values());
                            editor.putString("labourId",userid);
                            editor.apply();
                            Log.d("Single", sharedPreferences.getString("labourId", "def"));
                        }
                    }else if(!b) {
                        userId.remove(getAdapterPosition());
                        Log.d("Single",count-1+"");
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
