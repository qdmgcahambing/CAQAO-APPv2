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

        //Get the number of columns in grid layout
        val spanCount = layoutManager.spanCount
        // Get the total number of items in the adapter, or 0 if there is no adapter
        val childCount = parent.adapter?.itemCount ?: 0
        // Index or position of current item
        val childIndex = parent.getChildAdapterPosition(view)

        // Calculate the index of the last row in the grid layout
        val lastRowIndex = (childCount - 1) / spanCount
        // Calculate the index of the first item in the last row
        val lastRowFirstIndex = lastRowIndex * spanCount
        // Calculate the index of the last item in the last row
        val lastRowLastIndex = minOf(childCount - 1, lastRowFirstIndex + spanCount - 1)

        // Add margin to the bottom of the last row
        if (childIndex in lastRowFirstIndex..lastRowLastIndex) {
            outRect.bottom = marginBottom
        }
    }


}

