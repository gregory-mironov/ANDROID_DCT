package com.finnflare.android_dct.ui.location.storage

import java.util.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
object DummyStorageChooseFragmentContent {

    /**
     * An array of sample (dummy) items.
     */
    val STORAGE_ITEMS: MutableList<StorageDummyItem> = ArrayList()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT)
            STORAGE_ITEMS.add(createDummyItem(i))
    }

    private fun createDummyItem(position: Int): StorageDummyItem {
        return StorageDummyItem(
            "Storage " + position,
            "User\'s comment " + position
        )
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class StorageDummyItem(val title: String, val subtitle: String) {
        override fun toString(): String = subtitle
    }
}
