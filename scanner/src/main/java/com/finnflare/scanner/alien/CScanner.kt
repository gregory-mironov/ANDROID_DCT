package com.finnflare.scanner.alien

import android.content.Context
import com.finnflare.scanner.IScanner

class CScanner: IScanner {
    override fun startBarcodeScan(context: Context) {
        C2DScanner.init(context)
    }

    override fun startRFIDScan(context: Context) {
        CRFIDScanner.init()
    }
}