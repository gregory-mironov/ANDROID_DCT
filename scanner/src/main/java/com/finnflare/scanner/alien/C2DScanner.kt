package com.finnflare.scanner.alien

import android.content.Context
import android.view.KeyEvent
import com.alien.barcode.BarcodeReader
import com.alien.common.KeyCode
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

    private fun startScan() {
        barcodeReader?.let {
            if (it.isRunning)
                return

            it.start { scanRes ->
                viewModel.scanResult.value = ScanDecoder.decodeScanResult(scanRes)
            }
        }
    }

    private fun stopScan() {
        barcodeReader?.let {
            if (it.isRunning)
                it.stop()
        }

    }

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode != KeyCode.ALR_H450.SCAN)
            return false

        stopScan()
        return true
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode != KeyCode.ALR_H450.SCAN || event.repeatCount != 0)
            return false

        startScan()
        return true
    }
}