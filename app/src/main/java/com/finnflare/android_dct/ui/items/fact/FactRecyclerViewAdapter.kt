package com.finnflare.android_dct.ui.items.fact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.items.ItemsActivity
import kotlinx.android.synthetic.main.fragment_list_item.view.*

class FactRecyclerViewAdapter(
    private var mValues: List<ItemsActivity.PlanFactListItem>,
    private val mListener: FactItemsListFragment.OnListFactItemsListFragmentInteractionListener?
) : RecyclerView.Adapter<FactRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ItemsActivity.PlanFactListItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFactItemsListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues.get(position)
        holder.mTitleView.text = item.title
        holder.mSubtitleView.text = item.subtitle

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun changeData(newValues: List<ItemsActivity.PlanFactListItem>) {
        mValues = newValues
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.f_items_item_title
        val mSubtitleView: TextView = mView.f_items_item_subtitle

        override fun toString(): String {
            return super.toString() + " '" + mSubtitleView.text + "'"
        }
    }
}
