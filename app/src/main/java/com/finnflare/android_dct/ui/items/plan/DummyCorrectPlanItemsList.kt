package com.finnflare.android_dct.ui.items.plan

import com.finnflare.android_dct.ui.items.ItemsActivity
import java.util.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
object DummyCorrectPlanItemsList {

    /**
     * An array of sample (dummy) items.
     */
    val PLAN_ITEMS: MutableList<ItemsActivity.PlanFactListItem> = ArrayList()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            PLAN_ITEMS.add(createDummyItem(i))
        }
    }

    private fun createDummyItem(position: Int): ItemsActivity.PlanFactListItem {
        return ItemsActivity.PlanFactListItem(
            "Correct plan item " + position,
            "Some content"
        )
    }

}
