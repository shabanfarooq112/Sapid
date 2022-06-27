package com.horizam.sapid.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizam.sapid.App
import com.horizam.sapid.Constants
import com.horizam.sapid.R
import com.horizam.sapid.adapters.PlatformsAdapter
import com.horizam.sapid.databinding.ActivityEditProfileBinding
import com.horizam.sapid.networking.ApiListener
import com.horizam.sapid.networking.NetworkingModel
import com.horizam.sapid.networking.request.UpdateProfileRequest
import com.horizam.sapid.networking.response.Categories
import com.horizam.sapid.networking.response.CategoryAndPlatformResponse
import com.horizam.sapid.networking.response.UserResponse
import com.horizam.sapid.utils.ImageFilePath
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance
import kotlin.collections.ArrayList


class EditProfileActivity : AppCompatActivity() {

    private val myCalendar: Calendar = getInstance()
    private lateinit var networkingModel: NetworkingModel
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var categories: MutableList<Categories>
    private lateinit var adapter: PlatformsAdapter
    private lateinit var prefManager: PrefManager
    var makePrivate = 0

    private var imageSelected: Boolean = false
    private var imageUrl: String? = null

    private lateinit var arrayOfGenders: Array<String>
    private var genderTypeDefault: Int = -1
    private var comingFromTakePictureIntentAfterCroping = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()


        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        arrayOfGenders = arrayOf(
            resources.getString(R.string.male),
            resources.getString(R.string.female),
            resources.getString(R.string.non_binary),
            resources.getString(R.string.prefer_not_to_say)
        )


        setRecyclerview()
        setSwitch()
        setClickListeners()
        initSharedPrefference()
        MakeGetCategoriesAndPlatformsApiCall()
    }

    override fun onStart() {
        super.onStart()
        MakeGetCategoriesAndPlatformsApiCall()
    }

    private fun MakeGetCategoriesAndPlatformsApiCall() {
        showLoadingOnButton()
        networkingModel = NetworkingModel()
        networkingModel.exeCategoriesAndPlatformsApi(object :
            ApiListener<CategoryAndPlatformResponse> {
            override fun onSuccess(body: CategoryAndPlatformResponse?) {
                stopLoadingOnButton()
                body.also {
                    if (it!!.status == 200) {
                        adapter.renewlist(body!!.categories)
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

    private fun initSharedPrefference() {
        prefManager = PrefManager(this)
    }

    override fun onResume() {
        super.onResume()
        if (!comingFromTakePictureIntentAfterCroping) {
            exeUserprofileApi()
        }
        comingFromTakePictureIntentAfterCroping = false

    }

    private fun setRecyclerview() {
        categories = ArrayList()
//        binding.recyclerviewPlatforms.setNestedScrollingEnabled(false)
        binding.recyclerviewPlatforms.setHasFixedSize(true)
        binding.recyclerviewPlatforms.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = PlatformsAdapter(categories, this)
        binding.recyclerviewPlatforms.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setClickListeners() {
        binding.etDobEditProfile.setOnClickListener {
            setCalander()
        }
        binding.btnSaveEditProfile.setOnClickListener {
            validationFields()
        }
        binding.ivEditImageEditProfile.setOnClickListener {
            checkPermisionAccess()
        }
        binding.ivRemoveImage.setOnClickListener {
            showAlertDialogForRemoveImageConfirm()
        }

        binding.tvGenderEditProfile.setOnClickListener {
            showDialogToSelectGender()
        }

    }

    private fun showDialogToSelectGender() {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.select_your_gender))
        builder.setSingleChoiceItems(arrayOfGenders, genderTypeDefault) { _, which ->
            binding.tvGenderEditProfile.text = arrayOfGenders[which]
            genderTypeDefault = which
            dialog.dismiss()
        }

        dialog = builder.create()

        dialog.show()

    }

    private fun showAlertDialogForRemoveImageConfirm() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(resources.getString(R.string.do_you_want_to_remove_image))
            .setCancelable(false)
            .setPositiveButton(
                resources.getString(R.string.yes),
                DialogInterface.OnClickListener { dialog, id ->
                    imageSelected = false
                    imageUrl = ""
                    binding.cvProfileEditProfile.setImageResource(0)
                    binding.ivRemoveImage.visibility = View.GONE

                })
            .setNegativeButton(
                resources.getString(R.string.no),
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

        // create dialog box
        val alert = dialogBuilder.create()
        alert.setTitle(resources.getString(R.string.remove_image))
        alert.show()
    }


    private fun checkPermisionAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, Constants.PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*";
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, resources.getString(R.string.select_picture)),
            Constants.IMAGE_PICK_CODE
        );

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(
                        this,
                        resources.getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


    private fun setCalander() {
        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }
        DatePickerDialog(
            this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etDobEditProfile.setText(sdf.format(myCalendar.time))
    }

    private fun setSwitch() {
        binding.switchEditProfile.setOnCheckedChangeListener { buttonView, isChecked ->
            makePrivate = if (isChecked)
                1
            else
                0
        }
    }

    private fun validationFields() {
        val name = binding.tvNameEditProfile.text.toString().trim()
        val username = binding.tvUsernameEditProfile.text.toString().trim()
        val bio = binding.etBioEditProfile.text.toString()
        val gender =
            (arrayOfGenders.indexOf(binding.tvGenderEditProfile.text.toString())).toString().trim()
        val dob: String? = binding.etDobEditProfile.text.toString()

        if (name.isEmpty()) {
            binding.tvNameEditProfile.error =
                resources.getString(R.string.please_enter_your_username)
            return
        }
        if (name.contains(" ")) {
            binding.tvNameEditProfile.error =
                resources.getString(R.string.no_space_allowed_in_username)
            return
        }
        if (name.contains("/")) {
            binding.tvNameEditProfile.error =
                resources.getString(R.string.username_cant_contain_symbol)
            return
        }

        if (name.length > 24 || name.length < 3) {
            binding.tvNameEditProfile.error =
                resources.getString(R.string.name_sould_be_between_5_to_25)
            return

        }

        if (username.isEmpty()) {
            binding.tvUsernameEditProfile.error =
                resources.getString(R.string.please_enter_your_username)
            return
        }
        if (username.contains(" ")) {
            binding.tvUsernameEditProfile.error =
                resources.getString(R.string.no_space_allowed_in_username)
            return
        }
        if (username.contains("/")) {
            binding.tvUsernameEditProfile.error =
                resources.getString(R.string.username_cant_contain_symbol)
            return
        }

        if (username.length > 24 || username.length < 3) {
            binding.tvUsernameEditProfile.error =
                resources.getString(R.string.username_sould_be_between_5_to_25)
            return

        }

        binding.switchEditProfile.setOnCheckedChangeListener { buttonView, isChecked ->
            makePrivate = if (isChecked)
                1
            else
                0
        }


        networkingModel = NetworkingModel()


        showLoadingOnButton()
        if (imageSelected) {

//            val file = File(imageUrl)
            Log.d("size1", (File(imageUrl).length() / 1024).toString().toInt().toString())

            lifecycleScope.launch {
                val file =
                    Compressor.compress(this@EditProfileActivity, File(imageUrl), Dispatchers.Main)



                Log.d("size2", (file.length() / 1024).toString().toInt().toString())

                val aimage: RequestBody = RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    file
                )
                val image: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo", file.name, aimage
                )
                val name: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    name
                )
                val mUsername: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    username
                )
                val mGender: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    gender
                )
                val mDob: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    dob!!
                )
                val mBio: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    bio
                )
                val mMakePublic: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    makePrivate.toString()
                )

                exeUpdateprofileApiWithImage(
                    image,
                    name,
                    mUsername,
                    mGender,
                    mDob,
                    mBio,
                    mMakePublic
                )
            }
        } else {
            var updateProfileRequest: UpdateProfileRequest = UpdateProfileRequest(
                username,
                name,
                null,
                null,
                null,
                null,
                gender,
                null,
                dob,
                bio,
                makePrivate
            )
            exeUpdateprofileApiWithoutImage(updateProfileRequest)

        }


    }

    private fun upDateProfileWithoutImage(updateProfileRequest: UpdateProfileRequest) {
        networkingModel.exeUpdateProfileApi(
            updateProfileRequest,
            object : ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    stopLoadingOnButton()
                    if (body != null) {

                        saveUserName(
                            binding.tvUsernameEditProfile.text.toString().trim()
                        )


                        if (body.status == 200) {
                            startActivity(
                                Intent(
                                    this@EditProfileActivity,
                                    MainActivity::class.java
                                )
                            )
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton()

                    Log.e("error!@", error.message.toString());
                    Toast.makeText(this@EditProfileActivity, error.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })

    }

    private fun exeUpdateprofileApiWithoutImage(updateProfileRequest: UpdateProfileRequest) {
        showLoadingOnButton()

        upDateProfileWithoutImage(updateProfileRequest)

    }

    private fun exeUpdateprofileApiWithImage(
        updateProfileRequest: MultipartBody.Part,
        name: RequestBody,
        mUsername: RequestBody,
        mGender: RequestBody,
        mDob: RequestBody,
        mBio: RequestBody,
        mMakePublic: RequestBody
    ) {
        updateProfileWithImage(
            updateProfileRequest,
            name,
            mUsername,
            mGender,
            mDob,
            mBio,
            mMakePublic
        )
    }


    private fun updateProfileWithImage(
        updateProfileRequest: MultipartBody.Part,
        name: RequestBody,
        mUsername: RequestBody,
        mGender: RequestBody,
        mDob: RequestBody,
        mBio: RequestBody,
        mMakePublic: RequestBody
    ) {
        networkingModel.exeUpdateProfileWithImageApi(
            updateProfileRequest, name, mUsername, mGender, mDob, mBio, mMakePublic,
            object : ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    stopLoadingOnButton()
                    if (body != null) {

                        saveUserName(
                            binding.tvNameEditProfile.text.toString().trim()
                        )

                        val imgFile = File("${body.profile!!.photo}")

                        if (imgFile.exists()) {
                            val myBitmap: Bitmap =
                                BitmapFactory.decodeFile(imgFile.absolutePath)
                            binding.ivEditImageEditProfile.setImageBitmap(myBitmap)
                        }



                        if (body.status == 200) {
                            saveUserName(body.profile!!.username!!)
                            val x = body.profile!!.photo
                            startActivity(
                                Intent(
                                    this@EditProfileActivity,
                                    MainActivity::class.java
                                )
                            )
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton()

                    Toast.makeText(this@EditProfileActivity, error.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun exeUserprofileApi() {
        setProifiledata()
    }

    private fun setProifiledata(
//        name: String?,
//        username: String?,
//        bioString: String?,
//        dob: String?,
//        gender: Int,
//        private: Int,
//        mcategories: List<Categories>,
//        photo: String?,
//        verified: Int
    ) {
        if (!Constants.userResponse!!.profile!!.photo.isNullOrEmpty()) {
            Glide.with(this)
                .load("${Constants.BASE_URL}${Constants.userResponse!!.profile!!.photo}")
                .override(800, 400)
                .placeholder(R.drawable.ic_logo_drawable)
                .into(binding.cvProfileEditProfile)
        }

//        adapter.renewlist(mcategories)
        binding.tvNameEditProfile.setText(Constants.userResponse!!.profile!!.firstName)
        binding.tvUsernameEditProfile.setText(Constants.userResponse!!.profile!!.username)
        binding.etBioEditProfile.setText(Constants.userResponse!!.profile!!.bio)
        binding.etDobEditProfile.setText(Constants.userResponse!!.profile!!.dob)
        if (Constants.userResponse!!.profile!!.private == 1) {
            binding.switchEditProfile.isChecked = true
        }
        if (!Constants.userResponse!!.profile!!.gender!!.isNullOrEmpty()) {
            binding.tvGenderEditProfile.text =
                arrayOfGenders[Integer.parseInt(Constants.userResponse!!.profile!!.gender!!)]
        }
    }

    private fun showLoadingOnButton() {

        binding.btnSaveEditProfile.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.grey)
        binding.btnSaveEditProfile.isClickable = false
        binding.btnSaveEditProfile.text = ""
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoadingOnButton() {

        binding.btnSaveEditProfile.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.app_color)
        binding.btnSaveEditProfile.isClickable = true
        binding.btnSaveEditProfile.text = resources.getString(R.string.str_save_profile)

        binding.progressBar.visibility = View.GONE

    }

    private fun showMainLoader() {
        binding.progressBarMain.visibility = View.VISIBLE
    }

    private fun hideMainLoader() {
        binding.progressBarMain.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.IMAGE_PICK_CODE) {
            var uri = data!!.data
//
//            CropImage.activity(uri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setCropShape(CropImageView.CropShape.OVAL)
//                .setActivityTitle(resources.getString(R.string.crop_image))
//                .setFixAspectRatio(true)
//                .setCropMenuCropButtonTitle(resources.getString(R.string.done))
//                .start(this)
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.img_profile)
                .into(binding.cvProfileEditProfile)

            binding.ivRemoveImage.visibility = View.VISIBLE
            imageSelected = true
            imageUrl = ImageFilePath().getPath(this, uri!!)

            comingFromTakePictureIntentAfterCroping = true

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri = result.uri!!
            }

        }


    }

    private fun saveUserName(username: String) {
        prefManager.setUsername(username)
        Constants.STR_TOKEN
    }
}