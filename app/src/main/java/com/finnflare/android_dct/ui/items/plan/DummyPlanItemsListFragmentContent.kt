package com.finnflare.android_dct.ui.items.plan

import java.util.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
object DummyPlanItemsListFragmentContent {

    /**
     * An array of sample (dummy) items.
     */
    val PLAN_ITEMS: MutableList<PlanDummyItem> = ArrayList()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            PLAN_ITEMS.add(createDummyItem(i))
        }
    }

    private fun createDummyItem(position: Int): PlanDummyItem {
        return PlanDummyItem("Plan item " + position, "Some content")
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class PlanDummyItem(val title: String, val subtitle: String) {
        override fun toString(): String = subtitle
    }
}
