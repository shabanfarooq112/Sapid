package com.horizam.sapid.networking.request

import com.horizam.sapid.utils.SwapPlatform

data class SwapPlatformRequest(
    val orderList: List<SwapPlatform>
)