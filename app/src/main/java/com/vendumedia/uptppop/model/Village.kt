package com.vendumedia.uptppop.model

import com.google.gson.annotations.SerializedName

/**
 * Created by aldimaulana on 11/7/17.
 */

object Village {
	data class Response(
            @SerializedName("message") val message: String = "",
            @SerializedName("status") val status: Int = 0,
            @SerializedName("data") val data: Data = Data()
	)

	data class Data(
            @SerializedName("id") val id: Int = 0,
            @SerializedName("name") val name: String = "",
            @SerializedName("villages") val villages: List<Village> = listOf()
	)

	data class Village(
            @SerializedName("id") val id: Long = 0,
            @SerializedName("district_id") val districtId: String = "",
            @SerializedName("name") val name: String = ""
	)
}