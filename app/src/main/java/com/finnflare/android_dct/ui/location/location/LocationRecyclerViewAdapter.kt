package com.finnflare.android_dct.ui.location.location


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finnflare.android_dct.Location
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.location.location.LocationChooseFragment.OnListLocationChooseFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_location_choose_item.view.*

class LocationRecyclerViewAdapter(
    private val mValues: List<Location>,
    private val mListener: OnListLocationChooseFragmentInteractionListener?
) : RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            mListener?.onListLocationChooseFragmentInteraction(v.tag as Location)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_location_choose_item, parent, false)
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

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.f_location_item_title
        val mSubtitleView: TextView = mView.f_location_item_subtitle

        override fun toString(): String {
            return super.toString() + mTitleView.text + " : "+ mSubtitleView.text
        }
    }
}
