package com.example.caqao.fragments

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LastTwoItemBottomMarginDecorator(private val marginBottom: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return

        val spanCount = layoutManager.spanCount
        val childCount = parent.adapter?.itemCount ?: 0
        val childIndex = parent.getChildAdapterPosition(view)

        // Calculate the total number of rows in the grid layout
        val rowCount = (childCount + spanCount - 1) / spanCount
        // Calculate the index of the first item in the last row
        val lastRowFirstIndex = (rowCount - 1) * spanCount
        // Calculate the index of the last item in the last row
        val lastRowLastIndex = minOf(childCount - 1, lastRowFirstIndex + spanCount - 1)

        if (childCount % 2 == 0) {
            // Total number of items is even, apply margins to the last two items
            if (childIndex >= lastRowFirstIndex) {
                outRect.bottom = marginBottom
            }
        } else {
            // Total number of items is odd, apply margin to the last item
            if (childIndex == childCount - 1) {
                outRect.bottom = marginBottom
            }
        }
    }
}

