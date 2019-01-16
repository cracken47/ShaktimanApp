package com.alabhya.Shaktiman.apiBackend;

import com.alabhya.Shaktiman.models.OrderDetailsConsumer.OrderDetailsConsumer;
import com.alabhya.Shaktiman.models.OrderDetailsProducer.OrderDetailsProducer;
import com.alabhya.Shaktiman.models.PlaceOrder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OrderManagementService {


    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("myOrders.php")
    Call<OrderDetailsConsumer> getConsumerOrders
            (   @Field("userId") String userId
            );

    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("orderForMe.php")
    Call<OrderDetailsProducer> getProducerOrders
            (   @Field("userId") String userId
            );

    @FormUrlEncoded
    @Headers({"Authorization:674532","User-Agent:shaktiM@nApp","Content-Type:application/x-www-form-urlencoded"})
    @POST("placeOrder.php")
    Call<PlaceOrder> placeOrder
            (@Field("userId") String  userId,
             @Field("stateId") String stateId,
             @Field("cityId") String cityId,
             @Field("localityId") String localityId,
             @Field("flat") String flat,
             @Field("streetName") String streetName,
             @Field("contactPhone") String contactPhone,
             @Field("workDescription") String workDescription,
             @Field("workDate") String workDate,
             @Field("producersQuantity") String producersQuantity,
             @Field("area") String area,
             @Field("landmark") String landmark,
             @FieldMap HashMap<String, String> hashMap
             );
}
