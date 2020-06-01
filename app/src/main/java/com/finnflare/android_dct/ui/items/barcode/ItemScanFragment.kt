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
        return  if (enabled)
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

            scannerViewModel.scanResult.observe(viewLifecycleOwner, enabledScanObserver)
        }
    else
        inflater.inflate(R.layout.fragment_barcode_camera, container, false).apply {
            progressBar = this.findViewById(R.id.cameraProgressBar)

            correctCount = this.findViewById(R.id.camera_correct_items_count)

            wrongCount = this.findViewById(R.id.camera_wrong_items_count)

            planCount = this.findViewById(R.id.camera_plan_items_count)
            this.findViewById<Button>(R.id.cameraScanButton).setOnClickListener {
                scannerViewModel.scanner.startBarcodeScanUI()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateCounts()
    }

    private fun updateCounts() {
        lifecycleScope.launch {
            var correct = 0
            var wrong = 0

            scannerViewModel.factItemsList.value!!.distinct().forEach {
                if (it.barcodeCount > it.planCount) {
                    wrong += it.barcodeCount - it.planCount
                    correct += it.planCount
                }
                else
                    correct += it.barcodeCount
            }

            correctCount.text = correct.toString()
            wrongCount.text = wrong.toString()

            var plan = 0
            scannerViewModel.planItemsList.value!!.forEach { plan += it.planCount }
            planCount.text = plan.toString()

            if (enabled)
                return@launch

            progressBar?.let {
                it.max = plan + wrong
                it.progress = plan + wrong
                it.secondaryProgress = correct
            }
        }
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

        lifecycleScope.launch {
            CoroutineScope(scannerViewModel.scannerDispatcher).launch {
                scannerViewModel.increaseItemCount(it.first, it.second, it.third)
                scannerViewModel.scanResult.postValue(Triple("", "", ""))
            }.join()

            scannerViewModel.getItemData(it.first, it.second, it.third).apply {
                itemDescription?.text = this.first
                itemColor?.text = this.second.first
                itemSize?.text = this.second.second
                itemState?.text = this.second.third
            }

            updateCounts()
        }
    }
}