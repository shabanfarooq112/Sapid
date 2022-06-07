package com.horizam.ezlinq.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.Constants.Companion.NFC_INTENT
import com.horizam.ezlinq.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 4000
    private lateinit var binding: ActivitySplashBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSplash()

    }


    private fun setSplash() {
        Handler().postDelayed(Runnable { /* Create an Intent that will start the Menu-Activity. */
            prefManager = PrefManager(this)
            if (prefManager.getAccessToken().equals("")) {
                val mainIntent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                this@SplashActivity.startActivity(mainIntent)
                finish()
            } else {
                Constants.STR_TOKEN = prefManager.getAccessToken().toString()
                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                mainIntent.putExtra(NFC_INTENT, readTikl())
                this@SplashActivity.startActivity(mainIntent)
                finish()
            }
            this@SplashActivity.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    private fun readTikl(): String? {
        val mAction = intent!!.action
        val data: Uri? = intent.data
        if (data != null && ("android.intent.action.VIEW" == mAction || "android.nfc.action.NDEF_DISCOVERED" == mAction)) {
            Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
            return data.toString()
        }
        return null
    }
}