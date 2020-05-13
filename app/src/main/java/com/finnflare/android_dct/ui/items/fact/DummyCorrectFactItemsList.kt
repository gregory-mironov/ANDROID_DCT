package com.finnflare.android_dct.ui.items.fact

import com.finnflare.android_dct.ui.items.ItemsActivity
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
object DummyCorrectFactItemsList {

    /**
     * An array of sample (dummy) items.
     */
    val FACT_ITEMS: MutableList<ItemsActivity.PlanFactListItem> = ArrayList()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT)
            FACT_ITEMS.add(createDummyItem(i))
    }

    private fun createDummyItem(position: Int): ItemsActivity.PlanFactListItem {
        return ItemsActivity.PlanFactListItem(
            "Correct fact item " + position, "Some content"
        )
    }

}
