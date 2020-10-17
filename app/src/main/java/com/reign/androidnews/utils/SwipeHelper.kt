package com.reign.androidnews.utils

import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

abstract class SwipeHelper: ItemTouchHelper.SimpleCallback(0,
    ItemTouchHelper.LEFT) {
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean = false

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val icon = ContextCompat.getDrawable(recyclerView.context,
            android.R.drawable.ic_menu_delete);
        val iconMargin: Int = (viewHolder.itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop: Int =
            viewHolder.itemView.top + (viewHolder.itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        val iconLeft: Int = viewHolder.itemView.right - iconMargin - icon.intrinsicWidth
        val iconRight: Int = viewHolder.itemView.right - iconMargin
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)


        c.clipRect(viewHolder.itemView.right + dX, viewHolder.itemView.top.toFloat(),
            viewHolder.itemView.right.toFloat() ,viewHolder.itemView.bottom.toFloat())

        val percent = dX.absoluteValue  / viewHolder.itemView.right
        c.drawColor(updateColor(percent))
        icon.draw(c)
    }

    /**
     * Helper function for getting swipe indicator color.
     * We go from white to red.
     */
    private fun updateColor(percent: Float): Int {
        var color = Color.LTGRAY
        val a = Color.alpha(color)
        var r = Color.red(color)
        var g = (Color.green(color) - Color.green(color) * percent).toInt()
        var b = (Color.blue(color) - Color.blue(color) * percent).toInt()

        return  Color.argb(
            a,
            r.coerceAtMost(255),
            g.coerceAtMost(255),
            b.coerceAtMost(255)
        )
    }
}