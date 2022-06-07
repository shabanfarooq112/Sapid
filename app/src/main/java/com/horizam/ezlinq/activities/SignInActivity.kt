package com.horizam.ezlinq.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.horizam.ezlinq.App
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.R
import com.horizam.ezlinq.databinding.ActivitySignInBinding
import com.horizam.ezlinq.networking.ApiListener
import com.horizam.ezlinq.networking.NetworkingModel
import com.horizam.ezlinq.networking.request.LoginUserRequest
import com.horizam.ezlinq.networking.response.UserResponse

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var networkingModel: NetworkingModel
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()


        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setClickListeners()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setClickListeners() {
        binding.btnLogIn.setOnClickListener {
            validateInput()
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordEmailActivity::class.java))
        }
    }

    private fun validateInput() {
        val email: String = binding.etEmailSignIn.text.toString()
        val password: String = binding.etPasswordSignIn.text.toString()
        val emailCheck: Boolean = isValidEmail(email)
        val passwordCheck: Boolean = isPasswordValid(password)
        if (email.isEmpty()) {
            binding.etEmailSignIn.error = "Email is Empty"
        } else if (!emailCheck) {
            binding.etEmailSignIn.error = "Email is Invalid"
        } else if (password.isEmpty()) {
            binding.etPasswordSignIn.error = "Password is Empty"
        } else if (!passwordCheck) {
            binding.etPasswordSignIn.error = "Password is invalid"
        } else {
            exeLoginUserApi(email, password)
        }
    }

    private fun exeLoginUserApi(email: String, password: String) {

        showLoadingOnButton()

        networkingModel = NetworkingModel()
        val loginUserRequest = LoginUserRequest(email, password)
        networkingModel.exeLoginUserApi(loginUserRequest, object : ApiListener<UserResponse> {
            override fun onSuccess(body: UserResponse?) {
                stopLoadingOnButton()
                body.also {
                    if (it!!.status == 200) {
                        loginUser(it!!.status!!, it!!.message!!, it.token!!,it.profile!!.id!!)
                    } else {
                        Toast.makeText(App.getAppContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(error: Throwable) {
                stopLoadingOnButton()

                Toast.makeText(App.getAppContext(), "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun isValidEmail(emailone: String?): Boolean {
        return !TextUtils.isEmpty(emailone) && Patterns.EMAIL_ADDRESS.matcher(emailone).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        if (password.isNotEmpty()) {
            if (password.length < 6 || password.length < 200) {
                return true
            }
        }
        return false
    }

    private fun saveAccessTokenAndUserName(token: String,id: Int) {
        prefManager = PrefManager(this)
        prefManager.setAccessToken(token)
        prefManager.setUserId(id)
        Constants.STR_TOKEN = token
    }
    private fun loginUser(status: Int, message: String, token: String,id:Int) {
        if (status == 200) {
            Toast.makeText(App.getAppContext(), message, Toast.LENGTH_SHORT).show()
            saveAccessTokenAndUserName(token,id)
            val intent = Intent(App.getAppContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    private fun showLoadingOnButton() {

        binding.btnLogIn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
        binding.btnLogIn.isClickable = false
        binding.btnLogIn.text = ""


        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoadingOnButton() {

        binding.btnLogIn.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.app_color)
        binding.btnLogIn.isClickable = true
        binding.btnLogIn.text = resources.getString(R.string.str_log_in)

        binding.progressBar.visibility = View.GONE

    }
}