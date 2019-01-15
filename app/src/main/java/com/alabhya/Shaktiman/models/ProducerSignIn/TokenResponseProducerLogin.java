package com.alabhya.Shaktiman.models.ProducerSignIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponseProducerLogin {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("stateId")
        @Expose
        private String stateId;
        @SerializedName("cityId")
        @Expose
        private String cityId;
        @SerializedName("localityId")
        @Expose
        private String localityId;
        @SerializedName("isLabour")
        @Expose
        private String isLabour;
        @SerializedName("isAvailable")
        @Expose
        private String isAvailable;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("password")
        @Expose
        private String password;
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

        public String getIsLabour() {
            return isLabour;
        }

        public void setIsLabour(String isLabour) {
            this.isLabour = isLabour;
        }

        public String getIsAvailable() {
            return isAvailable;
        }

        public void setIsAvailable(String isAvailable) {
            this.isAvailable = isAvailable;
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
