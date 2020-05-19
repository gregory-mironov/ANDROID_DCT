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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.R
import com.finnflare.scanner.CScannerViewModel
import com.finnflare.scanner.Item
import org.koin.android.ext.android.inject

class FactItemsListFragment : Fragment(), AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {
    private val scannerViewModel by inject<CScannerViewModel>()

    private var columnCount = 1

    private var listener: OnListFactItemsListFragmentInteractionListener? = null

    private lateinit var mAdapter: FactRecyclerViewAdapter

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
        val view = inflater.inflate(R.layout.fragment_fact, container, false)

        setSpinnerListener(view)
        setRecyclerAdapter(view)

        view.findViewById<SearchView>(R.id.factSearchView).setOnQueryTextListener(this)

        return view
    }

    private fun setRecyclerAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.f_fact_recycler)
        recyclerView.layoutManager = if (columnCount == 1) LinearLayoutManager(context)
        else GridLayoutManager(context, columnCount)

        mAdapter = FactRecyclerViewAdapter(scannerViewModel.factItemsListCorrect, listener)
        recyclerView.adapter = mAdapter
    }

    private fun setSpinnerListener(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.f_fact_spinner)
        spinner.onItemSelectedListener = this
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

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val choose = resources.getStringArray(R.array.f_fact_sninner_items)

        when (choose[position]) {
            getString(R.string.array_correct_fact) ->
                mAdapter.changeData(scannerViewModel.factItemsListCorrect)
            getString(R.string.array_wrong_fact) ->
                mAdapter.changeData(scannerViewModel.factItemsListWrong)
            else -> throw RuntimeException(context.toString() + " unknown spinner item")
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapter.filter.filter(newText.toString())
        return false
    }

    interface OnListFactItemsListFragmentInteractionListener {
        fun onListFactItemsListFragmentInteraction(item: Item?)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) = FactItemsListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCount)
            }
        }
    }
}