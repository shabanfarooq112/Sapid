package com.horizam.ezlinq.networking

import com.horizam.ezlinq.networking.request.*
import com.horizam.ezlinq.networking.request.UpdateProfileRequest
import com.horizam.ezlinq.networking.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NetworkingModel : NetworkingHelper() {

    fun exeRegisterUserApi(
        registerUserRequest: RegisterUserRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        registerUser(registerUserRequest, apiListener = apiListener)
    }

    fun exeConnectUserApi(
        id:Int,
        apiListener: ApiListener<GeneralResponseWithStatusAndMessage>
    ){
        connectUser(id,apiListener = apiListener)
    }

    fun exeLoginUserApi(
        loginUserRequest: LoginUserRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        loginUser(loginUserRequest, apiListener = apiListener)
    }

    fun exeUpdateProfileApi(
        updateProfileRequest: UpdateProfileRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        updateUserProfile(updateProfileRequest, apiListener = apiListener)
    }

    fun exeUpdateProfileWithImageApi(
        updateProfileRequest: MultipartBody.Part,
        name: RequestBody,
        mUsername: RequestBody,
        mGender: RequestBody,
        mDob: RequestBody,
        mBio: RequestBody,
        mMakePublic: RequestBody,
        apiListener: ApiListener<UserResponse>
    ) {
        updateUserProfileWithImage(
            updateProfileRequest,
            name,
            mUsername,
            mGender,
            mDob,
            mBio,
            mMakePublic,
            apiListener
        )
    }

    fun exeUserprofileApi(
        string: String,
        apiListener: ApiListener<UserResponse>,
        source: String = "normal"
    ) {
        userProfileInfo(string, apiListener = apiListener, source = source)
    }
    fun exeUserPLatformSwap(
        swapPlatformRequest: SwapPlatformRequest,
        apiListener: ApiListener<GeneralResponseWithStatusAndMessage>

    ) {
        userSwapplatforms(swapPlatformRequest ,apiListener = apiListener)
    }

    fun exeForgetPasswordApi(
        forgetPasswordRequest: ForgetPasswordRequest,
        apiListener: ApiListener<GeneralResponseWithStatusAndMessage>
    ) {
        forgetPassword(forgetPasswordRequest, apiListener = apiListener)
    }

    fun exeRestPasswordApi(
        resetPasswordRequest: ResetPasswordRequest,
        apiListener: ApiListener<GeneralResponseWithStatusAndMessage>
    ) {
        resetPassword(resetPasswordRequest, apiListener = apiListener)
    }

    fun exeSearchUserApi(searchQuery: String, apiListener: ApiListener<SearchUserResponse>) {
        searchUser(searchQuery, apiListener = apiListener)
    }

    fun exeAddPlatFormApi(
        addPlatformRequest: AddPlatformRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        addPlatform(addPlatformRequest, apiListener = apiListener)
    }

    fun exeRemovePlatFormApi(
        platformId: Int,
        apiListener: ApiListener<GeneralResponseWithStatusAndMessage>
    ) {
        removePlatform(platformId, apiListener = apiListener)
    }

//    fun exeAddUserPlatform(
//        addPlatformRequest: AddPlatformRequest,
//        apiListener: ApiListener<AddPlatformResponse>
//    ) {
//        addUserPlatforms(addPlatformRequest, apiListener = apiListener)
//    }

    fun exeCategoriesAndPlatformsApi(
        apiListener: ApiListener<CategoryAndPlatformResponse>
    ) {
        CategoriesAndPlatformsApi(apiListener = apiListener)
    }

    fun exeDirectOnOff(
        addPlatformRequest: DirectOnOffRequest,
        apiListener: ApiListener<UserResponse>
    ) {
        directOnOff(addPlatformRequest, apiListener = apiListener)
    }

    fun exeOtherUserprofileApi(
        string: String,
        apiListener: ApiListener<UserResponse>,
        source: String = "normal"
    ) {
        otherUserprofile(string, apiListener = apiListener, source = source)
    }
}