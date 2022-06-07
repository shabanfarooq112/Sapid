package com.horizam.ezlinq.networking

import android.util.Log
import com.google.gson.GsonBuilder
import com.horizam.ezlinq.App
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.activities.PrefManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {


    const val API_BASE_URL = "${Constants.BASE_URL}api/"


    private val clientBuilder = OkHttpClient.Builder()
    private val okHttpClient = buildClient()
    private val retrofitBuilder = Retrofit.Builder()
    private val retrofit = buildRetrofit()

    /**
     * build and return okHttpClient
     */
    private fun buildClient(): OkHttpClient {
        var manager: PrefManager
        manager = PrefManager(App.getAppContext()!!);

        clientBuilder.addInterceptor(getLogginInterceptor())
            .addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${manager.getAccessToken()}")
                    .addHeader("Accept" , "application/json")
                    .addHeader("content-type" , "application/json")
                    .build()
                Log.d("#tokken123", Constants.STR_TOKEN)
                chain.proceed(newRequest)
            }.writeTimeout(60, TimeUnit.SECONDS).connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)

        return clientBuilder.build();
    }

    private fun getLogginInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    /**
     * build and return retrofit instance
     */
    private fun buildRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return retrofitBuilder
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * return the implementation of webservice interface
     */
    fun getApiEndpointImpl(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    fun <T> executeApi(call: Call<T>, apiListener: ApiListener<T>) {
        val callback = object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                if (call.isCanceled)
                    apiListener.onCancel()
                else
                    apiListener.onFailure(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (call.isCanceled) {
                    apiListener.onCancel()
                    return
                }
                if (response.isSuccessful) {
                    apiListener.onSuccess(response.body())
                } else {
                    val reason = DefaultErrorHandler(response).handleError()
                    apiListener.onFailure(reason)
                }
            }
        }

        call.enqueue(callback)
    }
}