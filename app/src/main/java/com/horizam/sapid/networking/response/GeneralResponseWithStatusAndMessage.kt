package com.horizam.sapid.networking.response

import com.google.gson.annotations.SerializedName


data class GeneralResponseWithStatusAndMessage(

    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String
)