package com.finnflare.android_dct.ui.location.storage

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

import com.finnflare.android_dct.ui.location.storage.DummyStorageChooseFragmentContent.StorageDummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [StorageChooseFragment.OnListStorageChooseFragmentInteractionListener] interface.
 */
class StorageChooseFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListStorageChooseFragmentInteractionListener? = null

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
                StorageRecyclerViewAdapter(
                    DummyStorageChooseFragmentContent.STORAGE_ITEMS,
                    listener
                )
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListStorageChooseFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnListStorageChooseFragmentInteractionListener")
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
    interface OnListStorageChooseFragmentInteractionListener {
        fun onListStorageChooseFragmentInteraction(item: StorageDummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            StorageChooseFragment()
                .apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
