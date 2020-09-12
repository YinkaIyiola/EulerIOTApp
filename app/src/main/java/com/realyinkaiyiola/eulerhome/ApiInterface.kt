package com.realyinkaiyiola.eulerhome

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("api/status/update")
    @FormUrlEncoded
    fun saveDeviceStatus(
        @Field("fan") fan: Int,
        @Field("bulb") bulb: Int,
        @Field("tv") tv: Int
    ): Call<Device>

    @GET("status")
    fun getDeviceStatus(
        @Query("fan") fan: Int,
        @Query("bulb") bulb: Int,
        @Query("tv") tv: Int
    ): Call<Device>
}