package com.vendumedia.uptppop.api

import com.vendumedia.uptppop.App
import com.vendumedia.uptppop.model.Register
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by aldimaulana on 11/7/17.
 */
interface Pendaftar {

    @POST("pendaftar/store")
    @Headers("Content-Type: application/json")
    fun store(@Body data: Register.Data): Observable<Register.Response>

    companion object {
        fun create(): Pendaftar {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(App.API)
                    .build()

            return retrofit.create(Pendaftar::class.java)
        }
    }

}