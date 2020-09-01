package com.miss.myretrofit.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface WeatherApi {
    @POST("/v3/weather/weatherInfo")
    @FormUrlEncoded
    fun postWeather(
        @Field("city") city: String?,
        @Field("key") key: String?
    ): Call<ResponseBody?>?

    @GET("/v3/weather/weatherInfo")
    fun getWeather(
        @Query("city") city: String?,
        @Query("key") key: String?
    ): Call<ResponseBody?>?
}