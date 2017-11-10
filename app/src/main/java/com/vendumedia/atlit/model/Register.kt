package com.vendumedia.atlit.model

import com.google.gson.annotations.SerializedName

/**
 * Created by aldimaulana on 11/7/17.
 */

object Register {
	data class Response(
            @SerializedName("message") val message: String = "", //Berhasil mendapatkan data provinsi!
            @SerializedName("status") val status: Int = 0, //200
            @SerializedName("data") val data: List<Data> = listOf()
	)

	data class Data(
            @SerializedName("id") val id: Int = 0, //11
            @SerializedName("name") val name: String = "" //Aceh
	)
}