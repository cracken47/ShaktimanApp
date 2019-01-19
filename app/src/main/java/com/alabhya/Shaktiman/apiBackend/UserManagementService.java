package com.alabhya.Shaktiman.apiBackend;


import com.alabhya.Shaktiman.models.ConsumerSignIn.TokenResponseConsumerSignIn;
import com.alabhya.Shaktiman.models.ConsumerSignUp.TokenResponseConsumerSignUp;
import com.alabhya.Shaktiman.models.Producer;
import com.alabhya.Shaktiman.models.Location;
import com.alabhya.Shaktiman.models.ProducerSignIn.TokenResponseProducerLogin;
import com.alabhya.Shaktiman.models.ProducerSignup.TokenResponseProducerSignUp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserManagementService {

    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp"})
    @GET("getProducersLabour.php")
    Call<List<Producer>> getProducersLabours();

    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp"})
    @GET("getProducersMason.php")
    Call<List<Producer>> getProducersMasons();

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
    Call<TokenResponseProducerSignUp> signUpProducer(
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
    Call<TokenResponseProducerLogin> signInProducer
        (   @Field("phone") String phone,
            @Field("password") String password
        );


    // @POST Request for Consumer SignUp
    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("getSignupConsumers.php")
    Call<TokenResponseConsumerSignUp> signUpConsumers
        (
            @Field("fullName") String fullName,
            @Field("phone") String phone,
            @Field("password") String password
        );

    // @POST Request for ProducerSignIn
    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("getLoginConsumers.php")
    Call<TokenResponseConsumerSignIn> signInConsumer
            (@Field("phone") String phone,
            @Field("password") String password
            );

}
