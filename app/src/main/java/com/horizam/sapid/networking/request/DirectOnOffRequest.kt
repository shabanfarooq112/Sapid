package com.horizam.sapid.networking.request

import com.google.gson.annotations.SerializedName

data class DirectOnOffRequest (
    @SerializedName("platform_id") val platformId: Int,
    @SerializedName("direct") val direct: Int = 0,
    @SerializedName("path") var path: String,
)