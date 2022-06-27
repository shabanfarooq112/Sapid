package com.horizam.sapid.networking.request

import com.google.gson.annotations.SerializedName

data class AddPlatformRequest(
    @SerializedName("platform_id") val platformId: Int,
    @SerializedName("path") val path: String,
    @SerializedName("label") val label: String?,
    @SerializedName("direct") val direct: Int = 0
)