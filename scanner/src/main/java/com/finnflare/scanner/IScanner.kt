package com.finnflare.scanner

import android.view.KeyEvent

interface IScanner {
    fun init()

    fun deinit()

    fun startBarcodeScan(keyCode: Int, event: KeyEvent?)

    fun stopBarcodeScan(keyCode: Int, event: KeyEvent?)

    fun startBarcodeScanUI()

    fun stopBarcodeScanUI()

    fun startRFIDScan(keyCode: Int, event: KeyEvent?)

    fun stopRFIDScan(keyCode: Int, event: KeyEvent?)

    fun startRFIDScanUI()

    fun stopRFIDScanUI()
}