package com.finnflare.scanner.camera

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.finnflare.scanner.IScanner
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class CCameraScanner(private val context: Context): IScanner {

    override fun init() { }

    override fun deinit() { }

    override fun startBarcodeScan(keyCode: Int, event: KeyEvent?) { }

    override fun stopBarcodeScan(keyCode: Int, event: KeyEvent?) { }

    override fun startBarcodeScanUI() {
        Dexter.withContext(context)
            .withPermission(android.Manifest.permission.CAMERA)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    context.startActivity(Intent(context, CameraScanActivity::class.java).apply {
                        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) { }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) { }
            })
            .check()
    }

    override fun stopBarcodeScanUI() { }

    override fun startRFIDScan(keyCode: Int, event: KeyEvent?) { }

    override fun stopRFIDScan(keyCode: Int, event: KeyEvent?) { }

    override fun startRFIDScanUI() { }

    override fun stopRFIDScanUI() { }
}