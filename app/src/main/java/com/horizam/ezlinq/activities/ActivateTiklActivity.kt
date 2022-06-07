package com.horizam.ezlinq.activities

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.horizam.ezlinq.Constants.Companion.BASE_URL
import com.horizam.ezlinq.Constants.Companion.USER_NAME
import com.horizam.ezlinq.Constants.Companion.userResponse
import com.horizam.ezlinq.R
import com.horizam.ezlinq.databinding.ActivityActivateProductBinding


import com.horizam.ezlinq.nfc_utils.NFCUtil


class ActivateTiklActivity : AppCompatActivity() {


    private lateinit var prefManager: PrefManager


    private var mNfcAdapter: NfcAdapter? = null
    private var nfcUtil: NFCUtil = NFCUtil()
    private var check: Boolean = false

    private lateinit var binding: ActivityActivateProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()

        binding = ActivityActivateProductBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        setClickListeners()
        setNfcWriter()
        initSharedPreference()
        setData()
    }

    private fun initSharedPreference() {
        prefManager = PrefManager(this)
    }

    private fun setNfcWriter() {
        //nfc process start
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

    }

    private fun setClickListeners() {
        binding.civCloseScreen.setOnClickListener {
            finish()
        }
        binding.btnActivate.setOnClickListener {
            check = true
            Toast.makeText(this, "Tap your LeShare on your phone to activate", Toast.LENGTH_SHORT)
                .show()
            startNfcWrinting()
        }
    }

    override fun onResume() {
        super.onResume()
        startNfcWrinting()

    }

    private fun pauseNfcWrinting() {
        mNfcAdapter?.let {
            nfcUtil.disableNFCInForeground(it, this)
        }
    }

    private fun startNfcWrinting() {
        mNfcAdapter?.let {
            nfcUtil.enableNFCInForeground(it, this, javaClass)
        }
    }

    private fun setData() {

        when (intent.getIntExtra("category", 0)) {
            1 -> {
//                Glide.with(this).load(R.raw.gif_read_activate).into(binding.ivPopl)
                binding.tvPoplTextOne.text =
                    "${getString(R.string.str_tikl_activate_text)} ${prefManager.getUserName()}"
            }
            2 -> {
//                Glide.with(this).load(R.raw.gif_activate_two).into(binding.ivPopl)
                binding.tvPoplTextOne.text =
                    "${getString(R.string.str_tikl_activate_text)} ${prefManager.getUserName()}"
            }
            3 -> {
//                Glide.with(this).load(R.raw.gif_activate_three).into(binding.ivPopl)
                binding.tvPoplTextOne.text =
                    "${getString(R.string.str_tikl_activate_text)} ${prefManager.getUserName()}"
            }
            4 -> {
//                Glide.with(this).load(R.raw.gif_activate_four).into(binding.ivPopl)
                binding.tvPoplTextOne.text =
                    "${getString(R.string.str_tikl_activate_text)} ${prefManager.getUserName()}"
            }
            0 -> {
                binding.tvPoplTextOne.text = ""
            }

        }


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (check) {
            val messageWrittenSuccessfully =
                nfcUtil.createNFCMessage(
                    "${BASE_URL}${userResponse!!.profile!!.username}?source=gotap",
                    intent
                )
            if (messageWrittenSuccessfully) {
                Toast.makeText(
                    this,
                    "$USER_NAME's ${resources.getString(R.string.tikl_activated_successfully)} ",
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.progressBar.visibility = View.GONE
                binding.tvPlaceYourTikl.text =
                    resources.getString(R.string.tikl_activated_successfully)
                finish()

            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.tikl_activation_failed),
                    Toast.LENGTH_SHORT
                ).show()

            }
        } else {
            Toast.makeText(this, "Click on Activate for activating Gotap", Toast.LENGTH_SHORT)
                .show()
        }

    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            nfcUtil.disableNFCInForeground(it, this)
        }
    }
}