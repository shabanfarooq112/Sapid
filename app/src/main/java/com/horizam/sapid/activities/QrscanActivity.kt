package com.horizam.sapid.activities;

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.WriterException
import com.horizam.sapid.Constants.Companion.BASE_URL
import com.horizam.sapid.Constants.Companion.OTHER_PROFILE_QR
import com.horizam.sapid.databinding.ActivityQrscanBinding


class QrscanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrscanBinding
    private var bitmap: Bitmap? = null
    private lateinit var inputValue: String
    private lateinit var prefManager: PrefManager
    lateinit var app_url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()

        binding = ActivityQrscanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        clickListeners()
        setQRcode()
    }

    private fun init() {
        val checkScreen = intent.getIntExtra(OTHER_PROFILE_QR, 0)
        prefManager = PrefManager(this)
        val strs = prefManager.getUserTiklData()!!.split("/").toTypedArray()
        val userApikey = strs[strs.size - 1]
        app_url = if (checkScreen == 1) {
            "$BASE_URL${userApikey}"
        } else if(checkScreen==2){
            "$BASE_URL${prefManager.getUserSearchName()}"
        }else {
            "$BASE_URL${prefManager.getUserName()}"
        }

    }

    private fun clickListeners() {
        binding.civCloseScreen.setOnClickListener {
            finish()
        }

        binding.btnShareProfile.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Tikl")

            shareIntent.putExtra(Intent.EXTRA_TEXT, app_url)
            this.startActivity(Intent.createChooser(shareIntent, "Share Tikl"))
        }
    }

    private fun setQRcode() {
        inputValue = app_url
        if (inputValue.length > 0) {
            val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = manager.defaultDisplay
            val point = Point()
            display.getSize(point)
            val width = point.x
            val height = point.y
            var smallerDimension = if (width < height) width else height
            smallerDimension = smallerDimension * 3 / 4
            val qrgEncoder =
                QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension)
            qrgEncoder.colorBlack = Color.BLACK
            qrgEncoder.colorWhite = Color.WHITE
            try {
                bitmap = qrgEncoder.bitmap
                val qrImage = binding.ivQrCode
                qrImage.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                Log.v("@qr112233", e.toString())
            }
        }
    }
}