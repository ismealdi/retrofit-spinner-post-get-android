package com.vendumedia.uptppop.model

import com.google.gson.annotations.SerializedName

/**
 * Created by aldimaulana on 11/7/17.
 */

object City {
	data class Response(
			@SerializedName("message") val message: String = "",
			@SerializedName("status") val status: Int = 0,
			@SerializedName("data") val data: Data = Data()
	)

	data class Data(
			@SerializedName("id") val id: Int = 0,
			@SerializedName("name") val name: String = "",
			@SerializedName("cities") val cities: List<City> = listOf()
	)

	data class City(
			@SerializedName("id") val id: Int = 0,
			@SerializedName("province_id") val provinceId: String = "",
			@SerializedName("name") val name: String = ""
	)
}