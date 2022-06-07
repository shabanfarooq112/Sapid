package com.horizam.ezlinq.nfc_utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import java.io.IOException

open class NFCUtil() {
    fun createNFCMessage(payload: String, intent: Intent?): Boolean {


        val nfcRecord = NdefRecord.createUri(payload)

        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent?.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return writeMessageToTag(nfcMessage, tag)
        }
        return false
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()
                if (it.maxSize < nfcMessage.toByteArray().size) {
                    //Message to large to write to NFC tag
                    return false
                }
                return if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    //Message is written to tag
                    true
                } else {
                    //NFC tag is read-only
                    false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                return try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    //The data is written to the tag
                    true
                } catch (e: IOException) {
                    //Failed to format tag
                    false
                }
            }
            //NDEF is not supported
            return false

        } catch (e: Exception) {
            //Write operation has failed
        }
        return false
    }

    public fun <T> enableNFCInForeground(nfcAdapter: NfcAdapter, activity: Activity, classType: Class<T>) {
        val pendingIntent = PendingIntent.getActivity(
            activity, 0,
            Intent(activity, classType).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val nfcIntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val filters = arrayOf(nfcIntentFilter)

        val TechLists =
            arrayOf(arrayOf(Ndef::class.java.name), arrayOf(NdefFormatable::class.java.name))

        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, TechLists)
    }

    public open fun disableNFCInForeground(nfcAdapter: NfcAdapter, activity: Activity) {
        nfcAdapter.disableForegroundDispatch(activity)
    }
}

