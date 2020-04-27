package com.finnflare.android_dct.ui.location.location

import java.util.ArrayList

object DummyLocationChooseFragmentContent {

    /**
     * An array of sample (dummy) items.
     */
    val LOCATION_ITEMS: MutableList<LocationDummyItem> = ArrayList()

    private val COUNT = 40

    init {
        // Add some sample items.
        for (i in 1..COUNT)
            LOCATION_ITEMS.add(createDummyItem(i))
    }

    private fun createDummyItem(position: Int): LocationDummyItem {
        return LocationDummyItem(
            "Location " + position,
            'A' + (position - 1) % 26 + " storages"
        )
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class LocationDummyItem(val title: String, val subtitle: String) {
        override fun toString(): String = title + ": " + subtitle
    }
}
