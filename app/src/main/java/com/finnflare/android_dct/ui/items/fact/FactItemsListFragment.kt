package com.finnflare.android_dct.ui.items.fact

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import com.finnflare.scanner.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ObsoleteCoroutinesApi
class FactItemsListFragment : Fragment() {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var columnCount = 1

    private var listener: OnListFactItemsListFragmentInteractionListener? = null

    private lateinit var mAdapter: FactRecyclerViewAdapter

    private val observer = Observer<MutableList<Item>> {
        when (view?.findViewById<Spinner>(R.id.f_fact_spinner)?.selectedItem.toString()) {
            getString(R.string.array_correct_fact) ->
                mAdapter.changeData(scannerViewModel.getCorrectFactList())
            getString(R.string.array_wrong_fact) ->
                mAdapter.changeData(scannerViewModel.getWrongFactList())
            else -> mAdapter.changeData(listOf())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(
        R.layout.fragment_fact,
        container,
        false).apply {
            setRecyclerAdapter(this)
            setSpinnerListener(this)
            setSearchListener(this)
            setSwipeRefreshListener(this)
    }

    override fun onStart() {
        super.onStart()
        scannerViewModel.factItemsList.observe(this, observer)
    }

    override fun onStop() {
        super.onStop()
        scannerViewModel.factItemsList.removeObserver(observer)
    }

    private fun setRecyclerAdapter(view: View) {
        view.findViewById<RecyclerView>(R.id.f_fact_recycler).apply {
            this.layoutManager = GridLayoutManager(context, columnCount)

            mAdapter = FactRecyclerViewAdapter(
                listOf(),
                listener,
                this@FactItemsListFragment.requireContext()
            )

            this.adapter = mAdapter
        }
    }

    private fun setSpinnerListener(view: View) {
        view.findViewById<Spinner>(R.id.f_fact_spinner).apply {
            this.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) { }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when (resources.getStringArray(R.array.f_fact_sninner_items)[position]) {
                            getString(R.string.array_correct_fact) ->
                                mAdapter.changeData(scannerViewModel.getCorrectFactList())
                            getString(R.string.array_wrong_fact) ->
                                mAdapter.changeData(scannerViewModel.getWrongFactList())
                            else -> mAdapter.changeData(listOf())
                        }
                    }
                }
        }
    }

    private fun setSearchListener(view: View) {
        view.findViewById<SearchView>(R.id.factSearchView).setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    mAdapter.filter.filter(newText.toString())
                    return false
                }
            })
    }

    private fun setSwipeRefreshListener(view: View) {
        view.findViewById<SwipeRefreshLayout>(R.id.factSwipeRefresh).apply {
            this.setOnRefreshListener {
                CoroutineScope(scannerViewModel.scannerDispatcher).launch {
                    scannerViewModel.refreshItemsList()
                    this@apply.isRefreshing = false
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFactItemsListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnListFactItemsListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFactItemsListFragmentInteractionListener {
        fun onListFactItemsListFragmentInteraction(item: Item?)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int = 1) =
            FactItemsListFragment().apply {
                this.columnCount = columnCount
            }
    }
}