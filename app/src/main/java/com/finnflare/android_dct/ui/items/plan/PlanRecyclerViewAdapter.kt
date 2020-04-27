package com.finnflare.android_dct.ui.items.plan

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.finnflare.android_dct.R

import kotlinx.android.synthetic.main.fragment_items_list_item.view.*

class PlanRecyclerViewAdapter(
    private val mValues: List<DummyPlanItemsListFragmentContent.PlanDummyItem>,
    private val mListener: PlanItemsListFragment.OnListPlanItemsListFragmentInteractionListener?
) : RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyPlanItemsListFragmentContent.PlanDummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListPlanItemsListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_items_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues.get(position)
        holder.mIdView.text = item.title
        holder.mContentView.text = item.subtitle

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.fragment_plan_item_title
        val mContentView: TextView = mView.fragment_plan_item_subtitle

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
