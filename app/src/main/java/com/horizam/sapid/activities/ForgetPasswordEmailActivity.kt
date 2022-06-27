package com.horizam.sapid.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.horizam.sapid.App
import com.horizam.sapid.R
import com.horizam.sapid.databinding.ActivityForgetPasswordEmailBinding
import com.horizam.sapid.networking.ApiListener
import com.horizam.sapid.networking.NetworkingModel
import com.horizam.sapid.networking.request.ForgetPasswordRequest
import com.horizam.sapid.networking.response.GeneralResponseWithStatusAndMessage
import kotlinx.android.synthetic.main.activity_forget_password_email.*


class ForgetPasswordEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordEmailBinding
    private lateinit var networkingModel: NetworkingModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()


        binding = ActivityForgetPasswordEmailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnSendCodeForgetPass.setOnClickListener {

            val email = et_email_forget_pass.text.toString()

            if (email == "") {
                binding.etEmailForgetPass.error = resources.getString(R.string.please_enter_your_email)
                return@setOnClickListener
            }
            exeForgetPasswordApi(email)

        }
        binding.tvBackToLogin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun exeForgetPasswordApi(email: String) {

        showLoadingOnButton()
        networkingModel = NetworkingModel()

        var forgetPasswordRequest: ForgetPasswordRequest = ForgetPasswordRequest(email)
        networkingModel.exeForgetPasswordApi(forgetPasswordRequest, object :
            ApiListener<GeneralResponseWithStatusAndMessage> {
            override fun onSuccess(body: GeneralResponseWithStatusAndMessage?) {
                stopLoadingOnButton()
                Toast.makeText(App.getAppContext(), body!!.message, Toast.LENGTH_SHORT).show()
                val intent=Intent(this@ForgetPasswordEmailActivity, SignInActivity::class.java)
                startActivity(intent)
            }
            override fun onFailure(error: Throwable) {
                stopLoadingOnButton()
                Toast.makeText(App.getAppContext(), error.message, Toast.LENGTH_SHORT).show()
            }

        })


    }
    private fun showLoadingOnButton() {

        binding.btnSendCodeForgetPass.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
        binding.btnSendCodeForgetPass.isClickable = false
        binding.btnSendCodeForgetPass.text = ""


        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoadingOnButton() {

        binding.btnSendCodeForgetPass.backgroundTintList = ContextCompat.getColorStateList(this, R.color.app_color)
        binding.btnSendCodeForgetPass.isClickable = true
        binding.btnSendCodeForgetPass.text = resources.getString(R.string.str_send_code)

        binding.progressBar.visibility = View.GONE

    }
}