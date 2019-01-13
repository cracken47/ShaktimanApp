package com.alabhya.Shaktiman.models.VendorSignup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenRequest {
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("cityId")
    @Expose
    private String cityId;
    @SerializedName("localityId")
    @Expose
    private String localityId;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("adharCard")
    @Expose
    private String adharCard;
    @SerializedName("isLabour")
    @Expose
    private String isLabour;


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String  stateId) {
        this.stateId = stateId;
    }

    public String  getCityId() {
        return cityId;
    }

    public void setCityId(String  cityId) {
        this.cityId = cityId;
    }

    public String  getLocalityId() {
        return localityId;
    }

    public void setLocalityId(String  localityId) {
        this.localityId = localityId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAdharCard() {
        return adharCard;
    }

    public void setAdharCard(String adharCard) {
        this.adharCard = adharCard;
    }

    public String  getIsLabour() {
        return isLabour;
    }

    public void setIsLabour(String  isLabour) {
        this.isLabour = isLabour;
    }
}
