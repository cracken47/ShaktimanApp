package com.alabhya.Shaktiman.models.ConsumerSignUp;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponseConsumerSignUp {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

