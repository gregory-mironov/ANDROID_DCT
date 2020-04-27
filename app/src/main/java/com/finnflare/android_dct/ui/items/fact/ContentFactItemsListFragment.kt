package com.finnflare.android_dct.ui.items.plan

import java.util.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
object ContentFactItemsListFragment {

    /**
     * An array of sample (dummy) items.
     */
    val FACT_ITEMS: MutableList<FactDummyItem> = ArrayList()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT)
            FACT_ITEMS.add(createDummyItem(i))
    }

    private fun createDummyItem(position: Int): FactDummyItem {
        return FactDummyItem("Fact item " + position, "Some content")
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class FactDummyItem(val title: String, val subtitle: String) {
        override fun toString(): String = subtitle
    }
}
