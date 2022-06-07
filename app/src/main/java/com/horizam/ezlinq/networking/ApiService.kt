package com.horizam.ezlinq.networking

import com.horizam.ezlinq.networking.request.*
import com.horizam.ezlinq.networking.request.UpdateProfileRequest
import com.horizam.ezlinq.networking.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    companion object {
        const val REGISTER_USER = "register"
        const val LOGIN_USER = "login"
        const val UPDATE_PROFILE = "updateProfile"
        const val PROFILE_USER = "profile"
        const val FORGET_PASSWORD = "forgetPassword"
        const val RESET_PASSWORD = "resetPassword"
        const val SEARCH = "search"
        const val ADD_PLATFORM = "addPlatform"
        const val SEARCH_QUERY = "q"
        const val REMOVE_PLATFORM = "removePlatform/{id}"
        const val REMOVE_QUERY = "platform_id"
        const val CONNECT_USER = "connect"
        const val SWAP_ODER = "swapOrder"
        const val CATEGORIES_PLATFORM = "platforms"
        const val OTHER_PROFILE_USER = "profile"

    }

    @POST(SWAP_ODER)
    fun swapplatforms(@Body swapPlatformRequest: SwapPlatformRequest): Call<GeneralResponseWithStatusAndMessage>

    @GET(PROFILE_USER)
    fun userProfileinfo(): Call<UserResponse>

    @GET(CONNECT_USER)
    fun userConnect(@Query("id") id: Int): Call<GeneralResponseWithStatusAndMessage>

    @GET(SEARCH)
    fun searchUser(@Query(SEARCH_QUERY) query: String): Call<SearchUserResponse>

    @POST(REGISTER_USER)
    fun registerUser(@Body registerUserRequest: RegisterUserRequest): Call<UserResponse>

    @POST(LOGIN_USER)
    fun loginUser(@Body loginUserRequest: LoginUserRequest): Call<UserResponse>

    @POST(UPDATE_PROFILE)
    fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest): Call<UserResponse>

    @Multipart
    @POST(UPDATE_PROFILE)
    fun updateProfileWithImage(
        @Part updateProfileRequest: MultipartBody.Part,
        @Part("first_name") name: RequestBody,
//        @Part("username") mUsername: RequestBody,
        @Part("gender") mGender: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("bio") mBio: RequestBody,
        @Part("private") mMakePublic: RequestBody
    ): Call<UserResponse>

    @POST(FORGET_PASSWORD)
    fun forgetPassword(@Body forgetPasswordRequest: ForgetPasswordRequest): Call<GeneralResponseWithStatusAndMessage>

    @POST(RESET_PASSWORD)
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Call<GeneralResponseWithStatusAndMessage>

    @POST(ADD_PLATFORM)
    fun addPlatform(@Body addPlatformRequest: AddPlatformRequest): Call<UserResponse>

    @DELETE(REMOVE_PLATFORM)
    fun removePlatform(@Path("id") platformId: Int): Call<GeneralResponseWithStatusAndMessage>

    @GET(CATEGORIES_PLATFORM)
    fun CategoriesAndPlatformsApi(): Call<CategoryAndPlatformResponse>

    @POST(ADD_PLATFORM)
    fun directOnOff(@Body addPlatformRequest: DirectOnOffRequest): Call<UserResponse>

    @GET(OTHER_PROFILE_USER)
    fun otherUserprofile(@Query("username") searchWord: String): Call<UserResponse>
}