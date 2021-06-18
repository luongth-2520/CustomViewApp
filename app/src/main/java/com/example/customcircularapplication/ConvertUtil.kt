package com.example.customcircularapplication

import android.content.Context
import android.util.TypedValue


object ConvertUtil {

    fun dpToPx(dp: Float, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.getResources().getDisplayMetrics()
        )
    }

    fun spToPx(sp: Float, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }

    fun dpToSp(dp: Float, context: Context): Float {
        return (dpToPx(dp, context) / context.resources.displayMetrics.scaledDensity)
    }
}
