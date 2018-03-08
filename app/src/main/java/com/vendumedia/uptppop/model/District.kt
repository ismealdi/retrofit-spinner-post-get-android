package com.vendumedia.uptppop.model

import com.google.gson.annotations.SerializedName

/**
 * Created by aldimaulana on 11/7/17.
 */

object District {
	data class Response(
            @SerializedName("message") val message: String = "",
            @SerializedName("status") val status: Int = 0,
            @SerializedName("data") val data: Data = Data()
	)

	data class Data(
            @SerializedName("id") val id: Int = 0,
            @SerializedName("name") val name: String = "",
            @SerializedName("districts") val districts: List<District> = listOf()
	)

	data class District(
            @SerializedName("id") val id: Long = 0,
            @SerializedName("city_id") val cityId: String = "",
            @SerializedName("name") val name: String = ""
	)
}