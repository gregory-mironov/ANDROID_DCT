package com.finnflare.scanner

import android.content.Context

interface IScanner {
    fun startBarcodeScan(context: Context)

    fun startRFIDScan(context: Context)
}