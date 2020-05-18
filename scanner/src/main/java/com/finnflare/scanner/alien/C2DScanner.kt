package com.finnflare.scanner.alien

import android.content.Context
import android.view.KeyEvent
import com.alien.barcode.BarcodeReader
import com.alien.common.KeyCode.ALR_H450
import com.finnflare.scanner.CScannerViewModel
import com.finnflare.scanner.ScanDecoder
import org.koin.core.KoinComponent
import org.koin.core.inject

object C2DScanner: KoinComponent {
    private val viewModel by inject<CScannerViewModel>()

    private var barcodeReader: BarcodeReader? = null


    fun init(context: Context) {
        if (barcodeReader == null)
            barcodeReader = BarcodeReader(context)
    }

    fun uiButtonStart() {
        barcodeReader?.let {
            if (it.isRunning)
                return@let

            it.start { scanRes ->
                viewModel.scanResult.value = ScanDecoder.decodeScanResult(scanRes)
            }
        }
    }

    fun uiButtonStop() {
        barcodeReader?.let {
            if (it.isRunning)
                it.stop()
        }
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.repeatCount != 0 || keyCode != ALR_H450.SCAN)
            return false

        barcodeReader?.let {
            if (it.isRunning)
                return@let

            it.start { scanRes ->
                viewModel.scanResult.value = ScanDecoder.decodeScanResult(scanRes)
            }
        }

        return true
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.repeatCount != 0 || keyCode != ALR_H450.SCAN)
            return false

        barcodeReader?.let {
            if (it.isRunning)
                it.stop()
        }

        return true
    }
}