package com.example.customcircularapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import java.util.*

class ClockView(context: Context, attrs: AttributeSet) : View(context, attrs), Handler.Callback {

    private var clockWidth = 0
    private var clockHeight = 0
    private var paintLine: Paint
    private var paintCircle: Paint
    private var paintHour: Paint
    private var paintMinute: Paint
    private var paintSecond: Paint
    private var paintText: Paint
    private var calendar: Calendar
    private val clockHandler: Handler = Handler(Looper.myLooper()!!, this)

    init {
        calendar = Calendar.getInstance()
        paintLine = Paint().apply {
            color = Color.BLUE
            strokeWidth = 10f
        }
        paintCircle = Paint().apply {
            color = Color.GREEN
            strokeWidth = 10f
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        paintText = Paint().apply {
            color = Color.BLUE
            strokeWidth = 10f
            textAlign = Paint.Align.CENTER
            textSize = 40f
        }
        paintHour = Paint().apply {
            strokeWidth = 20f
            color = Color.BLUE
        }
        paintMinute = Paint().apply {
            strokeWidth = 15f
            color = Color.BLUE
        }
        paintSecond = Paint().apply {
            strokeWidth = 10f
            color = Color.BLUE
        }
        clockHandler.sendEmptyMessage(NEED_INVALIDATE)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        clockWidth = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        clockHeight = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(clockWidth, clockHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val circleRadius = 400f
        canvas?.drawCircle(clockWidth / 2f, clockHeight / 2f, circleRadius, paintCircle)
        canvas?.drawCircle(clockWidth / 2f, clockHeight / 2f, 20f, paintCircle)
        for (i in 1..12) {
            canvas?.save()
            canvas?.rotate(30f * i, clockWidth / 2f, clockHeight / 2f)
            canvas?.drawLine(
                clockWidth / 2f,
                clockHeight / 2 - circleRadius,
                clockWidth / 2f,
                clockHeight / 2 - circleRadius + 30,
                paintCircle
            )
            canvas?.drawText(
                "${i}",
                clockWidth / 2f,
                clockHeight / 2 - circleRadius + 70,
                paintText
            )
            canvas?.restore()
        }
        val minute = calendar.get(Calendar.MINUTE)
        val hour = calendar.get(Calendar.HOUR)
        val sec = calendar.get(Calendar.SECOND)
        val minuteDegree = minute / 60f * 360
        canvas?.save()
        canvas?.rotate(minuteDegree, clockWidth / 2f, clockHeight / 2f)
        canvas?.drawLine(
            clockWidth / 2f,
            clockHeight / 2f - 250,
            clockWidth / 2f,
            clockHeight / 2f + 40,
            paintMinute
        )
        canvas?.restore()
        val hourDegree = (hour * 60 + minute) / 12f / 60 * 360
        canvas?.rotate(hourDegree, clockWidth / 2f, clockHeight / 2f)
        canvas?.save()
        canvas?.drawLine(
            clockWidth / 2f,
            clockHeight / 2f - 200,
            clockWidth / 2f,
            clockHeight / 2f + 30,
            paintHour
        )
        canvas?.restore()
        val secDegree = sec / 60f * 360
        canvas?.save()
        canvas?.rotate(secDegree, clockWidth / 2f, clockHeight / 2f)
        canvas?.drawLine(
            clockWidth / 2f,
            clockHeight / 2f - 300,
            clockWidth / 2f,
            clockHeight / 2f + 40,
            paintSecond
        )
        canvas?.restore()
    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            NEED_INVALIDATE -> {
                calendar = Calendar.getInstance()
                invalidate()
                clockHandler.sendEmptyMessageDelayed(NEED_INVALIDATE, 1000)
            }
        }
        return true
    }

    companion object {
        private const val NEED_INVALIDATE = 888
    }
}
