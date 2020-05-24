package com.finnflare.scanner.alien

import android.content.Context
import android.view.KeyEvent
import com.finnflare.scanner.IScanner
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
class CAlienScanner(private val context: Context): IScanner {

    override fun init() {
        C2DScanner.init(context)
        CRFIDScanner.init()
    }

    override fun deinit() {
        CRFIDScanner.deinit()
    }

    override fun startBarcodeScan(keyCode: Int, event: KeyEvent?) {
        C2DScanner.onKeyDown(keyCode, event)
    }

    override fun stopBarcodeScan(keyCode: Int, event: KeyEvent?) {
        C2DScanner.onKeyUp(keyCode, event)
    }

    override fun startBarcodeScanUI() {
        C2DScanner.uiButtonStart()
    }

    override fun stopBarcodeScanUI() {
        C2DScanner.uiButtonStop()
    }

    override fun startRFIDScan(keyCode: Int, event: KeyEvent?) {
        CRFIDScanner.onKeyDown(keyCode, event)
    }

    override fun stopRFIDScan(keyCode: Int, event: KeyEvent?) {
        CRFIDScanner.onKeyUp(keyCode, event)
    }

    override fun startRFIDScanUI() {
        CRFIDScanner.uiButtonStart()
    }

    override fun stopRFIDScanUI() {
        CRFIDScanner.uiButtonStop()
    }
}