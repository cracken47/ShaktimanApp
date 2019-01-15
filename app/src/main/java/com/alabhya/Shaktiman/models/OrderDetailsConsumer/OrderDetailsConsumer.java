package com.alabhya.Shaktiman.models.OrderDetailsConsumer;

import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDataConsumer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsConsumer {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<OrderDataConsumer> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrderDataConsumer> getData() {
        return data;
    }

    public void setData(List<OrderDataConsumer> data) {
        this.data = data;
    }
}
