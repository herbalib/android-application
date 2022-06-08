package com.rickyandrean.herbapedia.model

import com.google.gson.annotations.SerializedName

data class AddLocationResponse(
    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("success")
    val success: String
)