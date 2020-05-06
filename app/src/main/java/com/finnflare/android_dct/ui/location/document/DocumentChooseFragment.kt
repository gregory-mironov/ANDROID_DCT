package com.finnflare.android_dct.ui.location.document

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.location.document.DummyDocumentChooseFragmentContent.DocumentDummyItem

class DocumentChooseFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListDocumentChooseFragmentInteractionListener? = null

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
        val view = inflater.inflate(R.layout.fragment_document_choose, container, false)

        // Set the adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.f_document_recycler)
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter =
                DocumentRecyclerViewAdapter(
                    DummyDocumentChooseFragmentContent.DOCUMENT_ITEMS,
                    listener
                )
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListDocumentChooseFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnListDocumentChooseFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListDocumentChooseFragmentInteractionListener {
        fun onListDocumentChooseFragmentInteraction(item: DocumentDummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            DocumentChooseFragment()
                .apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
