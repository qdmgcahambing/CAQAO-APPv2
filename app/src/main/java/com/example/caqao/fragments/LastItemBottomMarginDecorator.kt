package com.example.caqao.fragments

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LastItemBottomMarginDecorator(private val marginBottom: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemCount = parent.adapter?.itemCount ?: 0
        val itemPosition = parent.getChildAdapterPosition(view)

        if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.bottom = marginBottom
        }
    }
}