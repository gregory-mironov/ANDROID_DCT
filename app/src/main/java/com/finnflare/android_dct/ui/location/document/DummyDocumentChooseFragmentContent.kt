package com.finnflare.android_dct.ui.location.document

import java.util.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
object DummyDocumentChooseFragmentContent {

    /**
     * An array of sample (dummy) items.
     */
    val DOCUMENT_ITEMS: MutableList<DocumentDummyItem> = ArrayList()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT)
            DOCUMENT_ITEMS.add(createDummyItem(i))
    }

    private fun createDummyItem(position: Int): DocumentDummyItem {
        return DocumentDummyItem(
            "Document " + position,
            "User\'s comment " + position
        )
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DocumentDummyItem(val title: String, val subtitle: String) {
        override fun toString(): String = subtitle
    }
}
