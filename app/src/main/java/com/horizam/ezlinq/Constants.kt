package com.horizam.ezlinq

import android.content.Context
import com.horizam.ezlinq.networking.response.UserResponse

class Constants {

    companion object {
        var userResponse: UserResponse? = null
        var STR_TOKEN: String = ""
        const val STR_UNKNOWN_ERROR = "Unknown Error"
        const val BASE_URL = "https://ezlinq.me/"
        const val PERMISSION_CODE = 1001;
        const val IMAGE_PICK_CODE = 1000;
        const val EMAIL = "email"
        const val OTP = "otp"
        const val TITLE = "title"
        const val ICON = "icon"
        const val PATH = "path"
        const val PLATFORM_ID = "platform_id"
        const val LABEL = "label"
        const val HINT = "hint"
        const val EDIT_PROFILE = "edit_profile"
        const val HOME = "home"
        const val OTHER_PROFILE_QR = "other_profile_qr"
        var PLATFORMS_PATH: String? = ""
        var PLATFORMS_TITLE = ""
        var PLATFORMS_ICON = ""
        var PLATFORMS_LABEL: String? = ""
        var PLATFORMS_ID = 0
        var PLATFORMS_HINT: String?=""
        var PLATFORMS_DESCRIPTION: String? = ""
        var BASE_LINK_FOR_PLATFORM_PATH: String? = ""

        var USER_NAME = ""
        var NAME = ""
        var USER_BIO: String? = ""
        var otherUserName = ""

        var NFC_INTENT = "nfc_intent"
        var SEARCH_INTENT = "SEARCH_RESULT"
        var REGEX_FOR_VALID_USERNAME =
            "^(?=[a-zA-Z0-9_._]{3,25}\$)(?!.*[_.]{2})[^_.].*[^_.]\$".toRegex()

        var PLATFORM_TEXT = "Text"
        var PLATFORM_ADDRESS = "Address"
        var PLATFORM_EMAIL = "Email"
        var PLATFORM_SKYPE = "Skype"
        var PLATFORM_LANK = "Lank"
        var PLATFORM_WEBSITE = "Website"
        var PLATFORM_KONTAKT = "Kontakt"
        var PLATFORM_HEMSIDA = "Hemsida"
        var PLATFORM_SPOTIFY = "Spotify"
        var PLATFORM_YOUTUBE = "Youtube"
        var PLATFORM_FACEBOOK = "Facebook"
        var DIRECT_ON = "Direct On"
        var DIRECT_OFF = "Direct Off"

        var ADD_LINKS = "Add Links"

        var currentActivityContext: Context? = null

        const val VCF_DIRECTORY = "/Tikl_VFC"
    }
}