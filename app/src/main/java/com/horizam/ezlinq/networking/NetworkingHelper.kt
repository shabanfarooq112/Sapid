package com.horizam.ezlinq.networking

import com.horizam.ezlinq.networking.RetrofitClient.getApiEndpointImpl
import com.horizam.ezlinq.networking.request.*
import com.horizam.ezlinq.networking.request.UpdateProfileRequest
import com.horizam.ezlinq.networking.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody


abstract class NetworkingHelper {

    private val apiService = getApiEndpointImpl()

    fun registerUser(
        registerUserRequest: RegisterUserRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        val call = apiService.registerUser(registerUserRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun connectUser(
        id:Int,
    apiListener: ApiListener<GeneralResponseWithStatusAndMessage>
    ){
        val call=apiService.userConnect(id)
        RetrofitClient.executeApi(call,apiListener)
    }

    fun loginUser(loginUserRequest: LoginUserRequest, apiListener: ApiListener<UserResponse>) {
        val call = apiService.loginUser(loginUserRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun updateUserProfile(
        updateProfileRequest: UpdateProfileRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        val call = apiService.updateProfile(updateProfileRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun updateUserProfileWithImage(
        updateProfileRequest: MultipartBody.Part,
        name: RequestBody,
        mUsername: RequestBody,
        mGender: RequestBody,
        mDob: RequestBody,
        mBio: RequestBody,
        mMakePublic: RequestBody,
        apiListener: ApiListener<UserResponse>
    ) {
        val call = apiService.updateProfileWithImage(updateProfileRequest,name,mGender,mDob,mBio,mMakePublic)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun addUserPlatforms(
        addPlatformRequest: AddPlatformRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        val call = apiService.addPlatform(addPlatformRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun userProfileInfo(string: String,apiListener: ApiListener<UserResponse>, source: String) {
        val call = apiService.userProfileinfo()
        RetrofitClient.executeApi(call, apiListener)
    }
    fun userSwapplatforms(
        swapPlatformRequest: SwapPlatformRequest
        ,apiListener: ApiListener<GeneralResponseWithStatusAndMessage>) {
        val call = apiService.swapplatforms(swapPlatformRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun forgetPassword(
        forgetPasswordRequest: ForgetPasswordRequest,
        apiListener: ApiListener<GeneralResponseWithStatusAndMessage>
    ) {
        val call = apiService.forgetPassword(forgetPasswordRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun resetPassword(
        resetPasswordRequest: ResetPasswordRequest,
        apiListener: ApiListener<GeneralResponseWithStatusAndMessage>
    ) {
        val call = apiService.resetPassword(resetPasswordRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun searchUser(searchQuery: String, apiListener: ApiListener<SearchUserResponse>) {
        val call = apiService.searchUser(searchQuery)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun addPlatform(
        addPlatformRequest: AddPlatformRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        val call = apiService.addPlatform(addPlatformRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun removePlatform(
        platformId: Int,
        apiListener: ApiListener<GeneralResponseWithStatusAndMessage>
    ) {
        val call = apiService.removePlatform(platformId)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun CategoriesAndPlatformsApi(apiListener: ApiListener<CategoryAndPlatformResponse>) {
        val call = apiService.CategoriesAndPlatformsApi()
        RetrofitClient.executeApi(call, apiListener)
    }

    fun directOnOff(
        addPlatformRequest: DirectOnOffRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        val call = apiService.directOnOff(addPlatformRequest)
        RetrofitClient.executeApi(call, apiListener)
    }

    fun otherUserprofile(string: String,apiListener: ApiListener<UserResponse>, source: String) {
        val call = apiService.otherUserprofile(string)
        RetrofitClient.executeApi(call, apiListener)
    }
}
