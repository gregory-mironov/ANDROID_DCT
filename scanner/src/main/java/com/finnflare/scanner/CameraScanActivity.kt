package com.finnflare.scanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.activity_continuous_scan.*

class CameraScanActivity: AppCompatActivity() {
    private val ARG_FLASH = "flash_arg"
    private val ARG_SCAN = "scan_arg"

    private var beepManager: BeepManager? = null

    private var mScan = true
    private var mFlash = false

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            onResumePauseClick()
            beepManager!!.playBeepSoundAndVibrate()
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
    }

    override fun onPause() {
        super.onPause()
        barcode_scanner!!.pause()
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