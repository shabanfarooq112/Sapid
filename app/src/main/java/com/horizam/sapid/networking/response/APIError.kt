package com.horizam.sapid.networking.response

import com.google.gson.annotations.SerializedName

data class APIError(
    @SerializedName("message") val message : String
)