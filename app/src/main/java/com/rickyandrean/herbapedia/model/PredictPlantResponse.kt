package com.rickyandrean.herbapedia.model

import com.google.gson.annotations.SerializedName

data class PredictPlantResponse (
    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("success")
    val success: String,

    @field:SerializedName("plant_id")
    val plantId: Int
)