package com.horizam.ezlinq.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.horizam.ezlinq.App
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.R
import com.horizam.ezlinq.databinding.ActivityResetPasswordBinding

import com.horizam.ezlinq.networking.ApiListener
import com.horizam.ezlinq.networking.NetworkingModel
import com.horizam.ezlinq.networking.request.ResetPasswordRequest
import com.horizam.ezlinq.networking.response.GeneralResponseWithStatusAndMessage

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var email: String
    private lateinit var otp: String
    private lateinit var networkingModel: NetworkingModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()


        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setClickListeners()
        getIntentData()
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.EMAIL) && intent.hasExtra(
                Constants.OTP)) {
            email = intent.getStringExtra(Constants.EMAIL)!!
            otp = intent.getStringExtra(Constants.OTP)!!
        }
    }

    private fun setClickListeners() {
        binding.btnResetPass.setOnClickListener {
            validationCheck()
        }
    }

    private fun validationCheck() {
        val password = binding.etEnterPass.text.toString()
        val confirmPassword = binding.etReEnterPass.text.toString()

        if (passwordValication(password, confirmPassword)) {
            exeResetPassword(password)

        } else {
            Toast.makeText(App.getAppContext(), resources.getString(R.string.password_dosent_match), Toast.LENGTH_SHORT)
                .show()

        }

    }

    private fun exeResetPassword(password: String) {
        showLoadingOnButton()
        networkingModel = NetworkingModel()
        val resetPasswordRequest: ResetPasswordRequest = ResetPasswordRequest(email, otp, password)

        networkingModel.exeRestPasswordApi(resetPasswordRequest,
            object : ApiListener<GeneralResponseWithStatusAndMessage> {
                override fun onSuccess(body: GeneralResponseWithStatusAndMessage?) {
                    stopLoadingOnButton()
                    body.also {
                        if (it!!.status == 200) {

                            Toast.makeText(
                                App.getAppContext(),
                                "${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            startActivity(Intent(App.getAppContext(), SignInActivity::class.java))

                        } else {
                            Toast.makeText(App.getAppContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton()
                    Toast.makeText(App.getAppContext(), error.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onCancel() {
                    super.onCancel()
                }
            })
    }

    private fun passwordValication(password: String, confirmPassword: String): Boolean {

        if (password == confirmPassword) {
            return true
        }
        return false
    }


    private fun showLoadingOnButton() {

        binding.btnResetPass.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.grey)
        binding.btnResetPass.isClickable = false
        binding.btnResetPass.text = ""


        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoadingOnButton() {

        binding.btnResetPass.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.app_color)
        binding.btnResetPass.isClickable = true
        binding.btnResetPass.text = resources.getString(R.string.str_reset_password)

        binding.progressBar.visibility = View.GONE

    }
}