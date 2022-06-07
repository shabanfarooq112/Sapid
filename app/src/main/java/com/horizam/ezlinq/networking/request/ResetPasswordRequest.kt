package com.horizam.ezlinq.networking.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("password") val password: String
)