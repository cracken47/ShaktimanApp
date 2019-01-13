package com.alabhya.Shaktiman.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("stateId")
    @Expose
    private int stateId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isLive")
    @Expose
    private String isLive;
    @SerializedName("addedAT")
    @Expose
    private String addedAT;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsLive() {
        return isLive;
    }

    public void setIsLive(String isLive) {
        this.isLive = isLive;
    }

    public String getAddedAT() {
        return addedAT;
    }

    public void setAddedAT(String addedAT) {
        this.addedAT = addedAT;
    }

}

