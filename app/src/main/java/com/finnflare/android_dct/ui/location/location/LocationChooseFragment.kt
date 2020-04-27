package com.finnflare.android_dct.ui.location.location

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finnflare.android_dct.R

import com.finnflare.android_dct.ui.location.location.DummyLocationChooseFragmentContent.LocationDummyItem

class LocationChooseFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListLocationChooseFragmentInteractionListener? = null

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
        val view = inflater.inflate(R.layout.fragment_location_choose, container, false)

        // Set the adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_location_choose_recycler)
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter =
                LocationRecyclerViewAdapter(
                    DummyLocationChooseFragmentContent.LOCATION_ITEMS,
                    listener
                )
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListLocationChooseFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnListLocationChooseFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListLocationChooseFragmentInteractionListener {
        fun onListLocationChooseFragmentInteraction(item: LocationDummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            LocationChooseFragment()
                .apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
