package com.finnflare.android_dct.ui.items.search

import `in`.excogitation.zentone.library.ZenTone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.math.absoluteValue
import kotlin.math.ceil

class ItemSearchFragment : Fragment(), SearchView.OnQueryTextListener {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var enabled = false

    private var lastBeepTime = 0L

    private val scanResObserver = Observer<Triple<String, String, String>> {
        if (System.currentTimeMillis() - lastBeepTime < 700)
            return@Observer

        if (!scannerViewModel.cmpScanResWithSearchList(it.first, it.second, it.third))
            return@Observer

        val freq = (30000 / (ceil(scannerViewModel.rfidRSSI.absoluteValue))).toInt() / 10 * 10
        ZenTone.getInstance().generate(freq, 1, 1.0f
        ) { }

        GlobalScope.launch {
            delay(350)
            ZenTone.getInstance().stop()
        }

        lastBeepTime = System.currentTimeMillis() + 500
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!enabled)
            return inflater.inflate(R.layout.fragment_search_disabled, container, false)

        return inflater.inflate(R.layout.fragment_search_enabled, container, false).apply {
            this.findViewById<SearchView>(R.id.itemSearchSearchView)
                .setOnQueryTextListener(this@ItemSearchFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        if (enabled) {
            lastBeepTime = System.currentTimeMillis()
            scannerViewModel.scanResult.observe(this, scanResObserver)
        }
    }

    override fun onStop() {
        super.onStop()

        if (enabled)
            scannerViewModel.scanResult.removeObserver(scanResObserver)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        scannerViewModel.updateSearchList(query.toString())
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    companion object {
        @JvmStatic
        fun newInstance(enabled: Boolean) =
            ItemSearchFragment().apply {
                this.enabled = enabled
            }
    }
}