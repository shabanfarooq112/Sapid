package com.horizam.sapid.networking.response

import com.google.gson.annotations.SerializedName

class UserLoginResponse(
        @SerializedName("status") val status : Int,
        @SerializedName("message") val message : String,
        @SerializedName("data") val userLoginData : UserLoginData
)

data class UserLoginData (
        @SerializedName("id") val id : Int,
        @SerializedName("name") val name : String,
        @SerializedName("email") val email : String,
        @SerializedName("username") val username : String,
        @SerializedName("phone") val phone : String,
        @SerializedName("photo") val photo : String,
        @SerializedName("status") val status : Int,
        @SerializedName("address") val address : String,
        @SerializedName("gender") val gender : Int,
        @SerializedName("dob") val dob : String,
        @SerializedName("private") val private : Int,
        @SerializedName("fcm_token") val fcm_token : String,
        @SerializedName("created_at") val created_at : String,
        @SerializedName("updated_at") val updated_at : String,
        @SerializedName("token") val token : String
)