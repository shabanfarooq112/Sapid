package com.horizam.ezlinq.networking.response

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var dataSearch: SearchData
)

data class SearchData(
    @SerializedName("connected") var connectedUser : List<ConnectedUser>
)

data class ConnectedUser(
    @SerializedName("id") var id: Int,
    @SerializedName("first_name") var firstName: String,
    @SerializedName("last_name") var lastName: String,
    @SerializedName("username") var username: String,
    @SerializedName("email") var email: String,
    @SerializedName("photo") var photo: String,
    @SerializedName("phone1") var phone1: String,
    @SerializedName("gender") var gender: Int,
    @SerializedName("tiks") var tiks: Int,
    @SerializedName("dob") var dob: String,
    @SerializedName("bio") var bio: String,
    @SerializedName("private") var private: Int,
    @SerializedName("status") var status: Int,
    @SerializedName("featured") var featured: Int,
    @SerializedName("verified") var verified: Int,
    @SerializedName("company_logo") var companyLogo: String,
    @SerializedName("connected") var connected: String
)