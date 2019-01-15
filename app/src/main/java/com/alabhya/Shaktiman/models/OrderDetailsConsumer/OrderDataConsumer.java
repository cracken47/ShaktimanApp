package com.alabhya.Shaktiman.models.OrderDetailsConsumer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDataConsumer {
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("localityName")
    @Expose
    private String localityName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("cityId")
    @Expose
    private String cityId;
    @SerializedName("localityId")
    @Expose
    private String localityId;
    @SerializedName("flat")
    @Expose
    private String flat;
    @SerializedName("streetName")
    @Expose
    private String streetName;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("contactPhone")
    @Expose
    private String contactPhone;
    @SerializedName("workDescription")
    @Expose
    private String workDescription;
    @SerializedName("producersQuantity")
    @Expose
    private String producersQuantity;
    @SerializedName("confirmedProducers")
    @Expose
    private String confirmedProducers;
    @SerializedName("workDate")
    @Expose
    private String workDate;
    @SerializedName("estimate")
    @Expose
    private String estimate;
    @SerializedName("addedAt")
    @Expose
    private String addedAt;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getLocalityId() {
        return localityId;
    }

    public void setLocalityId(String localityId) {
        this.localityId = localityId;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getProducersQuantity() {
        return producersQuantity;
    }

    public void setProducersQuantity(String producersQuantity) {
        this.producersQuantity = producersQuantity;
    }

    public String getConfirmedProducers() {
        return confirmedProducers;
    }

    public void setConfirmedProducers(String confirmedProducers) {
        this.confirmedProducers = confirmedProducers;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

}
