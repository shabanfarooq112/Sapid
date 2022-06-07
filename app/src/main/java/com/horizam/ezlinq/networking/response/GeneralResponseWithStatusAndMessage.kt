package com.horizam.ezlinq.networking.response

import com.google.gson.annotations.SerializedName


data class GeneralResponseWithStatusAndMessage(

    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String
)