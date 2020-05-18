package com.finnflare.android_dct.ui.items.barcode

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import org.koin.android.ext.android.inject

class ItemScanFragment(private val enabled: Boolean) : Fragment() {
    private val scannerViewModel by inject<CScannerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureObservers()

        if (enabled)
            return inflater.inflate(R.layout.fragment_barcode_scanner, container, false)

        return inflater.inflate(R.layout.fragment_barcode_camera, container, false).apply {
            this.findViewById<Button>(R.id.cameraScanButton).setOnClickListener {
                scannerViewModel.scanner.startBarcodeScan(KeyEvent.KEYCODE_F12, null)
            }
        }
    }

    private fun configureObservers() {
        if (enabled)
            scannerViewModel.scanResult.observe(viewLifecycleOwner, Observer {

            })
        else
            scannerViewModel.scanResult.observe(viewLifecycleOwner, Observer {

            })
    }
}