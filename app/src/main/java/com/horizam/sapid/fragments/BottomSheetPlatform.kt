package com.horizam.sapid.fragments;

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.horizam.sapid.App
import com.horizam.sapid.Constants
import com.horizam.sapid.Constants.Companion.BASE_LINK_FOR_PLATFORM_PATH
import com.horizam.sapid.Constants.Companion.PLATFORMS_DESCRIPTION
import com.horizam.sapid.Constants.Companion.PLATFORMS_HINT
import com.horizam.sapid.Constants.Companion.PLATFORMS_ICON
import com.horizam.sapid.Constants.Companion.PLATFORMS_LABEL
import com.horizam.sapid.Constants.Companion.PLATFORMS_PATH
import com.horizam.sapid.Constants.Companion.PLATFORMS_TITLE
import com.horizam.sapid.Constants.Companion.PLATFORM_ADDRESS
import com.horizam.sapid.Constants.Companion.PLATFORM_EMAIL
import com.horizam.sapid.Constants.Companion.PLATFORM_HEMSIDA
import com.horizam.sapid.Constants.Companion.PLATFORM_KONTAKT
import com.horizam.sapid.Constants.Companion.PLATFORM_LANK
import com.horizam.sapid.Constants.Companion.PLATFORM_SPOTIFY
import com.horizam.sapid.Constants.Companion.PLATFORM_TEXT
import com.horizam.sapid.Constants.Companion.PLATFORM_WEBSITE
import com.horizam.sapid.Constants.Companion.PLATFORM_YOUTUBE
import com.horizam.sapid.R
import com.horizam.sapid.activities.PrefManager
import com.horizam.sapid.databinding.FragmentItemPopupBinding

import com.horizam.sapid.event_bus_events.BackFromBottomSheetHome
import com.horizam.sapid.networking.ApiListener
import com.horizam.sapid.networking.NetworkingModel
import com.horizam.sapid.networking.request.AddPlatformRequest
import com.horizam.sapid.networking.response.GeneralResponseWithStatusAndMessage
import com.horizam.sapid.networking.response.UserResponse
import org.greenrobot.eventbus.EventBus
import java.util.*


class BottomSheetPlatform : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentItemPopupBinding
    private lateinit var networkingModel: NetworkingModel
    private lateinit var prefManager: PrefManager

    private var forSavePlatform: Int = 0
    private var forRemovePlatform: Int = 1


    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemPopupBinding.inflate(layoutInflater)
        init()
        clickListeners()
        setData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireView().parent as View).backgroundTintList =
            ColorStateList.valueOf(Color.TRANSPARENT)
    }

    private fun init() {
        prefManager = PrefManager(requireContext())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.platformsPath.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val textView = v as TextView
                if (event.x >= textView.width - textView.compoundPaddingEnd) {
                    binding.platformsPath.setText("")
                    return@OnTouchListener true
                }
            }
            false
        })
        binding.civCloseScreen.setOnClickListener {
            dismiss()
        }
        binding.btnDeleteItem.setOnClickListener {

            exeRemovePlatform(Constants.PLATFORMS_ID)
        }

        binding.btnOpenItem.setOnClickListener {

            when (PLATFORMS_TITLE) {
                PLATFORM_TEXT -> {
                    openMessages()

                }
                PLATFORM_ADDRESS -> {
                    openMap()
                }
                PLATFORM_KONTAKT -> {
                    openContactlist(PLATFORMS_PATH!!)
                }
                PLATFORM_HEMSIDA -> {
                    urlvalidation()
                }
                PLATFORM_YOUTUBE -> {
                    urlvalidation()
                }
                PLATFORM_SPOTIFY -> {
                    urlvalidation()
                }
                PLATFORM_EMAIL -> {
                    composeEmail(PLATFORMS_PATH!!)

                }
                PLATFORM_LANK -> {
                    urlvalidation()

                }
                PLATFORM_WEBSITE -> {

                    urlvalidation()

                }
                Constants.PLATFORM_FACEBOOK -> {
                    val browserIntent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(PLATFORMS_PATH)
                        )
                    startActivity(browserIntent)
                }

                else -> {
                    if (!BASE_LINK_FOR_PLATFORM_PATH.isNullOrEmpty()) {
                        val browserIntent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("$BASE_LINK_FOR_PLATFORM_PATH$PLATFORMS_PATH")
                            )
                        startActivity(browserIntent)
                    }else{
                        var url= PLATFORMS_PATH
                        if (!url!!.startsWith("https://") && !url.startsWith("http://")) {
                            url = "http://$PLATFORMS_PATH"
                        }
                        val openUrlIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(openUrlIntent)
                    }
                }
            }

        }
        binding.btnSave.setOnClickListener {
            if (binding.platformsPath.text.isNotEmpty()) {
                if (PLATFORMS_TITLE == "Youtube" || PLATFORMS_TITLE == "Hamsida" || PLATFORMS_TITLE == "Spotify") {
                    val checkurl: Boolean =
                        Patterns.WEB_URL.matcher(binding.platformsPath.text.toString()).matches()
                    if (!checkurl) {
                        binding.platformsPath.error = "Invalid url"
                        return@setOnClickListener
                    } else {
                        exeAddPlatformApi(
                            binding.platformsPath.text.toString(),
                            Constants.PLATFORMS_ID
                        )
                    }
                }
                exeAddPlatformApi(binding.platformsPath.text.toString(), Constants.PLATFORMS_ID)
            } else {
                binding.platformsPath.error = "Path is Empty"
            }

        }
    }

    private fun urlvalidation() {
        try {
            if (!URLUtil.isValidUrl(binding.platformsPath.text.toString())) {
                binding.platformsPath.setError(
                    "This is not a valid link Link Should be like https://google.com"
                )
            } else {
                val uri = Uri.parse("www.google.com")
                val intent = Intent(Intent.ACTION_VIEW, uri)

                intent.data = Uri.parse(Constants.PLATFORMS_PATH!!.toLowerCase(Locale.ROOT))
                startActivity(intent)
            }
        } catch (e: ActivityNotFoundException) {
            Log.d("wesite@21.1", e.toString())

            Toast.makeText(
                context, resources.getString(R.string.you_dont_have_browser),
                Toast.LENGTH_LONG
            )


        }

//        val checkurl:Boolean= Patterns.WEB_URL.matcher(binding.platformsPath.text.toString()).matches()
//        if (binding.platformsPath.text.equals("")||!checkurl){
//            binding.platformsPath.error="url is empty or invalid"
//        }else {
//            val browserIntent =
//                Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse(binding.platformsPath.text.toString())
//                )
//            startActivity(browserIntent)
//        }
    }


    private fun setData() {
        binding.platformsPath.setText(PLATFORMS_PATH)
        binding.tvPlatformTitle.text = PLATFORMS_TITLE
        binding.platformsPath.hint = "$PLATFORMS_HINT"
        binding.etLabel.setText(PLATFORMS_LABEL)

        if (PLATFORMS_TITLE == "Website") {
            binding.tvGuide.text = requireContext().resources.getString(R.string.Enter_you_website)
        } else {
            binding.tvGuide.text = PLATFORMS_DESCRIPTION
        }
        if (PLATFORMS_TITLE == "Youtube" || PLATFORMS_TITLE == "Hamsida" || PLATFORMS_TITLE == "Spotify" || PLATFORMS_TITLE == "Website") {
            binding.platformsPath.hint = "Enter url here"
        }
        Glide
            .with(this)
            .load("${Constants.BASE_URL}${PLATFORMS_ICON}")
            .into(binding.ivScanTikl)
        Glide
            .with(this)
            .load("${Constants.BASE_URL}${Constants.PLATFORMS_ICON}")
            .into(binding.imgDrawable)
    }

    private fun exeRemovePlatform(platformId: Int) {
        showLoadingOnButton(forRemovePlatform)

        networkingModel = NetworkingModel()
        networkingModel.exeRemovePlatFormApi(platformId,
            object : ApiListener<GeneralResponseWithStatusAndMessage> {
                override fun onSuccess(body: GeneralResponseWithStatusAndMessage?) {
                    stopLoadingOnButton(forRemovePlatform)

                    Toast.makeText(App.getAppContext(), body!!.message, Toast.LENGTH_SHORT).show()

                    EventBus.getDefault().post(BackFromBottomSheetHome())

                    dismiss()
                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton(forRemovePlatform)

                    Toast.makeText(App.getAppContext(), error.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun exeAddPlatformApi(userName: String, platformId: Int) {
        showLoadingOnButton(forSavePlatform)
        networkingModel = NetworkingModel()
        var addPlatformRequest =
            AddPlatformRequest(platformId, userName, binding.etLabel.text.toString())
        networkingModel.exeAddPlatFormApi(addPlatformRequest,
            object : ApiListener<UserResponse> {

                override fun onSuccess(body: UserResponse?) {
                    stopLoadingOnButton(forSavePlatform)
                    Toast.makeText(App.getAppContext(), body!!.message, Toast.LENGTH_SHORT).show()
                    EventBus.getDefault().post(BackFromBottomSheetHome())
                    dismiss()
                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton(forSavePlatform)

                    Toast.makeText(App.getAppContext(), error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {
                    super.onCancel()
                }
            })
    }

    private fun showLoadingOnButton(type: Int) {
        /**0 is for save 1 is for remove**/

        if (type == forSavePlatform) {
            binding.btnSave.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.grey)
            binding.btnSave.isClickable = false
            binding.btnSave.text = ""

            binding.progressSavePlatform.visibility = View.VISIBLE
        } else {
            binding.btnDeleteItem.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.app_color)
            binding.btnDeleteItem.isClickable = false
            binding.btnDeleteItem.text = ""

            binding.progressRemovePlatform.visibility = View.VISIBLE
        }

    }

    private fun stopLoadingOnButton(type: Int) {
        /**0 is for save 1 is for remove**/

        if (type == forSavePlatform) {
            binding.btnSave.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.app_color)
            binding.btnSave.isClickable = true
            binding.btnSave.text = resources.getString(R.string.save)

            binding.progressSavePlatform.visibility = View.GONE
        } else {
            binding.btnDeleteItem.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            binding.btnDeleteItem.isClickable = true
            binding.btnDeleteItem.text = resources.getString(R.string.save)

            binding.progressRemovePlatform.visibility = View.GONE

        }

    }

    private fun openMessages() {
//        val smsIntent = Intent(Intent.ACTION_VIEW)
//        smsIntent.type = "vnd.android-dir/mms-sms"
//        smsIntent.putExtra("address", PLATFORMS_PATH)
//        startActivity(smsIntent)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:" + Uri.encode(PLATFORMS_PATH))
        startActivity(intent)

    }


    fun composeEmail(
        addresses: String
    ) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$addresses")
        }
        startActivity(Intent.createChooser(emailIntent, "sapid"))

//        val intent = Intent(Intent.ACTION_SENDTO)
//        intent.data = Uri.parse("mailto:") // only email apps should handle this
//        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
//        if (intent.resolveActivity(requireContext().packageManager) != null) {
//            startActivity(intent)
//        }
    }

    private fun openContactlist(platformsPath: String) {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE)
        val data: java.util.ArrayList<ContentValues> = java.util.ArrayList()

//Filling data with phone numbers

//Filling data with phone numbers
        val row = ContentValues()
        row.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )
        row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, platformsPath)
        row.put(
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK
        )
        data.add(row)


        intent.putExtra(ContactsContract.Intents.Insert.NAME, "username")
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
        startActivity(intent)


    }

    private fun openMap() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.co.in/maps?q=$PLATFORMS_PATH")
        )
        startActivity(intent)
    }

}