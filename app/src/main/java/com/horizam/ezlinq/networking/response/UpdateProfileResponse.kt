package com.horizam.ezlinq.networking.response

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
        @SerializedName("status") val status : Int,
        @SerializedName("message") val message : String,
        @SerializedName("data") val data : UpdateProfileRequest
)

data class UpdateProfileRequest(
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
        @SerializedName("bio") val bio : String,
        @SerializedName("fcm_token") val fcm_token : String,
        @SerializedName("created_at") val created_at : String,
        @SerializedName("updated_at") val updated_at : String

)