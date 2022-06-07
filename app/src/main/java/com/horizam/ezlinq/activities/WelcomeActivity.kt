package com.horizam.ezlinq.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.horizam.ezlinq.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()


        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity,
                SignInActivity::class.java))
        }
        binding.btnSetTikl.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity,
                    SignUpActivity::class.java))
        }
        binding.btnBuyTikl.setOnClickListener {
            val url = "https://www.gotaps.me"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
            //throw RuntimeException("Test Crash"); // Force a crash
        }
    }
}