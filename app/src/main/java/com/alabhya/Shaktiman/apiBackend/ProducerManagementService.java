package com.alabhya.Shaktiman.apiBackend;

import com.alabhya.Shaktiman.models.Producer;
import com.alabhya.Shaktiman.models.ProducerSignIn.TokenResponseProducerLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ProducerManagementService {

    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("getProducersLabour.php")
    Call<List<Producer>> getLabour
            (   @Field("state") String state,
                @Field("city") String city,
                @Field("locality") String locality
            );

    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("getProducersMason.php")
    Call<List<Producer>> getMason
            (   @Field("state") String state,
                @Field("city") String city,
                @Field("locality") String locality
            );
}
