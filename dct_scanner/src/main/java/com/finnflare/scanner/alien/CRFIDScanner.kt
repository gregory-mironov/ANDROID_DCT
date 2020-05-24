package com.finnflare.scanner.alien

import android.view.KeyEvent
import com.alien.common.KeyCode.ALR_H450
import com.alien.rfid.*
import com.finnflare.scanner.CScannerViewModel
import com.finnflare.scanner.ScanDecoder
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

@ObsoleteCoroutinesApi
object CRFIDScanner: RFIDCallback, KoinComponent {
    private val viewModel by inject<CScannerViewModel>()

    private var reader: RFIDReader? = null

    @Throws(ReaderException::class)
    fun init() {
        if (reader == null)
            reader = RFID.open()
    }

    fun deinit() {
        reader?.let{
            it.close()
            reader = null
        }
    }

    fun uiButtonStart() {
        reader?.let {
            if (!it.isRunning)
                it.inventory(this)
        }
    }

    fun uiButtonStop() { reader?.stop() }

    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean{
        if (event?.repeatCount != 0 || keyCode != ALR_H450.SCAN)
            return false

        reader?.let {
            if (!it.isRunning)
                it.inventory(this)
        }

        return true
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean{
        if (event?.repeatCount != 0 || keyCode != ALR_H450.SCAN)
            return false

        reader?.stop()

        return true
    }

    override fun onTagRead(tag: Tag) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                viewModel.rfidRSSI = tag.rssi
                viewModel.scanResult.value = ScanDecoder.decodeScanResult(tag.epc)
            }
        }
    }
}