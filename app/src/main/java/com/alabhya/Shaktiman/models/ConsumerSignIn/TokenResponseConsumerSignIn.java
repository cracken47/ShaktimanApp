package com.alabhya.Shaktiman.models.ConsumerSignIn;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponseConsumerSignIn {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("addedAT")
    @Expose
    private String addedAT;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddedAT() {
        return addedAT;
    }

    public void setAddedAT(String addedAT) {
        this.addedAT = addedAT;
    }

}

