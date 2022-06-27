package com.horizam.sapid.networking.response

import com.google.gson.annotations.SerializedName

data class UserProfileRespose(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UeserData
)


data class Categories (

    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("name_sv") var nameSv : String,
    @SerializedName("status") var status : String,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String,
    @SerializedName("platforms") var platforms : List<Platforms>

)


data class UeserData(
    @SerializedName("user") val user: User,
    @SerializedName("categories") val categories: List<Categories>

)

data class User(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("email") val email : String,
    @SerializedName("username") val username : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("photo") val photo : String,
    @SerializedName("status") val status : Int,
    @SerializedName("password") val password : String,
    @SerializedName("address") val address : String,
    @SerializedName("gender") val gender : Int,
    @SerializedName("tiks") val tiks : Int,
    @SerializedName("dob") val dob : String,
    @SerializedName("private") val private : Int,
    @SerializedName("verified") val verified : Int,
    @SerializedName("bio") val bio : String,
    @SerializedName("fcm_token") val fcm_token : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("connected") val connected : Int,
    @SerializedName("direct") val direct : Int
)

data class Platforms (

    @SerializedName("id") var id : Int,
    @SerializedName("title") var title : String,
    @SerializedName("icon") var icon : String,
    @SerializedName("input") var input : String,
    @SerializedName("baseURL") var baseURL : String,
    @SerializedName("pro") var pro : Int,
    @SerializedName("category_id") var categoryId : Int,
    @SerializedName("status") var status : Int,
    @SerializedName("contact_card") var contactCard : Int,
    @SerializedName("placeholder_en") var placeholderEn : String,
    @SerializedName("placeholder_sv") var placeholderSv : String,
    @SerializedName("description_en") var descriptionEn : String,
    @SerializedName("description_sv") var descriptionSv : String,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String,
    @SerializedName("path") var path : String,
    @SerializedName("direct") var direct : Int
)