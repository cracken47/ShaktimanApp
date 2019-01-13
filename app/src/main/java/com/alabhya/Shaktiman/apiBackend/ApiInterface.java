package com.alabhya.Shaktiman.apiBackend;


import com.alabhya.Shaktiman.models.Location;
import com.alabhya.Shaktiman.models.Producers;
import com.alabhya.Shaktiman.models.VendorSignIn.TokenResponseLogin;
import com.alabhya.Shaktiman.models.VendorSignup.TokenResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp"})
    @GET("getProducers.php")
    Call<List<Producers>> getProducers();

    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp"})
    @GET("getCities.php")
    Call<List<Location>> getCities();

    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp"})
    @GET("getLocalities.php")
    Call<List<Location>> getLocalities();

    // @POST Request for ProducerSignUp
    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("getSignupProducers.php")
    Call<TokenResponse> signUpProducer(
            @Field("fullName") String fullName,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("stateId") String stateId,
            @Field("cityId") String cityId,
            @Field("localityId") String localityId,
            @Field("dob") String dob,
            @Field("adharCard") String adharCard,
            @Field("isLabour") String isLabour
            );

    // @POST Request for ProducerSignIn
    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("getLoginProducers.php")
    Call<TokenResponseLogin> signInProducer
            (@Field("phone") String phone,
             @Field("password") String password
            );
}
