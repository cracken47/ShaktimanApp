package com.alabhya.Shaktiman.models.OrderDetailsProducer;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsProducer {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<OrderDataProducer> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrderDataProducer> getData() {
        return data;
    }

    public void setData(List<OrderDataProducer> data) {
        this.data = data;
    }
}
