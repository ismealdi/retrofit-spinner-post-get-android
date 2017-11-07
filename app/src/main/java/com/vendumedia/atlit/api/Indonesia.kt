package com.vendumedia.atlit.api

import com.vendumedia.atlit.App
import com.vendumedia.atlit.model.Province
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import io.reactivex.Observable
import retrofit2.http.Headers

/**
 * Created by aldimaulana on 11/7/17.
 */
interface Indonesia {

    @GET("indonesia/provinces")
    @Headers("Content-Type: application/json")
    fun province(): Observable<Province.Response>

    companion object {
        fun create(): Indonesia {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(App.API)
                    .build()

            return retrofit.create(Indonesia::class.java)
        }
    }

}