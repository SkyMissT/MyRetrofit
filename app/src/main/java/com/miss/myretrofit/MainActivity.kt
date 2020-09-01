package com.miss.myretrofit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.miss.myretrofit.api.EnjoyWeatherApi
import com.miss.myretrofit.api.WeatherApi
import com.miss.myretrofit.retrofit.MyRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var weatherApi: WeatherApi? = null
    private val TAG = "MainActivity"
    private var enjoyWeatherApi: EnjoyWeatherApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder().baseUrl("https://restapi.amap.com")
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)

        val enjoyRetrofit: MyRetrofit= MyRetrofit.Builder().baseUrl("https://restapi.amap.com").build()
        enjoyWeatherApi = enjoyRetrofit.create(EnjoyWeatherApi::class.java)

    }

    operator fun get(view: View?) {
        val call =
            weatherApi!!.getWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b")
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    try {
                        val string = body!!.string()
                        Log.i(TAG, "onResponse get: $string")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        body!!.close()
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseBody?>,
                t: Throwable
            ) {
            }
        })
    }

    fun post(view: View?) {
        val call =
            weatherApi!!.postWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b")
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                val body = response.body()
                try {
                    val string = body!!.string()
                    Log.i(TAG, "onResponse post: $string")
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    body!!.close()
                }
            }

            override fun onFailure(
                call: Call<ResponseBody?>,
                t: Throwable
            ) {
            }
        })
    }

    fun enjoyGet(view: View?) {
        val call =
            enjoyWeatherApi!!.getWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b")
        call!!.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                Log.i(
                    TAG,
                    "onResponse enjoy get: " + response.body()!!.string()
                )
                response.close()
            }
        })
    }

    fun enjoyPost(view: View?) {
        val call =
            enjoyWeatherApi!!.postWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b")
        call!!.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                Log.i(
                    TAG,
                    "onResponse enjoy post: " + response.body()!!.string()
                )
                response.close()
            }
        })
    }

}