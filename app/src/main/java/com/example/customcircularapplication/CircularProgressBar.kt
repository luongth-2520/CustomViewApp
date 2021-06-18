package com.example.customcircularapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.Nullable

class CircularProgressBar(context: Context, @Nullable attrs: AttributeSet) : View(context, attrs) {

    private var paint: Paint
    private var ringColor: Int
    private var loadColor: Int
    private var ringWidth: Int
    private var textColor: Int
    private var textFontSize: Int
    private var progress: Int = 0
    private var progressType: Int
    private var isShowText: Int
    private var rect: Rect

    init {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0)
        ringColor = typedArray.getColor(R.styleable.CircleProgressBar_ringColor, Color.GRAY)
        loadColor = typedArray.getColor(R.styleable.CircleProgressBar_loadColor, Color.GREEN)
        ringWidth = typedArray.getDimensionPixelSize(
            R.styleable.CircleProgressBar_ringWidth,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)
                .toInt()
        )
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.BLACK)
        textFontSize = typedArray.getDimensionPixelSize(
            R.styleable.CircleProgressBar_textSize,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics
            ).toInt()
        )
        progress = typedArray.getInt(R.styleable.CircleProgressBar_progress, 1)
        progressType = typedArray.getInt(R.styleable.CircleProgressBar_progressType, 1)
        isShowText = typedArray.getInt(R.styleable.CircleProgressBar_isShowText, 1)
        typedArray.recycle()
        rect = Rect()
        paint = Paint().apply {
            isAntiAlias = true
            getTextBounds(PERCENT_TEXT, 0, PERCENT_TEXT.length, rect)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = centerX - ringWidth / 2
        paint.apply {
            strokeWidth = ringWidth.toFloat()
            textSize = textFontSize.toFloat()
            style = Paint.Style.STROKE
            color = ringColor
        }
        canvas?.drawCircle(centerX, centerY, radius, paint)
        paint.apply {
            color = loadColor
            strokeWidth = ringWidth.toFloat()
        }
        when (progressType) {
            STROKE -> {
                paint.style = Paint.Style.STROKE
                val ovalStroke =
                    RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
                canvas?.drawArc(ovalStroke, -90f, -progress * 360f / MAX_PROGRESS, false, paint)
            }
            FILL -> {
                paint.style = Paint.Style.FILL
                paint.color = Color.YELLOW
                val ovalFill = RectF(
                    centerX - radius + ringWidth / 2,
                    centerY - radius + ringWidth / 2,
                    centerX + radius - ringWidth / 2,
                    centerY + radius - ringWidth / 2
                )
                canvas?.drawArc(ovalFill, -90f, -progress * 360f / MAX_PROGRESS, true, paint)
            }
        }
        if (isShowText == 1)
            return
        val percent = progress
        paint.apply {
            color = textColor
            style = Paint.Style.FILL
            strokeWidth = 3f
        }
        val measureTextWidth = paint.measureText("$percent$PERCENT_TEXT")
        canvas?.drawText(
            "$percent $PERCENT_TEXT",
            centerX - measureTextWidth / 2,
            centerY + rect.height() / 2,
            paint
        )
    }

    fun setProgress(value: Int) {
        progress = value
        invalidate()
    }

    companion object {
        private const val FILL = 0
        private const val STROKE = 1
        private const val MAX_PROGRESS: Int = 100
        private const val PERCENT_TEXT = "%"
    }
}
