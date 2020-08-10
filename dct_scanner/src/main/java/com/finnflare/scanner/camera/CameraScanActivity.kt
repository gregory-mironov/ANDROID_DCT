package com.finnflare.scanner.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.finnflare.scanner.CScannerViewModel
import com.finnflare.scanner.R
import com.finnflare.scanner.ScanDecoder
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.activity_continuous_scan.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

@ObsoleteCoroutinesApi
class CameraScanActivity: AppCompatActivity() {
    private val viewModel by inject<CScannerViewModel>()

    private val ARG_FLASH = "flash_arg"
    private val ARG_SCAN = "scan_arg"

    private var beepManager: BeepManager? = null

    private var mScan = true
    private var mFlash = false

    private var lastScanTime = 0L

    private val scanObserver = Observer<Triple<String, String, String>> {
        if (it.first.isEmpty() && it.second.isEmpty() && it.third.isEmpty())
            return@Observer

        CoroutineScope(viewModel.scannerDispatcher).launch {
            viewModel.increaseItemCount(it.first, it.second, it.third)

            val data = viewModel.getItemData(it.first, it.second, it.third)
            lifecycleScope.launch {
                full_description_of_item?.text = if (data.first.isNotEmpty()) data.first
                else resources.getString(R.string.items_empty_name_corrector)
            }

            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    viewModel.scanResult.value = Triple("", "", "")
                }
            }
        }
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (System.currentTimeMillis() - lastScanTime < 750)
                return

            beepManager!!.playBeepSoundAndVibrate()

            viewModel.scanResult.value = ScanDecoder.decodeScanResult(result.text)

            lastScanTime = System.currentTimeMillis()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continuous_scan)
        configureToolbar()

        val formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.EAN_13)

        barcode_scanner.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcode_scanner.setStatusText("")
        barcode_scanner.initializeFromIntent(intent)
        barcode_scanner.decodeContinuous(callback)

        beepManager = BeepManager(this)

        fab_resume_pause.setIcon(R.drawable.ic_resume)
        fab_resume_pause.setOnClickListener { onResumePauseClick() }

        savedInstanceState?.let{
            mScan = savedInstanceState.getBoolean(ARG_SCAN)
            mFlash = savedInstanceState.getBoolean(ARG_FLASH)
        }
    }

    override fun onResume() {
        super.onResume()
        barcode_scanner!!.resume()
        viewModel.scanResult.observe(this, scanObserver)
    }

    override fun onPause() {
        super.onPause()
        barcode_scanner!!.pause()
        viewModel.scanResult.removeObserver(scanObserver)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean(ARG_FLASH, mFlash)
        outState.putBoolean(ARG_SCAN, mScan)
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onResumePauseClick() {
        mScan = if (mScan) {
            fab_resume_pause.setIcon(R.drawable.ic_pause)
            barcode_scanner!!.pause()
            false
        } else{
            fab_resume_pause.setIcon(R.drawable.ic_resume)
            barcode_scanner!!.resume()
            true
        }
    }

    private fun configureToolbar(){
        val toolbar = findViewById<Toolbar>(R.id.scan_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.title_activity_scan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scan_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            R.id.scanner_flash_button ->{
                mFlash = if (mFlash) {
                    barcode_scanner.setTorchOff()
                    false
                } else{
                    barcode_scanner.setTorchOn()
                    true
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}