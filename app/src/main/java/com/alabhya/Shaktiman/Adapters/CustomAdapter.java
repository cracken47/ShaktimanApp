package com.alabhya.Shaktiman.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alabhya.Shaktiman.MainActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDataConsumer;
import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDetailsConsumer;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<OrderDataConsumer> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            //initialize view
        }
    }

    public CustomAdapter(ArrayList<OrderDataConsumer> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.consumer_order_list, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        //set Text here
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
