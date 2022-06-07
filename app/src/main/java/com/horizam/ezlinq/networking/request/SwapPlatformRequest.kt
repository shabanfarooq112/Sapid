package com.horizam.ezlinq.networking.request

import com.horizam.ezlinq.utils.SwapPlatform

data class SwapPlatformRequest(
    val orderList: List<SwapPlatform>
)