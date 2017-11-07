package com.vendumedia.atlit.api

import com.vendumedia.atlit.App
import com.vendumedia.atlit.model.City
import com.vendumedia.atlit.model.District
import com.vendumedia.atlit.model.Province
import com.vendumedia.atlit.model.Village
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import io.reactivex.Observable
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by aldimaulana on 11/7/17.
 */
interface Indonesia {

    @GET("indonesia/provinces")
    @Headers("Content-Type: application/json")
    fun province(): Observable<Province.Response>

    @GET("indonesia/cities/{id}")
    @Headers("Content-Type: application/json")
    fun city(@Path("id") id: Int): Observable<City.Response>

    @GET("indonesia/districts/{id}")
    @Headers("Content-Type: application/json")
    fun district(@Path("id") id: Int): Observable<District.Response>

    @GET("indonesia/villages/{id}")
    @Headers("Content-Type: application/json")
    fun village(@Path("id") id: Int): Observable<Village.Response>

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