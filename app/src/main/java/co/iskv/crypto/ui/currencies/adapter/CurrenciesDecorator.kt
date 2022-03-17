package co.iskv.crypto.ui.currencies.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.iskv.crypto.R

class CurrenciesDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val horizontalMargin =
        context.resources.getDimensionPixelOffset(R.dimen._16dp)

    private val dividerHeight =
        context.resources.getDimensionPixelOffset(R.dimen._1dp)

    private val dividerPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.color_gray)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (adapterPosition == RecyclerView.NO_POSITION) continue
            if (i == parent.childCount - 1) return
            c.drawRect(
                horizontalMargin.toFloat(),
                child.bottom - dividerHeight / 2f + child.translationY,
                child.right - horizontalMargin.toFloat(),
                child.bottom + dividerHeight / 2f + child.translationY,
                dividerPaint
            )
        }
    }

}