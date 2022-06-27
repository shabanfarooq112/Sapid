package com.horizam.sapid.activities

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.horizam.sapid.App
import com.horizam.sapid.Constants
import com.horizam.sapid.databinding.ActivityForgetPasswordOtpBinding


class ForgetPasswordOtpActivity : AppCompatActivity() {
    private var email: String? = null
    private var otp: String? = null
    private lateinit var binding: ActivityForgetPasswordOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()

        binding = ActivityForgetPasswordOtpBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setOtp()
        getIntentData()
        setClickListeners()
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.EMAIL)) {
            email = intent.getStringExtra(Constants.EMAIL)
        }
    }

    private fun setClickListeners() {
        binding.btnSendCodeForgetPass.setOnClickListener {
            checkValidation()
        }
        binding.tvBackToLogin.setOnClickListener {
            startActivity(Intent(App.getAppContext(), SignInActivity::class.java))
            finish()
        }
    }

    private fun checkValidation() {

        otp = binding.otpForgetPass.otp
        if (otp!!.length<6) {
            binding.otpForgetPass.showError()
            return
        }
        val intent = Intent(App.getAppContext(), ResetPasswordActivity::class.java)
        intent.putExtra(Constants.EMAIL, email)
        intent.putExtra(Constants.OTP, otp)
        startActivity(intent)

    }

    private fun setOtp() {
        val otpTextView = binding.otpForgetPass
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                checkValidation()
            }
        }
    }
}