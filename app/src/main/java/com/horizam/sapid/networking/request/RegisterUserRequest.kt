package com.horizam.sapid.networking.request

import com.google.gson.annotations.SerializedName

data class  RegisterUserRequest(
        val username  : String,
        val email : String,
        val phone1 :String,
        val password : String,
        @SerializedName("password_confirmation") val passwordConfirmation : String
)