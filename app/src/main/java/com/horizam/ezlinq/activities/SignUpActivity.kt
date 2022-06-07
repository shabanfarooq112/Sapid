package com.horizam.ezlinq.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.horizam.ezlinq.App
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.R
import com.horizam.ezlinq.databinding.ActivitySignUpBinding


import com.horizam.ezlinq.networking.ApiListener
import com.horizam.ezlinq.networking.NetworkingModel
import com.horizam.ezlinq.networking.request.RegisterUserRequest
import com.horizam.ezlinq.networking.response.UserResponse


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var networkingModel: NetworkingModel
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()


        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
//        binding.ccp.setDefaultCountryUsingNameCode("+213")
        setContentView(view)

        binding.ccp.setCountryForPhoneCode(213)
        setClickListeners()
    }
    private fun setClickListeners() {
        binding.btnContinueSignUp.setOnClickListener {
            validateInput()
        }

        binding.tvTermConditions.setOnClickListener {
            val url = "http://www.google.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
    private fun validateInput() {
        val name : String = binding.etNameSignUp.text.toString()
        val email: String = binding.etEmailSignUp.text.toString()
        val phone:String= binding.ccp.selectedCountryCodeWithPlus+binding.etCarrierNumber.text.toString().trim()
        val password: String = binding.etPasswordSignUp.text.toString()
        val confirmPassword:String=binding.etConfimRpasswordSignUp.text.toString()
        val emailCheck: Boolean = isValidEmail(email)
        val passwordCheck: Boolean = isPasswordValid(password)
        if (email.isEmpty()) {
            binding.etEmailSignUp.error = "Email is Empty"
        } else if (!emailCheck) {
            binding.etEmailSignUp.error = "Email is Invalid"
        } else if (password.isEmpty()) {
            binding.etPasswordSignUp.error = "Password is Empty"
        } else if (!passwordCheck) {
            binding.etPasswordSignUp.error = "Password is invalid"
        }
        else if(phone.length< 10 || phone.length >20 || phone.matches(Patterns.PHONE.toRegex())==false) {
           binding.etCarrierNumber.error="Invalid phone number"
        }
        else if(!password.equals(confirmPassword)) {
            binding.etConfimRpasswordSignUp.error="Password is not match"

        }else if(name.length > 24 || name.length < 3){
            binding.etNameSignUp.error = "Name should between 3 to 25 Characters"
        }else if(!Constants.REGEX_FOR_VALID_USERNAME.containsMatchIn(name)){
            binding.etNameSignUp.error = "Please Enter valid name"
        }
        else {
            exeRegisterUserApi(name,email,phone,password,confirmPassword)
        }
    }

    private fun exeRegisterUserApi(name:String,email: String,phone:String, password: String,confrm_pass:String) {
        showLoadingOnButton()
        networkingModel = NetworkingModel()
        val registerUserRequest: RegisterUserRequest =
            RegisterUserRequest(name,email,phone, password, confrm_pass)
        networkingModel.exeRegisterUserApi(registerUserRequest,
            object : ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {

                    stopLoadingOnButton()
                    if (body!!.status == 200) {
                        loginUser(body!!.status!!, body!!.message!!, body!!.token!!,body.profile!!.id!!)
                    }else {
                        Toast.makeText(
                            App.getAppContext(),
                            body.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton()

                    Toast.makeText(App.getAppContext(), error.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("@1122@1", error.toString())
                }
            })
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

    private fun saveAccessTokenAndUserName(token: String, id:Int) {
        prefManager = PrefManager(this)
        prefManager.setAccessToken(token)
        prefManager.setUserId(id)
    }


    fun isValidEmail(email: String?): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        if (password.isNotEmpty()) {
            if (password.length < 6 || password.length < 200) {
                return true
            }
        }
        return false
    }

    private fun clearFieldsAndIntentToAddUserName() {
        binding.etEmailSignUp.text.clear()
        binding.etPasswordSignUp.text.clear()
        startActivity(Intent(App.getAppContext(), UserNameActivity::class.java))
    }

    private fun showLoadingOnButton() {

        binding.btnContinueSignUp.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.grey)
        binding.btnContinueSignUp.isClickable = false
        binding.btnContinueSignUp.text = ""


        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoadingOnButton() {

        binding.btnContinueSignUp.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.app_color)
        binding.btnContinueSignUp.isClickable = true
        binding.btnContinueSignUp.text = resources.getString(R.string.str_continue)

        binding.progressBar.visibility = View.GONE

    }
}