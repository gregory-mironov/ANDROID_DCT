package com.finnflare.android_dct.ui.location.location

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.finnflare.android_dct.R


import com.finnflare.android_dct.ui.location.location.LocationChooseFragment.OnListLocationChooseFragmentInteractionListener
import com.finnflare.android_dct.ui.location.location.DummyLocationChooseFragmentContent.LocationDummyItem

import kotlinx.android.synthetic.main.fragment_location_choose_item.view.*

class LocationRecyclerViewAdapter(
    private val mValues: List<LocationDummyItem>,
    private val mListener: OnListLocationChooseFragmentInteractionListener?
) : RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as LocationDummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListLocationChooseFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_location_choose_item, parent, false)
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
        val mIdView: TextView = mView.location_textview
        val mContentView: TextView = mView.storages_number_textview

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
