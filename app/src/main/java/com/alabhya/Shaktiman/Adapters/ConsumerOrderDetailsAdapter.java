package com.alabhya.Shaktiman.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabhya.Shaktiman.OrderDetails.ConsumerOrderFullDetailActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDataConsumer;
import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDetailsConsumer;

import java.util.List;

public class ConsumerOrderDetailsAdapter extends RecyclerView.Adapter<ConsumerOrderDetailsAdapter.MyViewHolder> {

    private OrderDetailsConsumer dataSet;
    List<OrderDataConsumer> orderDataConsumers;
    Context ctx;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView orderId;
        TextView isActive;
        TextView producerQuantity;
        TextView dateOfWork;
        TextView workDesc;
        private List<OrderDataConsumer> orderDataConsumer;
        Context ctx;

        public MyViewHolder(View itemView, Context ctx,List<OrderDataConsumer> orderDataConsumer) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.orderDataConsumer = orderDataConsumer;
            this.ctx = ctx;
            orderId = itemView.findViewById(R.id.orderId);
            isActive = itemView.findViewById(R.id.isActive);
            producerQuantity = itemView.findViewById(R.id.producerQuantity);
            dateOfWork = itemView.findViewById(R.id.dateOfWork);
            workDesc = itemView.findViewById(R.id.workDesc);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            OrderDataConsumer orderDataConsumer = this.orderDataConsumer.get(position);
            Intent intent = new Intent(ctx,ConsumerOrderFullDetailActivity.class);
            intent.putExtra("state",orderDataConsumer.getStateName());
            intent.putExtra("city",orderDataConsumer.getCityName());
            intent.putExtra("id",orderDataConsumer.getId());
            intent.putExtra("locality",orderDataConsumer.getLocalityName());
            intent.putExtra("isActive",orderDataConsumer.getIsActive());
            intent.putExtra("flat",orderDataConsumer.getFlat());
            intent.putExtra("area",orderDataConsumer.getArea());
            intent.putExtra("landmark",orderDataConsumer.getLandmark());
            intent.putExtra("addedAt",orderDataConsumer.getAddedAt());
            intent.putExtra("WorkDate",orderDataConsumer.getWorkDate());
            intent.putExtra("producers",orderDataConsumer.getProducersQuantity());
            intent.putExtra("confirmed",orderDataConsumer.getConfirmedProducers());
            intent.putExtra("WorkDesc",orderDataConsumer.getWorkDescription());
            intent.putExtra("phoneNumber",orderDataConsumer.getContactPhone());
            this.ctx.startActivity(intent);
        }
    }

    public ConsumerOrderDetailsAdapter(OrderDetailsConsumer data,Context ctx) {
        this.dataSet = data;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.consumer_order_list, parent, false);

        orderDataConsumers = dataSet.getData();

        MyViewHolder myViewHolder = new MyViewHolder(view,ctx,orderDataConsumers);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        orderDataConsumers = dataSet.getData();
        holder.orderId.setText("Order Id: "+orderDataConsumers.get(listPosition).getId());
        int isActive = Integer.parseInt(orderDataConsumers.get(listPosition).getIsActive());
        if(isActive==0){
            holder.isActive.setText("Not Active");
        }else holder.isActive.setText("Active");
        holder.dateOfWork.setText("Date: "+orderDataConsumers.get(listPosition).getWorkDate());
        holder.producerQuantity.setText("Producer: "+orderDataConsumers.get(listPosition).getProducersQuantity());
        holder.workDesc.setText("Locality: "+orderDataConsumers.get(listPosition).getLocalityName());

    }

    @Override
    public int getItemCount() {
        return dataSet.getData().size();
    }
}
