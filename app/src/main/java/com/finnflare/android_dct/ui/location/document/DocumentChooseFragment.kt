package com.finnflare.android_dct.ui.location.document

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.CUIViewModel
import com.finnflare.android_dct.Document
import com.finnflare.android_dct.R
import org.koin.android.ext.android.inject

class DocumentChooseFragment : Fragment() {
    private val uiViewModel by inject<CUIViewModel>()

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

        view.findViewById<RecyclerView>(R.id.f_document_recycler)?.let {
            it.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            it.adapter =
                DocumentRecyclerViewAdapter(
                    uiViewModel.documentList.toList(),
                    listener
                )
        }

        view.findViewById<EditText>(R.id.documentSearchEditText).addTextChangedListener {

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
        fun onListDocumentChooseFragmentInteraction(item: Document)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            DocumentChooseFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
