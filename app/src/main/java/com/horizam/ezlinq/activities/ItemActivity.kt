package com.horizam.ezlinq.activities;

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.horizam.ezlinq.App
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.Constants.Companion.HINT
import com.horizam.ezlinq.Constants.Companion.ICON
import com.horizam.ezlinq.Constants.Companion.LABEL
import com.horizam.ezlinq.Constants.Companion.PATH
import com.horizam.ezlinq.Constants.Companion.PLATFORM_ID
import com.horizam.ezlinq.Constants.Companion.TITLE
import com.horizam.ezlinq.R
import com.horizam.ezlinq.databinding.ActivityItemBinding
import com.horizam.ezlinq.networking.ApiListener
import com.horizam.ezlinq.networking.NetworkingModel
import com.horizam.ezlinq.networking.request.AddPlatformRequest
import com.horizam.ezlinq.networking.response.GeneralResponseWithStatusAndMessage
import com.horizam.ezlinq.networking.response.UserResponse


class ItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemBinding
    private lateinit var networkingModel: NetworkingModel

    private var path: String? = "";
    private var platformId: String = "";
    private var titlecheck = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()
        binding = ActivityItemBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        setClickListener()
        setData()
    }

    private fun exeAddPlatformApi(
        userName: String,
        platformId: String,
        label: String
    ) {
        showLoadingOnButton()
        networkingModel = NetworkingModel()
        var addPlatformRequest = AddPlatformRequest(platformId.toInt(), userName,label)
        networkingModel.exeAddPlatFormApi(addPlatformRequest,
            object : ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    stopLoadingOnButton()
                    Toast.makeText(App.getAppContext(), body!!.message, Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton()
                    Toast.makeText(App.getAppContext(), error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {
                    stopLoadingOnButton()
                    super.onCancel()
                }
            })


    }


    private fun setClickListener() {
        binding.civCloseScreen.setOnClickListener {
            finish()
        }
        binding.tvSavePlatform.setOnClickListener {
            checkValidation()
        }
        binding.ivRemovePlatform.setOnClickListener {
            exeRemovePlatform(platformId)
        }
        binding.btnAddAnotherItem.setOnClickListener {
            startActivity(Intent(App.getAppContext(), EditProfileActivity::class.java))
        }


    }

    private fun exeRemovePlatform(platformId: String) {
        networkingModel = NetworkingModel()
        networkingModel.exeRemovePlatFormApi(platformId.toInt(),
            object : ApiListener<GeneralResponseWithStatusAndMessage> {
                override fun onSuccess(body: GeneralResponseWithStatusAndMessage?) {

                    Toast.makeText(App.getAppContext(), body!!.message, Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onFailure(error: Throwable) {
                    Toast.makeText(App.getAppContext(), error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {


                    super.onCancel()
                }
            })

    }

    private fun checkValidation() {
        val checkurl: Boolean = Patterns.WEB_URL.matcher(binding.etItem.text.toString()).matches();
        val userName: String = binding.etItem.text.toString()
        val label: String = binding.etTitleEdit.text.toString()
        if (userName.isEmpty() || platformId.isEmpty()) {
            binding.etItem.error = "Please Enter Username"
            return
        } else if (titlecheck == "Youtube") {
            if (!checkurl) {
                binding.etItem.error = "Invalid url"
                return
            } else {
                exeAddPlatformApi(userName, platformId,label)
            }
        } else {
            exeAddPlatformApi(userName, platformId,label)
        }


    }

    private fun setData() {
        val title = intent.getStringExtra(TITLE)
        val label = intent.getStringExtra(LABEL)
        val icon = intent.getStringExtra(ICON)
        if (title != null) {
            titlecheck = title
        }
        if (intent.getStringExtra(HINT) != null) {
            binding.etItem.hint = intent.getStringExtra(HINT)
        }
        path = intent.getStringExtra(PATH)
        platformId = intent.getIntExtra(PLATFORM_ID, -1).toString()
        if (label != null) {
            binding.etTitleEdit.setText(label.trim())
        } else {
            binding.etTitleEdit.setText(title!!.trim())
        }
        Glide
            .with(this)
            .load("${Constants.BASE_URL}${icon}")
            .into(binding.ivItemPic)

        binding.etTitleEdit.hint = "${title!!.trim()} username"
        if (title.trim() == "Youtube") {
            binding.etItem.hint = "Enter Url here"
        }
        if (path != null) {
            binding.etItem.setText(path!!.trim())
        }
    }

    private fun showLoadingOnButton() {

        binding.tvSavePlatform.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.grey)
        binding.tvSavePlatform.isClickable = false
        binding.tvSavePlatform.text = ""


        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoadingOnButton() {

        binding.tvSavePlatform.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.app_color)
        binding.tvSavePlatform.isClickable = true
        binding.tvSavePlatform.text = resources.getString(R.string.save)

        binding.progressBar.visibility = View.GONE

    }
}