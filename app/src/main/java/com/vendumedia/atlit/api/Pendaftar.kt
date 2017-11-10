package com.vendumedia.atlit.api

import com.vendumedia.atlit.App
import com.vendumedia.atlit.model.Register
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by aldimaulana on 11/7/17.
 */
interface Pendaftar {

    @POST("pendaftar/store")
    @Headers("Content-Type: application/json")
    fun store(): Observable<Register.Response>

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