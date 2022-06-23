package com.horizam.ezlinq.networking.response

import com.google.gson.annotations.SerializedName

data class CategoryAndPlatformResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var categories: ArrayList<Categories>
)

