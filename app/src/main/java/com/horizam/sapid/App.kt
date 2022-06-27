package com.horizam.sapid

import android.app.Application
import android.content.Context


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        context = applicationContext
    }

    companion object {
        private var context: Context? = null

        fun getAppContext(): Context? {


            return context
        }

    }
}