package com.finnflare.android_dct.ui.location.location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.CUIViewModel
import com.finnflare.android_dct.Location
import com.finnflare.android_dct.R
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.android.inject

@ObsoleteCoroutinesApi
class LocationChooseFragment : Fragment(), SearchView.OnQueryTextListener {
    private val uiViewModel by inject<CUIViewModel>()

    private var columnCount = 1

    private var listener: OnListLocationChooseFragmentInteractionListener? = null

    private lateinit var mAdapter: LocationRecyclerViewAdapter

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

        mAdapter = LocationRecyclerViewAdapter(
            uiViewModel.locationList,
            listener
        )

        view.findViewById<RecyclerView>(R.id.f_location_recycler).let {
            it.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            it.adapter = mAdapter

            view.findViewById<SearchView>(R.id.locationSearchView).setOnQueryTextListener(this)
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapter.filter.filter(newText)
        return false
    }

    interface OnListLocationChooseFragmentInteractionListener {
        fun onListLocationChooseFragmentInteraction(item: Location)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) = LocationChooseFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCount)
            }
        }
    }
}
