package com.miss.myretrofit.api


import com.miss.myretrofit.retrofit.annotation.Field
import com.miss.myretrofit.retrofit.annotation.GET
import com.miss.myretrofit.retrofit.annotation.POST
import com.miss.myretrofit.retrofit.annotation.Query
import okhttp3.Call

interface EnjoyWeatherApi {
    @POST("/v3/weather/weatherInfo")
    fun postWeather(
        @Field("city") city: String?,
        @Field("key") key: String?
    ): Call?

    @GET("/v3/weather/weatherInfo")
    fun getWeather(
        @Query("city") city: String?,
        @Query("key") key: String?
    ): Call?
}