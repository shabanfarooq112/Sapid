package com.horizam.ezlinq.activities

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Lincoln on 05/05/16.
 */
class PrefManager(var _context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    // shared pref mode
    var PRIVATE_MODE = 0

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "tiklPrefs"
        private const val ACCESS_TOKEN = "accessToken"
        private const val USERID_THIS = "userid"
        private const val USERID_ID = "userintegerid"
        private const val USER_NFC_ID = "usernfcinfo"
        private const val USER_SEARCH_ID = "usersearchinfo"
        private const val DIRECT_STATE = "directState"

    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun setAccessToken(token: String) {
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
        editor.commit()
    }

    fun setUsername(userName: String) {
        editor.putString(USERID_THIS, userName)
        editor.apply()
        editor.commit()
    }

    fun setUserId(id: Int) {
        editor.putInt(USERID_ID, id)
        editor.apply()
        editor.commit()
    }
    fun getUserId(): Int? {

        var userID = pref.getInt(USERID_ID, 0)
        return userID
    }

    fun setDirectState(onOff: Int) {
        editor.putInt(DIRECT_STATE, onOff)
        editor.apply()
        editor.commit()
    }

    fun getDirectState(): Int? {

        return pref.getInt(DIRECT_STATE, 0)
    }

    fun getAccessToken(): String? {

        var accessToken = pref.getString(ACCESS_TOKEN, "")
        return accessToken
    }

    fun getUserName(): String? {

        var userName = pref.getString(USERID_THIS, "")
        return userName
    }

    fun setUserTiklData(userID: String) {
        editor.putString(USER_NFC_ID, userID)
        editor.apply()
        editor.commit()
    }

    fun setUserSearchName(userID: String) {
        editor.putString(USER_SEARCH_ID, userID)
        editor.apply()
        editor.commit()
    }

    fun getUserTiklData(): String? {
        var userData = pref.getString(USER_NFC_ID, "")
        return userData
    }

    fun getUserSearchName(): String? {
        var userData = pref.getString(USER_SEARCH_ID, "")
        return userData
    }
    fun clearAll(){
        editor.clear()
        editor.commit()
    }


}