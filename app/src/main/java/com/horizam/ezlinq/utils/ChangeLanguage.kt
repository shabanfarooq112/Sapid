package com.horizam.ezlinq.utils

import com.horizam.ezlinq.App
import java.util.*

class ChangeLanguage {

    companion object {
        public fun setLocale() {
            var a = Locale.getDefault().displayName
            if (a.contains("svenska")||a.contains("русский")) {
//            if (a.contains("svenska")) {

                val myLocale = Locale("ru")
                val res = App.getAppContext()!!.resources
                val dm = res.displayMetrics
                val conf = res.configuration
                conf.locale = myLocale
                res.updateConfiguration(conf, dm)
            }

        }
    }
}

