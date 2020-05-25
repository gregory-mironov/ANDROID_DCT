package com.finnflare.android_dct.ui.location.document


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.Document
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.location.document.DocumentChooseFragment.OnListDocumentChooseFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_document_choose_item.view.*
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.KoinComponent
import java.util.*

@ObsoleteCoroutinesApi
class DocumentRecyclerViewAdapter(
    private val mValues: List<Document>,
    private val mListener: OnListDocumentChooseFragmentInteractionListener?,
    private val mContext: Context
) : RecyclerView.Adapter<DocumentRecyclerViewAdapter.ViewHolder>(), Filterable, KoinComponent {

    private val mOnClickListener: View.OnClickListener

    private var mValuesFiltered = mValues

    init {
        mOnClickListener = View.OnClickListener { v ->
            mListener?.onListDocumentChooseFragmentInteraction(v.tag as Document)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_document_choose_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValuesFiltered[position]

        holder.mDocNumberView.text = item.title
        holder.mBasisView.text = item.basis
        holder.mAuditorView.text = item.auditor
        holder.mQtyView.text = mContext.resources.getString(
            R.string.item_count_string, item.qtyout, item.qtyin
        )

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValuesFiltered.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mDocNumberView: TextView = mView.f_document_item_number
        val mBasisView: TextView = mView.f_document_item_basis
        val mAuditorView: TextView = mView.f_document_item_auditor
        val mQtyView: TextView = mView.f_document_item_qty

        override fun toString(): String {
            return super.toString() + mDocNumberView.text + " : " + mBasisView.text
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mValuesFiltered = if (charString.isEmpty()) { mValues } else {
                    val filteredList = mutableListOf<Document>()
                    for (location in mValues) {
                        if (location.title.toLowerCase(Locale.ROOT)
                                .contains(charString.toLowerCase(Locale.ROOT))) {
                            filteredList.add(location)
                        }
                    }
                    filteredList
                }

                return FilterResults().apply { values = mValuesFiltered}
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                (filterResults.values as List<*>).asListOfType<Document>()?.let {
                    mValuesFiltered = it
                }
                notifyDataSetChanged()
            }
        }
    }

    inline fun <reified T> List<*>.asListOfType(): List<T>? =
        if (all { it is T })
            @Suppress("UNCHECKED_CAST")
            this as List<T> else
            null
}
