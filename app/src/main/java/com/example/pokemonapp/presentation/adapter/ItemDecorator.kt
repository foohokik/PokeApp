package com.example.pokemonapp.presentation.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(
    private val verticalDivider: Int,
    private val horizontalDivider: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val adapter = parent.adapter ?: return
        val currentPosition =
            parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: return

        val isLastView = adapter.itemCount - 1

        val oneSideInnerDivider = verticalDivider / 2
        val oneSideHorizontalDivider = horizontalDivider / 2

        with(outRect) {
            if (currentPosition == 0) {
                top = verticalDivider
            }
            bottom = when (currentPosition) {
                isLastView -> 0
                else -> oneSideInnerDivider
            }
            left = oneSideHorizontalDivider
            right = oneSideHorizontalDivider
        }
    }

}