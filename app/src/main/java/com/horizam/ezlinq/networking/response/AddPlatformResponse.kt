package com.horizam.ezlinq.networking.response

import com.google.gson.annotations.SerializedName

class AddPlatformResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Data
)

data class Data(

    @SerializedName("platform_id") val platform_id: Int,
    @SerializedName("path") val path: String,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("direct") val direct: Int,
    @SerializedName("label") val label: String
)