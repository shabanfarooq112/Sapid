package com.horizam.ezlinq.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.horizam.ezlinq.databinding.ActivityBuyTiklBinding

class BuyTiklActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyTiklBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()

        binding = ActivityBuyTiklBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loadWebView("https://www.gotaps.me")
        setClickListeners()
    }

    private fun loadWebView(url: String) {
        binding.webview.loadUrl(url)
    }

    private fun setClickListeners() {

    }
}