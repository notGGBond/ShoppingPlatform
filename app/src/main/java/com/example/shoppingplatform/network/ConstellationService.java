package com.example.shoppingplatform.network;

import com.example.shoppingplatform.model.ConstellationData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConstellationService {

    @GET("constellation/getAll")
    Call<ConstellationData> getConstellationData(
            @Query("key") String key,
            @Query("consName") String consName,
            @Query("type") String type
    );

}
