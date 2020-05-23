package com.finnflare.android_dct.ui.items.plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.R
import com.finnflare.scanner.Item
import kotlinx.android.synthetic.main.fragment_list_item.view.*
import java.util.*

class PlanRecyclerViewAdapter(
    private var mValues: List<Item>,
    private val mListener: PlanItemsListFragment.OnListPlanItemsListFragmentInteractionListener?,
    private val mContext: Context
) : RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder>(), Filterable{

    private val mOnClickListener: View.OnClickListener

    private var mValuesFiltered = mValues

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Item
            mListener?.onListPlanItemsListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValuesFiltered[position]
        holder.mTitleView.text = item.description
        holder.mBarcodeCountView.text = mContext.resources.getString(
            R.string.item_count_string, item.barcodeCount, item.planCount
        )
        holder.mRFIDCountView.text = mContext.resources.getString(
            R.string.item_count_string, item.rfidCount, item.planCount
        )

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValuesFiltered.size

    fun changeData(newValues: List<Item>) {
        mValues = newValues
        mValuesFiltered = newValues
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.f_items_item_title
        val mBarcodeCountView: TextView = mView.f_items_item_barcode_count
        val mRFIDCountView: TextView = mView.f_items_item_rfid_count
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mValuesFiltered = if (charString.isEmpty()) { mValues } else {
                    val filteredList = mutableListOf<Item>()
                    for (item in mValues) {
                        if (item.description.toLowerCase(Locale.ROOT)
                                .contains(charString.toLowerCase(Locale.ROOT))) {
                            filteredList.add(item)
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
                (filterResults.values as List<*>).asListOfType<Item>()?.let {
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
