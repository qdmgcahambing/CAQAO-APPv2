package com.example.caqao.fragments

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridSpanSizeLookup(private val adapter: RecyclerView.Adapter<*>, private val spanCount: Int) :
    GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        return when (adapter.getItemViewType(position)) {
            ITEM_VIEW_TYPE_HEADER -> spanCount
            ITEM_VIEW_TYPE_ITEM -> 1
            else -> throw IllegalStateException("Unknown view type")
        }
    }

    companion object {
        const val ITEM_VIEW_TYPE_HEADER = 0
        const val ITEM_VIEW_TYPE_ITEM = 1
    }
}
