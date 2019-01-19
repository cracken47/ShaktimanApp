package com.alabhya.Shaktiman.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabhya.Shaktiman.OrderDetails.ProducerOrderFullDetailsActivity;
import com.alabhya.Shaktiman.R;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDataProducer;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDetailsProducer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProducerOrderDetailsAdapter extends RecyclerView.Adapter<ProducerOrderDetailsAdapter.MyViewHolder> {
    private OrderDetailsProducer dataSet;
    private int mLastClicked = 0;
    List<OrderDataProducer> orderDataProducers;
    Context ctx;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView orderId;
        TextView isActive;
        TextView producerQuantity;
        TextView dateOfWork;
        TextView orderDate;
        TextView locality;
        TextView contact;
        CardView cardView;
        private long mLastClicked = 0;
        private List<OrderDataProducer> orderDataProducers;
        Context ctx;

        public MyViewHolder(View itemView, Context ctx,List<OrderDataProducer> orderDataProducers) {
            super(itemView);
            itemView.findViewById(R.id.producer_order_list_viewDetailsButton).setOnClickListener(this);
            this.orderDataProducers = orderDataProducers;
            this.ctx = ctx;
            orderId = itemView.findViewById(R.id.producer_order_list_orderId);
            isActive = itemView.findViewById(R.id.isActiveProducerOrder);
            producerQuantity = itemView.findViewById(R.id.producer_order_list_producerQuant);
            dateOfWork = itemView.findViewById(R.id.producer_order_list_workDate);
            orderDate = itemView.findViewById(R.id.producer_order_list_orderDate);
            locality = itemView.findViewById(R.id.producer_order_list_locality);
            contact = itemView.findViewById(R.id.producer_order_list_contact);
            cardView = itemView.findViewById(R.id.cardView6);
        }

        @Override
        public void onClick(View view) {


            int position = getAdapterPosition();

            if (SystemClock.elapsedRealtime()-mLastClicked<1000){
                return;
            }
            mLastClicked = SystemClock.elapsedRealtime();
            OrderDataProducer orderDataProducer = this.orderDataProducers.get(position);
            Intent intent = new Intent(ctx,ProducerOrderFullDetailsActivity.class);
            intent.putExtra("state",orderDataProducer.getStateName());
            intent.putExtra("city",orderDataProducer.getCityName());
            intent.putExtra("id",orderDataProducer.getId());
            intent.putExtra("consumer",orderDataProducer.getConsumer());
            intent.putExtra("locality",orderDataProducer.getLocalityName());
            intent.putExtra("isActive",orderDataProducer.getIsActive());
            intent.putExtra("flat",orderDataProducer.getFlat());
            intent.putExtra("area",orderDataProducer.getArea());
            intent.putExtra("landmark",orderDataProducer.getLandmark());
            intent.putExtra("addedAt",orderDataProducer.getAddedAt());
            intent.putExtra("WorkDate",orderDataProducer.getWorkDate());
            intent.putExtra("producers",orderDataProducer.getProducersQuantity());
            intent.putExtra("confirmed",orderDataProducer.getConfirmedProducers());
            intent.putExtra("WorkDesc",orderDataProducer.getWorkDescription());
            intent.putExtra("phoneNumber",orderDataProducer.getContactPhone());
            intent.putExtra("accepted",orderDataProducer.getAccepted());
            this.ctx.startActivity(intent);
        }
    }

    public ProducerOrderDetailsAdapter(OrderDetailsProducer data,Context ctx) {
        this.dataSet = data;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.producer_order_list, parent, false);

        orderDataProducers = dataSet.getData();

        MyViewHolder myViewHolder = new MyViewHolder(view,ctx,orderDataProducers);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        orderDataProducers = dataSet.getData();

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

        Date d = null;
        try
        {
            d = input.parse(orderDataProducers.get(listPosition).getAddedAt());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        holder.orderId.setText("Order Id: "+orderDataProducers.get(listPosition).getId());
        int isActive = Integer.parseInt(orderDataProducers.get(listPosition).getIsActive());
        int isAccepted = Integer.parseInt(orderDataProducers.get(listPosition).getAccepted());

        if(isActive==0){
            holder.isActive.setText("Not Active");
        }else holder.isActive.setText("Active");

        if(isActive==1 && isAccepted ==1){
            holder.cardView.setBackgroundColor(Color.parseColor("#81c784"));
            holder.producerQuantity.setText("Accepted");
            holder.producerQuantity.setTextColor(Color.RED);
        }else holder.producerQuantity.setText("To Accept");

        holder.dateOfWork.setText("Work Date: "+orderDataProducers.get(listPosition).getWorkDate());
        holder.locality.setText("Locality: "+orderDataProducers.get(listPosition).getLocalityName());
        holder.orderDate.setText("Order Date: "+formatted);
        holder.contact.setText("Contact: "+ orderDataProducers.get(listPosition).getContactPhone());

    }

    @Override
    public int getItemCount() {
        return dataSet.getData().size();
    }
}
