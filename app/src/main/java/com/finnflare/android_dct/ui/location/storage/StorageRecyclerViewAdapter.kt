package com.finnflare.android_dct.ui.location.storage

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.finnflare.android_dct.R


import com.finnflare.android_dct.ui.location.storage.StorageChooseFragment.OnListStorageChooseFragmentInteractionListener
import com.finnflare.android_dct.ui.location.storage.DummyStorageChooseFragmentContent.StorageDummyItem

import kotlinx.android.synthetic.main.fragment_location_choose_item.view.*

class StorageRecyclerViewAdapter(
    private val mValues: List<StorageDummyItem>,
    private val mListener: OnListStorageChooseFragmentInteractionListener?
) : RecyclerView.Adapter<StorageRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as StorageDummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListStorageChooseFragmentInteraction(item)
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
