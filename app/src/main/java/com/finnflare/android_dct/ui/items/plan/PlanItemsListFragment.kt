package com.finnflare.android_dct.ui.items.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.R


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PlanItemsListFragment.OnListPlanItemsListFragmentInteractionListener] interface.
 */
class PlanItemsListFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListPlanItemsListFragmentInteractionListener? = null

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
        val view = inflater.inflate(R.layout.fragment_plan_items, container, false)

        // Set the adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_plan_items_recycler)
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = PlanRecyclerViewAdapter(DummyPlanItemsListFragmentContent.PLAN_ITEMS, listener)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListPlanItemsListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnListPlanItemsListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListPlanItemsListFragmentInteractionListener {
        fun onListPlanItemsListFragmentInteraction(item: DummyPlanItemsListFragmentContent.PlanDummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PlanItemsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}