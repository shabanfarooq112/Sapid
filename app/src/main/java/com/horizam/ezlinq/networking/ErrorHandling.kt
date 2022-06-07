package com.horizam.ezlinq.networking

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.networking.response.APIError
import retrofit2.Response

interface BaseErrorHandler {
    val statusCode: Int
    fun handleError(): Throwable
}

data class ErrorResponse(val success : Boolean = true, val message: String)

class ClientSideError(val statusCode: Int, override val message: String) : Throwable(message)
class ServerSideError(val statusCode: Int, override val message: String) : Throwable(message)
class UnknownError(override val message: String) : Throwable(message)

class DefaultErrorHandler<T>(private val response: Response<T>) : BaseErrorHandler {
    override val statusCode: Int
        get() = response.code()

    override fun handleError(): Throwable {
        return when (response.code()) {
            in 400..499 -> {
                val gson = Gson()
                val type = object : TypeToken<APIError>() {}.type
                val apiError: APIError? = gson.fromJson(response.errorBody()!!.charStream(), type)
                ClientSideError(statusCode, apiError!!.message)
            }

            in 500..599 -> {
                val gson = Gson()
                val type = object : TypeToken<APIError>() {}.type
                val apiError: APIError? = gson.fromJson(response.errorBody()!!.charStream(), type)
                ServerSideError(statusCode, apiError!!.message)
            }
            else -> {
                UnknownError(Constants.STR_UNKNOWN_ERROR)
            }
        }
    }

}