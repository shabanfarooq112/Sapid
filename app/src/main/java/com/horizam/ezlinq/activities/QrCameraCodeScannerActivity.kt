package com.horizam.ezlinq.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrCameraCodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    var check = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)

        if ((PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ))
        ) {
            ActivityCompat.requestPermissions(
                this@QrCameraCodeScannerActivity,
                arrayOf(Manifest.permission.CAMERA), 1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                for (element in grantResults) {
                    if (grantResults.isNotEmpty() && element !=
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        check++
                    }
                }
                if (check > 0) {
                    Toast.makeText(
                        this,
                        "Go to Setting to Allow Camera Permission",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                }
                check = 0
                return
            }
        }

    }

    override fun onResume() {
        super.onResume()
        // Register ourselves as a handler for scan results.
        mScannerView!!.setResultHandler(this)
        // Start camera on resume
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        // Stop camera on pause
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Log.wtf("mytag", rawResult!!.getText()) // Prints scan results
        Log.wtf(
            "mytag",
            rawResult.getBarcodeFormat().toString()
        ) // Prints the scan format (qrcode, pdf417 etc.)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("load", "OtherUserProfileFragment")
        intent.putExtra("url", rawResult.text.toString())
        startActivity(intent)

        mScannerView!!.resumeCameraPreview(this)
    }
}