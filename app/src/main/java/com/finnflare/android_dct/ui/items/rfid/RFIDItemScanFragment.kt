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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ObsoleteCoroutinesApi
class RFIDItemScanFragment : Fragment() {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var enabled = false

    private lateinit var progressBar: ProgressBar
    private lateinit var correctCount: TextView
    private lateinit var wrongCount: TextView
    private lateinit var planCount: TextView

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
            progressBar.progress = 100
            progressBar.secondaryProgress = 25

            correctCount = this.findViewById(R.id.rfid_correct_items_count)

            wrongCount = this.findViewById(R.id.rfid_wrong_items_count)

            planCount = this.findViewById(R.id.rfid_plan_items_count)

            this.findViewById<Button>(R.id.rfidScanButton).setOnClickListener {
                if (running) {
                    scannerViewModel.scanner.stopRFIDScanUI()
                    (it as Button).text = getString(R.string.start_scan)
                } else {
                    scannerViewModel.scanner.startRFIDScanUI()
                    (it as Button).text = getString(R.string.stop_scan)
                }

                running = !running
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (!enabled)
            return

        scannerViewModel.scanResult.observe(viewLifecycleOwner, Observer {
            if (it.first.isEmpty() && it.second.isEmpty() && it.third.isEmpty())
                return@Observer

            scannerViewModel.scanResult.postValue(Triple("", "", ""))
            CoroutineScope(scannerViewModel.scannerDispatcher).launch {
                scannerViewModel.increaseItemCount(it.first, it.second, it.third)
            }
        })

        scannerViewModel.planItemsCount.observe(this, Observer {
            progressBar.let { pb ->

                pb.max += it - if (planCount.text.toString()
                        .isEmpty()
                ) 0 else planCount.text.toString().toInt()

                pb.progress = pb.max
            }
            planCount.text = it.toString()
        })

        scannerViewModel.correctRfidItemsCount.observe(this, Observer {
            progressBar.secondaryProgress = it
            correctCount.text = it.toString()
        })

        scannerViewModel.wrongRfidItemsCount.observe(this, Observer {
            progressBar.let { pb ->

                pb.max += it - if (wrongCount.text.toString()
                        .isEmpty()
                ) 0 else wrongCount.text.toString().toInt()

                pb.progress = pb.max
            }
            wrongCount.text = it.toString()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(enabled: Boolean) =
            RFIDItemScanFragment().apply {
                this.enabled = enabled
            }
    }
}