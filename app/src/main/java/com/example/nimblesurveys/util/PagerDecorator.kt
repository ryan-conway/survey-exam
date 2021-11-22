package com.example.nimblesurveys.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.pow
import kotlin.math.sqrt

class PagerDecorator(
    private val indicatorPosX: Float,
    private val indicatorPosY: Float
) : RecyclerView.ItemDecoration() {

    private var paintStroke: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.WHITE
    }

    private val paintFill = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val indicators = mutableListOf<Pair<Float, Float>>()

    private val indicatorRadius = 15f
    private val indicatorPadding = 180f

    var activeIndicator = 0
        private set
    private var isInitialized = false

    private var currentItemCount = 0

    override fun onDrawOver(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.adapter?.let { adapter ->
            val itemCount = adapter.itemCount
            if (itemCount == 0) return@let
            if (itemCount != currentItemCount) {
                setupIndicators(parent)
                currentItemCount = itemCount
            }

            with(canvas) {
                for (i in 0 until itemCount) {
                    drawCircle(indicators[i].first, indicators[i].second)
                }
            }

            val visibleItem = (parent.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()

            if (visibleItem >= 0) {
                activeIndicator = visibleItem
            }

            indicators[activeIndicator].let { canvas.drawCircle(it.first, it.second, true) }
        }
    }

    private fun Canvas.drawCircle(x: Float, y: Float, isFill: Boolean = false) {
        drawCircle(x, y, indicatorRadius, if (isFill) paintFill else paintStroke)
    }

    private fun setupIndicators(recyclerView: RecyclerView) {
        val itemCount = recyclerView.adapter?.itemCount ?: 0
        if (itemCount == 0) return

        isInitialized = true

//        val indicatorTotalWidth = indicatorRadius * itemCount + indicatorPadding
//        val indicatorPosX = (recyclerView.width - indicatorTotalWidth) / 2f
//        val indicatorPosY = recyclerView.height - (indicatorRadius * 6 / 2f)
        val recyclerHeight = recyclerView.height

        for (i in 0 until itemCount) {
            indicators.add(indicatorPosX + (indicatorRadius * i * 4) to recyclerHeight - indicatorPosY)
        }
    }

    fun isIndicatorPressing(motionEvent: MotionEvent, recyclerView: RecyclerView): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                checkIfIndicatorPressing(motionEvent.x, motionEvent.y)?.let {
                    recyclerView.scrollToPosition(it)
                }
            }
        }
        return false
    }

    private fun checkIfIndicatorPressing(touchX: Float, touchY: Float): Int? {
        indicators.indices.forEach {
            // sqrt((x0-x1)*(x0-x1)+(y0-y1)*(y0-y1))<=r
            if (sqrt(
                    (indicators[it].first - touchX).toDouble().pow(2.0)
                            + (indicators[it].second - touchY).toDouble().pow(2.0)
                )
                <= indicatorRadius * 2
            ) {
                return it
            }
        }
        return null
    }
}