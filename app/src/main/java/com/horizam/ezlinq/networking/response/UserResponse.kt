package com.horizam.ezlinq.networking.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("profile") var profile: Profile? = Profile()
)

data class Profile(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("email_verified_at") var emailVerifiedAt: String? = null,
    @SerializedName("photo") var photo: String? = null,
    @SerializedName("mobile1") var mobile1: String? = null,
    @SerializedName("mobile2") var mobile2: String? = null,
    @SerializedName("phone1") var phone1: String? = null,
    @SerializedName("phone2") var phone2: String? = null,
    @SerializedName("company_logo") var companyLogo: String? = null,
    @SerializedName("company_name") var companyName: String? = null,
    @SerializedName("company_address") var companyAddress: String? = null,
    @SerializedName("job_title") var jobTitle: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("tiks") var tiks: Int? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("private") var private: Int? = null,
    @SerializedName("social_media_enable") var socialMediaEnable: String? = null,
    @SerializedName("bio") var bio: String? = null,
    @SerializedName("fcm_token") var fcmToken: String? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lng") var lng: String? = null,
    @SerializedName("web_url") var webUrl: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("connected") var connected: Int? = null,
    @SerializedName("direct") var direct: Int? = null,
    @SerializedName("user_platforms") var userPlatforms: ArrayList<UserPlatforms> = arrayListOf()
)

data class UserPlatforms(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("icon") var icon: String? = null,
    @SerializedName("input") var input: String? = null,
    @SerializedName("baseURL") var baseURL: String? = null,
    @SerializedName("pro") var pro: Int? = null,
    @SerializedName("category_id") var categoryId: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("contact_card") var contactCard: Int? = null,
    @SerializedName("placeholder_en") var placeholderEn: String? = null,
    @SerializedName("placeholder_sv") var placeholderSv: String? = null,
    @SerializedName("description_en") var descriptionEn: String? = null,
    @SerializedName("description_sv") var descriptionSv: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("path") var path: String? = null,
    @SerializedName("platform_order") var platformOrder: Int? = null,
    @SerializedName("direct") var direct: Int? = null,
    @SerializedName("label") var label: String? = null
)