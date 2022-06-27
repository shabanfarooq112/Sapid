package com.horizam.sapid.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.horizam.sapid.R
import com.horizam.sapid.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()
        prefManager = PrefManager(this)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.tvSignIn.setOnClickListener {
            startActivity(
                Intent(
                    this@WelcomeActivity,
                    SignInActivity::class.java
                )
            )
        }
        binding.btnSetTikl.setOnClickListener {
            startActivity(
                Intent(
                    this@WelcomeActivity,
                    SignUpActivity::class.java
                )
            )
        }
        binding.btnBuyTikl.setOnClickListener {
            val url = "https://www.gotaps.me"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
            //throw RuntimeException("Test Crash"); // Force a crash
        }


        var cvEnglish: CardView = findViewById(R.id.cv_english)
        var cvSpanish: CardView = findViewById(R.id.cv_spanish)
        var rlLang: RelativeLayout = findViewById(R.id.rl_lang)

        if (prefManager.getLang() == "en") {
            cvEnglish.visibility = View.VISIBLE
            cvSpanish.visibility = View.INVISIBLE
        } else {
            cvEnglish.visibility = View.INVISIBLE
            cvSpanish.visibility = View.VISIBLE
        }

        cvEnglish.setOnClickListener {
            cvEnglish.visibility = View.VISIBLE
            cvSpanish.visibility = View.INVISIBLE
            prefManager.setLang("en")
            var intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        rlLang.setOnClickListener {
            if (cvSpanish.visibility == View.VISIBLE) {
                cvEnglish.visibility = View.VISIBLE
                cvSpanish.visibility = View.INVISIBLE
                prefManager.setLang("en")
                var intent = Intent(this, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                cvEnglish.visibility = View.INVISIBLE
                cvSpanish.visibility = View.VISIBLE
                prefManager.setLang("es")
                var intent = Intent(this, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        cvSpanish.setOnClickListener {
            cvEnglish.visibility = View.INVISIBLE
            cvSpanish.visibility = View.VISIBLE
            prefManager.setLang("es")
            var intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}