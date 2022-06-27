package com.horizam.sapid.networking.request

import com.google.gson.annotations.SerializedName
import java.io.File

data class UpdateProfileRequest(
    @SerializedName("username") val userName: String? = null,
    @SerializedName("first_name") val name: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("fcm_token") val fcm_token: String? = null,
    @SerializedName("dob") val dob: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("private") val private: Int? = null,
    @SerializedName("photo") val photo: File? = null

)