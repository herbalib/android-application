package com.rickyandrean.herbapedia.model

import com.google.gson.annotations.SerializedName

data class AddLocationRequest (
    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("lon")
    val lon: Double,

    @field:SerializedName("plant_id")
    val plantId: Int,

    @field:SerializedName("description")
    val description: String,
)