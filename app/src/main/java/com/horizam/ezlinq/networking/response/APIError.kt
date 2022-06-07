package com.horizam.ezlinq.networking.response

import com.google.gson.annotations.SerializedName

data class APIError(
    @SerializedName("message") val message : String
)