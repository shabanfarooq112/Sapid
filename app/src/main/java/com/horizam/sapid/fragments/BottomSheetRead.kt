package com.horizam.sapid.fragments;

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.horizam.sapid.R
import com.horizam.sapid.databinding.FragmentReadTiklBinding


class BottomSheetRead : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentReadTiklBinding


    var intentFiltersArray: Array<IntentFilter>? = null
    val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))

    var iswrite = "0"
    var userId = "";

    val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(context)
    }
    var pendingIntent: PendingIntent? = null

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadTiklBinding.inflate(layoutInflater)
        clickListeners()
        setNfcWriter()
        Glide.with(requireActivity())
            .load(R.raw.gif_read_activate)
            .into(binding.ivScanTikl)

        return binding.root

    }

    private fun setNfcWriter() {
        pendingIntent = PendingIntent.getActivity(
            context, 0, Intent(context, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
        intentFiltersArray = arrayOf(ndef)
    }

    private fun clickListeners() {
        binding.tvCancel.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            intentFiltersArray,
            techListsArray
        )
    }


    override fun onPause() {
        nfcAdapter?.disableForegroundDispatch(activity)

        super.onPause()
    }
}