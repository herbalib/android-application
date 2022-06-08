package com.rickyandrean.herbapedia.model

import com.google.gson.annotations.SerializedName

data class PlantsLocationResponse(
	@field:SerializedName("success")
	val success: String,

	@field:SerializedName("locations")
	val locations: List<PlantsLocationsItem>,

	@field:SerializedName("error")
	val error: String
)

data class PlantsLocationsItem(
	@field:SerializedName("plant_id")
	val plantId: Int,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("lat")
	val lat: Double
)
