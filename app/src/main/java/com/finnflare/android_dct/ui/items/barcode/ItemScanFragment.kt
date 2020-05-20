package com.finnflare.android_dct.ui.items.barcode

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

class ItemScanFragment(private val enabled: Boolean) : Fragment() {
    private val scannerViewModel by inject<CScannerViewModel>()

    private lateinit var correctCount: TextView
    private lateinit var wrongCount: TextView
    private lateinit var planCount: TextView

    private var progressBar: ProgressBar? = null

    private var itemDescription: TextView? = null
    private var itemColor: TextView? = null
    private var itemSize: TextView? = null
    private var itemState: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureObservers()
        return if (enabled)
             inflater.inflate(R.layout.fragment_barcode_scanner, container, false).apply {
                 correctCount = this.findViewById(R.id.barcode_correct_items_count)
                 correctCount.text = "0"

                 wrongCount = this.findViewById(R.id.barcode_wrong_items_count)
                 correctCount.text = "0"

                 planCount = this.findViewById(R.id.barcode_plan_items_count)
                 planCount.text = "0"

                 itemDescription = this.findViewById(R.id.scanner_item_description)
                 itemColor = this.findViewById(R.id.scanner_item_color)
                 itemSize = this.findViewById(R.id.scanner_item_size)
                 itemState = this.findViewById(R.id.scanner_item_state)
            }
        else
            inflater.inflate(R.layout.fragment_barcode_camera, container, false).apply {
                progressBar = this.findViewById(R.id.cameraProgressBar)
                progressBar?.progress = 100
                progressBar?.secondaryProgress = 25

                correctCount = this.findViewById(R.id.camera_correct_items_count)
                correctCount.text = "0"

                wrongCount = this.findViewById(R.id.camera_wrong_items_count)
                wrongCount.text = "0"

                planCount = this.findViewById(R.id.camera_plan_items_count)
                planCount.text = "0"

                this.findViewById<Button>(R.id.cameraScanButton).setOnClickListener {
                    scannerViewModel.scanner.startBarcodeScanUI()
                }
            }
    }

    private fun configureObservers() {
        scannerViewModel.scanResult.observe(viewLifecycleOwner, Observer {
            when (scannerViewModel.increaseItemCount(it.first, it.second, it.third)) {
                -1 -> {}
                0 -> {
                    correctCount.text = (correctCount.text.toString().toInt() + 1).toString()

                    if (!enabled)
                        progressBar?.secondaryProgress = correctCount.text.toString().toInt()
                }
                1 -> {
                    wrongCount.text = (wrongCount.text.toString().toInt() + 1).toString()

                    if (!enabled)
                        progressBar?.progress =
                            planCount.text.toString().toInt() + wrongCount.text.toString().toInt()
                }
            }

            if (!enabled)
                return@Observer

            val item = scannerViewModel.getItemData(it.first, it.second, it.third)
            itemDescription?.text = item.first
            itemColor?.text = item.second.first
            itemSize?.text = item.second.second
            itemState?.text = item.second.third
        })
    }
}