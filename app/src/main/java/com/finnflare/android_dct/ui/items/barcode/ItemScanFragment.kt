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
import androidx.lifecycle.lifecycleScope
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ObsoleteCoroutinesApi
class ItemScanFragment : Fragment() {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var enabled = false

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
        return if (enabled)
            inflater.inflate(R.layout.fragment_barcode_scanner, container, false).apply {
                correctCount = this.findViewById(R.id.barcode_correct_items_count)
                correctCount.text = scannerViewModel.correctBarcodeItemsCount.value.toString()

                wrongCount = this.findViewById(R.id.barcode_wrong_items_count)
                wrongCount.text = scannerViewModel.wrongBarcodeItemsCount.value.toString()

                planCount = this.findViewById(R.id.barcode_plan_items_count)
                planCount.text = scannerViewModel.planItemsCount.value.toString()

                itemDescription = this.findViewById(R.id.scanner_item_description)
                itemColor = this.findViewById(R.id.scanner_item_color)
                itemSize = this.findViewById(R.id.scanner_item_size)
                itemState = this.findViewById(R.id.scanner_item_state)

                scannerViewModel.scanResult.observe(viewLifecycleOwner, enabledScanObserver)
            }
        else
            inflater.inflate(R.layout.fragment_barcode_camera, container, false).apply {
                progressBar = this.findViewById(R.id.cameraProgressBar)

                correctCount = this.findViewById(R.id.camera_correct_items_count)
                correctCount.text = scannerViewModel.correctBarcodeItemsCount.value.toString()

                wrongCount = this.findViewById(R.id.camera_wrong_items_count)
                wrongCount.text = scannerViewModel.wrongBarcodeItemsCount.value.toString()

                planCount = this.findViewById(R.id.camera_plan_items_count)
                planCount.text = scannerViewModel.planItemsCount.value.toString()

                this.findViewById<Button>(R.id.cameraScanButton).setOnClickListener {
                    scannerViewModel.scanner.startBarcodeScanUI()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        if (enabled) {
            scannerViewModel.correctBarcodeItemsCount.observe(this, Observer {
                correctCount.text = it.toString()
            })

            scannerViewModel.wrongBarcodeItemsCount.observe(this, Observer {
                wrongCount.text = it.toString()
            })

            scannerViewModel.planItemsCount.observe(this, Observer {
                planCount.text = it.toString()
            })

            return
        }

        scannerViewModel.correctBarcodeItemsCount.observe(this, Observer {
            progressBar?.secondaryProgress = it
            correctCount.text = it.toString()
        })

        scannerViewModel.wrongBarcodeItemsCount.observe(this, Observer {
            progressBar?.let { pb ->

                pb.max += it - if (wrongCount.text.toString()
                        .isEmpty()
                ) 0 else wrongCount.text.toString().toInt()

                pb.progress = pb.max
            }
            wrongCount.text = it.toString()
        })

        scannerViewModel.planItemsCount.observe(this, Observer {
            progressBar?.let { pb ->

                pb.max += it - if (planCount.text.toString()
                        .isEmpty()
                ) 0 else planCount.text.toString().toInt()

                pb.progress = pb.max
            }
            planCount.text = it.toString()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(enabled: Boolean) =
            ItemScanFragment().apply {
                this.enabled = enabled
            }
    }

    private val enabledScanObserver = Observer<Triple<String, String, String>> {
        if (it.first.isEmpty() && it.second.isEmpty() && it.third.isEmpty())
            return@Observer

        scannerViewModel.scanResult.postValue(Triple("", "", ""))
        CoroutineScope(scannerViewModel.scannerDispatcher).launch {
            scannerViewModel.increaseItemCount(it.first, it.second, it.third)

            val data = scannerViewModel.getItemData(it.first, it.second, it.third)
            lifecycleScope.launch {
                itemDescription?.text =
                    if (data.first.isNotEmpty()) data.first
                    else resources.getString(R.string.items_empty_name_corrector)

                itemColor?.text = data.second.first
                itemSize?.text = data.second.second
                itemState?.text = data.second.third

            }
        }
    }
}