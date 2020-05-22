package com.finnflare.android_dct.ui.items.rfid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import org.koin.android.ext.android.inject

class RFIDItemScanFragment : Fragment() {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var enabled = false

    private val scanResObserver = Observer<Triple<String, String, String>> {
        when (scannerViewModel.increaseItemCount(it.first, it.second, it.third)) {
            -1 -> {}
            0 -> {
                correctCount?.text = (correctCount?.text.toString().toInt() + 1).toString()

                if (!enabled)
                    progressBar?.secondaryProgress = correctCount?.text.toString().toInt()
            }
            1 -> {
                wrongCount?.text = (wrongCount?.text.toString().toInt() + 1).toString()

                if (!enabled)
                    progressBar?.progress =
                        planCount?.text.toString().toInt() + wrongCount?.text.toString().toInt()
            }
        }
    }

    private var progressBar: ProgressBar? = null
    private var correctCount: TextView? = null
    private var wrongCount: TextView? = null
    private var planCount: TextView? = null

    private var running = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!enabled)
            return inflater.inflate(R.layout.fragment_rfid_disabled, container, false)

        return inflater.inflate(R.layout.fragment_rfid_enabled, container, false).apply {
            progressBar = this.findViewById(R.id.rfidProgressBar)
            progressBar?.progress = 100
            progressBar?.secondaryProgress = 25

            correctCount = this.findViewById(R.id.rfid_correct_items_count)
            correctCount?.text = "0"

            wrongCount = this.findViewById(R.id.rfid_wrong_items_count)
            wrongCount?.text = "0"

            planCount = this.findViewById(R.id.rfid_plan_items_count)
            planCount?.text = "0"

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

    override fun onStart() {
        super.onStart()
        if (enabled)
            scannerViewModel.scanResult.observe(viewLifecycleOwner, scanResObserver)
    }

    override fun onStop() {
        super.onStop()
        if (enabled)
            scannerViewModel.scanResult.removeObserver(scanResObserver)
    }

    companion object {
        @JvmStatic
        fun newInstance(enabled: Boolean) =
            RFIDItemScanFragment().apply {
                this.enabled = enabled
            }
    }
}