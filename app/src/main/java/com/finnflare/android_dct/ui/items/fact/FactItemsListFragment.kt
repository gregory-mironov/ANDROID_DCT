package com.finnflare.android_dct.ui.items.fact

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.items.ItemsActivity

class FactItemsListFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var columnCount = 1

    private var listener: OnListFactItemsListFragmentInteractionListener? = null

    private var adapter: FactRecyclerViewAdapter? = null

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

        return view
    }

    private fun setRecyclerAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.f_fact_recycler)
        recyclerView.layoutManager = when {
            columnCount == 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        adapter = FactRecyclerViewAdapter(DummyCorrectFactItemsList.FACT_ITEMS, listener)
        recyclerView.adapter = adapter
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

    interface OnListFactItemsListFragmentInteractionListener {
        fun onListFactItemsListFragmentInteraction(item: ItemsActivity.PlanFactListItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            FactItemsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val choose = resources.getStringArray(R.array.f_fact_sninner_items)

        when (choose[position]) {
            getString(R.string.array_correct_fact) ->
                adapter?.changeData(DummyCorrectFactItemsList.FACT_ITEMS)
            getString(R.string.array_wrong_fact) ->
                adapter?.changeData(DummyWrongFactItemsList.FACT_ITEMS)
            else -> throw RuntimeException(context.toString() + " unknown spinner item")
        }
    }
}