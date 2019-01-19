package com.alabhya.Shaktiman.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Producer {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("cityId")
    @Expose
    private String cityId;
    @SerializedName("localtyId")
    @Expose
    private String localtyId;
    @SerializedName("isLabour")
    @Expose
    private String isLabour;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("ratings")
    @Expose
    private String ratings;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("adharCard")
    @Expose
    private String adharCard;
    @SerializedName("addedAt")
    @Expose
    private String addedAt;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getLocaltyId() {
        return localtyId;
    }

    public void setLocaltyId(String localtyId) {
        this.localtyId = localtyId;
    }

    public String getIsLabour() {
        return isLabour;
    }

    public void setIsLabour(String isLabour) {
        this.isLabour = isLabour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAdharCard() {
        return adharCard;
    }

    public void setAdharCard(String adharCard) {
        this.adharCard = adharCard;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

}
