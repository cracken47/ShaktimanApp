package com.alabhya.Shaktiman.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.models.Producers;

import java.util.List;

public class ProducerAdapter extends RecyclerView.Adapter<ProducerAdapter.MyViewHolder> {

    private List<Producers> producers;

    public ProducerAdapter (List<Producers> producers){
        this.producers = producers;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Name.setText(producers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return producers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        public MyViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.profileName);
        }
    }
}
