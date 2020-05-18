package com.finnflare.android_dct.ui.items.rfid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import org.koin.android.ext.android.inject

class RFIDItemScanFragment(private val enabled: Boolean) : Fragment() {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var running = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!enabled)
            return inflater.inflate(R.layout.fragment_rfid_disabled, container, false)

        configureObservers()

        return inflater.inflate(R.layout.fragment_rfid_enabled, container, false).apply {
            this.findViewById<Button>(R.id.rfidScanButton).setOnClickListener {
                if (running) {
                    scannerViewModel.scanner.stopRFIDScanUI()
                    (it as Button).text = getString(R.string.start_scan)
                }
                else {
                    scannerViewModel.scanner.startRFIDScanUI()
                    (it as Button).text = getString(R.string.stop_scan)
                }

                running = !running
            }
        }
    }

    private fun configureObservers() {
        scannerViewModel.scanResult.observe(viewLifecycleOwner, Observer {

        })
    }
}