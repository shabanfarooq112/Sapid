package com.horizam.ezlinq.activities;

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.horizam.ezlinq.databinding.ActivityActivateBinding

class ActivateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActivateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setLocale()
        binding = ActivityActivateBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.layoutTiklActivate.setOnClickListener {
            val intent = Intent(this, ActivateTiklActivity::class.java)
            intent.putExtra("category", 1)
            startActivity(intent)
        }
        binding.layoutTiklActivateTwo.setOnClickListener {
            val intent = Intent(this, ActivateTiklActivity::class.java)
            intent.putExtra("category", 2)
            startActivity(intent)
        }
        binding.layoutTiklActivateThree.setOnClickListener {
            val intent: Intent = Intent(this, ActivateTiklActivity::class.java)
            intent.putExtra("category", 3)
            startActivity(intent)
        }
        binding.layoutTiklActivateFour.setOnClickListener {
            val intent: Intent = Intent(this, ActivateTiklActivity::class.java)
            intent.putExtra("category", 4)
            startActivity(intent)
        }
        binding.layoutGetAGo.setOnClickListener {
            startActivity(Intent(this, BuyTiklActivity::class.java))
        }
        binding.civCloseScreen.setOnClickListener {
            finish()
        }
    }
}