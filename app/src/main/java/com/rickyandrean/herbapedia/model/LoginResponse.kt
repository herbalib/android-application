package com.rickyandrean.herbapedia.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("accessToken")
	val accessToken: String?,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("success")
	val success: String
)
