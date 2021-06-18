package com.example.customcircularapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.ceil

private const val TAG = "SimpleLineChart"

class SimpleLineChart(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var chartWidth = 0
    private var chartHeight = 0
    private var textFontSize = ConvertUtil.spToPx(DEFAULT_FONT_SIZE, context)
    private var lineColor = Color.CYAN
    private var lineStrokeWidth = ConvertUtil.dpToPx(DEFAULT_STROKE_WIDTH, context)
    private var pointMap: HashMap<Int, Int> = hashMapOf()
    private var pointRadius = ConvertUtil.dpToPx(DEFAULT_POINT_RADIUS, context)
    private var xAxisData: Array<String>? = null
    private var yAxisData: Array<String>? = null
    private val paintAxis = Paint().apply {
        textSize = textFontSize
        color = Color.BLUE
    }
    private val paintPoint = Paint().apply {
        color = lineColor
    }
    private val paintLine = Paint().apply {
        color = lineColor
        isAntiAlias = true
        strokeWidth = lineStrokeWidth
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) {
            chartWidth = widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            throw IllegalArgumentException(resources.getString(R.string.msg_width_must_be_exact))
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            chartHeight = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            throw IllegalArgumentException(resources.getString(R.string.msg_height_must_be_exact))
        }
        setMeasuredDimension(chartWidth, chartHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (xAxisData == null || yAxisData == null || xAxisData?.size == 0 || yAxisData?.size == 0) {
            throw IllegalArgumentException(resources.getString(R.string.msg_x_or_y_null))
        }

        if (pointMap.size == 0) {
            val textLength = paintAxis.measureText(resources.getString(R.string.msg_no_data))
            canvas?.drawText(
                resources.getString(R.string.msg_no_data),
                chartWidth / 2 - textLength / 2,
                chartHeight / 2f,
                paintAxis
            )
        } else {
            val yPoints = FloatArray(yAxisData!!.size)
            val yInterval = (chartHeight - textFontSize - 2f) / yAxisData!!.size

            for ((i, item) in yAxisData!!.withIndex()) {
                canvas?.drawText(item, 0f, chartHeight - textFontSize - i * yInterval, paintAxis)
                yPoints[i] = chartHeight - textFontSize - i * yInterval
                Log.d(TAG, "onDraw: $i")
            }

            val xPoints = FloatArray(xAxisData!!.size)
            val xItemX = paintAxis.measureText(yAxisData!![1])
            val xOffset = 50
            val xInterval = (chartWidth - xOffset) / xAxisData!!.size
            val xItemY = textFontSize + yAxisData!!.size * yInterval
            for ((i, item) in xAxisData!!.withIndex()) {
                canvas?.drawText(item, i * xInterval + xItemX + xOffset, xItemY, paintAxis)
                xPoints[i] = i * xInterval + xItemX + paintAxis.measureText(item) / 2 + xOffset + 10
            }

            for (i in xAxisData!!.indices) {
                if (pointMap[i] == null) {
                    throw IllegalArgumentException(resources.getString(R.string.msg_incomplete_pointmap))
                }
                canvas?.drawCircle(xPoints[i], yPoints[pointMap[i]!!], pointRadius, paintPoint)
                if (i > 0) {
                    canvas?.drawLine(
                        xPoints[i - 1],
                        yPoints[pointMap[i - 1]!!],
                        xPoints[i],
                        yPoints[pointMap[i]!!],
                        paintLine
                    )
                }
            }
        }
    }

    fun setPointMap(data: HashMap<Int, Int>) {
        pointMap = data
        invalidate()
    }

    fun setXItem(xItems: Array<String>) {
        xAxisData = xItems
    }

    fun setYItem(yItems: Array<String>) {
        yAxisData = yItems
    }

    fun setLineColor(color: Int) {
        lineColor = color
        invalidate()
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 16f
        private const val DEFAULT_STROKE_WIDTH = 3f
        private const val DEFAULT_POINT_RADIUS = 5f
    }
}
