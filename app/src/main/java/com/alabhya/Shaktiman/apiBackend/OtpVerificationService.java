package com.alabhya.Shaktiman.apiBackend;

import com.alabhya.Shaktiman.models.HttpResponse;
import com.alabhya.Shaktiman.models.Producer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OtpVerificationService {

    //Get Otp
    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("getOtp.php")
    Call<HttpResponse> getOtp
            (   @Field("phone") String phone
            );

    //Verify Otp

    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("otpCheck.php")
    Call<HttpResponse> verifyOtp
            (   @Field("phone") String phone,
                @Field("otp") String otp
            );
}
