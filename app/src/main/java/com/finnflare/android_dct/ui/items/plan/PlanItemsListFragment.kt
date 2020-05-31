package com.finnflare.android_dct.ui.items.plan

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import com.finnflare.scanner.Item
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ObsoleteCoroutinesApi
class PlanItemsListFragment : Fragment() {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var docId = ""
    private var columnCount = 1

    private var listener: OnListPlanItemsListFragmentInteractionListener? = null

    private lateinit var mAdapter: PlanRecyclerViewAdapter

    private val observer = Observer<MutableList<Item>> {
//        mAdapter.changeData()
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
    ): View? = inflater.inflate(
        R.layout.fragment_plan,
        container,
        false).apply {
            setRecyclerAdapter(this)
            setSpinnerListener(this)
            setSearchListener(this)
            setSwipeRefreshListener(this)
    }

    override fun onStart() {
        super.onStart()
        scannerViewModel.planItemsList.observe(this, observer)
    }

    override fun onStop() {
        super.onStop()
        scannerViewModel.planItemsList.removeObserver(observer)
    }

    private fun setRecyclerAdapter(view: View) {
        view.findViewById<RecyclerView>(R.id.f_plan_recycler).apply {
            this.layoutManager = GridLayoutManager(context, columnCount)

            mAdapter = PlanRecyclerViewAdapter(
                listOf(),
                listener,
                this@PlanItemsListFragment.requireContext()
            )

            this.adapter = mAdapter
        }
    }

    private fun setSpinnerListener(view: View) {
        view.findViewById<Spinner>(R.id.f_plan_spinner).onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (resources.getStringArray(R.array.f_plan_sninner_items)[position]) {
                        getString(R.string.array_plan_not_found) ->
                            mAdapter.changeData(scannerViewModel.getPlanListNotFound())
                        getString(R.string.array_plan_found) ->
                            mAdapter.changeData(scannerViewModel.getPlanListFound())
                        else -> mAdapter.changeData(listOf())
                    }
                }
            }
    }

    private fun setSearchListener(view: View) {
        view.findViewById<SearchView>(R.id.planSearchView).setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    mAdapter.filter.filter(newText.toString())
                    return false
                }
            })
    }

    private fun setSwipeRefreshListener(view: View) {
        view.findViewById<SwipeRefreshLayout>(R.id.planSwipeRefresh).apply {
            this.setOnRefreshListener {
                lifecycleScope.launch {
                    scannerViewModel.refreshItemsList()
                    this@apply.isRefreshing = false
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListPlanItemsListFragmentInteractionListener)
            listener = context
        else
            throw RuntimeException(context.toString() +
                    " must implement OnListPlanItemsListFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListPlanItemsListFragmentInteractionListener {
        fun onListPlanItemsListFragmentInteraction(item: Item?)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int = 1) =
            PlanItemsListFragment().apply {
                this.columnCount = columnCount
            }
    }
}