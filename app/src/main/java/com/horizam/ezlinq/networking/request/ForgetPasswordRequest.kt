package com.horizam.ezlinq.networking.request

import com.google.gson.annotations.SerializedName

data class ForgetPasswordRequest(
    @SerializedName("email") val email: String
)