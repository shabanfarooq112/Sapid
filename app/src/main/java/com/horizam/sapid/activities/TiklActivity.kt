package com.horizam.sapid.activities;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.horizam.sapid.databinding.ActivityTiklBinding


class TiklActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTiklBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()


        binding = ActivityTiklBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        clickListeners()
    }

    private fun clickListeners() {
        binding.civCloseScreen.setOnClickListener { finish() }
    }
}