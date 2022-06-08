package com.rickyandrean.herbapedia.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PlantResponse(
	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("success")
	val success: String,

	@field:SerializedName("plants")
	val plants: List<PlantsItem>
)

@Parcelize
data class PlantsItem(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("latin_name")
	val latinName: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("consumption")
	val consumption: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("ref")
	val ref: String,

	@field:SerializedName("benefits")
	val benefits: List<BenefitsItem>,

	@field:SerializedName("nutritions")
	val nutritions: List<NutritionsItem>,

	@field:SerializedName("locations")
	val locations: List<LocationsItem?>? = null
): Parcelable

@Parcelize
data class BenefitsItem(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("plant_id")
	val plantId: Int
): Parcelable

@Parcelize
data class NutritionsItem(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("plant_id")
	val plantId: Int
): Parcelable

@Parcelize
data class LocationsItem(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("plant_id")
	val plant_id: Int? = null
): Parcelable


