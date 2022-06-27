package com.horizam.sapid.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.horizam.sapid.App
import com.horizam.sapid.Constants.Companion.REGEX_FOR_VALID_USERNAME
import com.horizam.sapid.R
import com.horizam.sapid.databinding.ActivityUserNameBinding
import com.horizam.sapid.networking.ApiListener
import com.horizam.sapid.networking.NetworkingModel
import com.horizam.sapid.networking.request.UpdateProfileRequest
import com.horizam.sapid.networking.response.UserResponse

class UserNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserNameBinding
    private lateinit var networkingModel: NetworkingModel
    private lateinit var prefManager: PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()

        binding = ActivityUserNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setClickListeners()
        initPreference()
    }

    private fun initPreference() {
        prefManager = PrefManager(this)
    }

    private fun setClickListeners() {
        binding.btnContinueUserName.setOnClickListener {
            validateInput()
        }
    }


    private fun validateInput() {

        val userName = binding.etUserName.text.toString()
//        if (!Regex(userName).containsMatchIn(REGEX_FOR_VALID_USERNAME)) {


        if (userName.length > 24 || userName.length < 3) {
            binding.etUserName.error = "User Name should between 3 to 25 Characters"
            return

        }

        if (!REGEX_FOR_VALID_USERNAME.containsMatchIn(userName)) {
            binding.etUserName.error = "Please Enter valid user name"
            return
        } else {
            exeUpdateProfileApi(userName)
        }
    }

    private fun exeUpdateProfileApi(userName: String) {
        showLoadingOnButton()
        networkingModel = NetworkingModel()
        val updateProfileRequest = UpdateProfileRequest(userName)
        networkingModel.exeUpdateProfileApi(updateProfileRequest,
            object : ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    stopLoadingOnButton()
                    body.also {
                        if (it!!.status == 200) {
                            Toast.makeText(
                                App.getAppContext(),
                                "${it!!.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                            saveAccessTokenAndUsername(
                                prefManager.getAccessToken()!!,
                                userName
                            )

                            val intent =
                                Intent(this@UserNameActivity, EditProfileActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(
                                intent
                            )
                            finish()
                        } else {
                            binding.etUserName.error = it.message

                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton()

                    Toast.makeText(App.getAppContext(), "${error}", Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun saveAccessTokenAndUsername(token: String, userName: String) {

        prefManager = PrefManager(this)
        prefManager.setAccessToken(token)
        prefManager.setUsername(userName)


    }

    private fun showLoadingOnButton() {

        binding.btnContinueUserName.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.grey)
        binding.btnContinueUserName.isClickable = false
        binding.btnContinueUserName.text = ""


        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoadingOnButton() {

        binding.btnContinueUserName.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.app_color)
        binding.btnContinueUserName.isClickable = true
        binding.btnContinueUserName.text = resources.getString(R.string.str_continue)

        binding.progressBar.visibility = View.GONE

    }
}