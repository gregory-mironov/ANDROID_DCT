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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import com.finnflare.scanner.Item
import org.koin.android.ext.android.inject


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PlanItemsListFragment.OnListPlanItemsListFragmentInteractionListener] interface.
 */
class PlanItemsListFragment : Fragment(), AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var columnCount = 1

    private var listener: OnListPlanItemsListFragmentInteractionListener? = null

    private lateinit var mAdapter: PlanRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)

        setSpinnerListener(view)
        setRecyclerAdapter(view)

        view.findViewById<SearchView>(R.id.planSearchView).setOnQueryTextListener(this)

        return view
    }

    private fun setRecyclerAdapter(view: View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.f_plan_recycler)
        recyclerView.layoutManager = if (columnCount == 1) LinearLayoutManager(context)
        else GridLayoutManager(context, columnCount)

        mAdapter = PlanRecyclerViewAdapter(scannerViewModel.planItemsList, listener)
        recyclerView.adapter = mAdapter
    }

    private fun setSpinnerListener(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.f_plan_spinner)
        spinner.onItemSelectedListener = this
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

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val choose = resources.getStringArray(R.array.f_plan_sninner_items)

        when (choose[position]) {
            getString(R.string.array_plan_not_found) ->
                mAdapter.changeData(scannerViewModel.planItemsList)
            getString(R.string.array_plan_found) ->
                mAdapter.changeData(scannerViewModel.planItemsListFound)
            else -> throw RuntimeException(context.toString() + " unknown spinner item")
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapter.filter.filter(newText.toString())
        return false
    }

    interface OnListPlanItemsListFragmentInteractionListener {
        fun onListPlanItemsListFragmentInteraction(item: Item?)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) = PlanItemsListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCount)
            }
        }
    }
}