package com.mx.dialog.utils

import android.content.Context
import kotlin.math.roundToInt

object MXDialogUtils {
    fun dp2px(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density + 0.5f).roundToInt()
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    fun getNavigationBarHeight(context: Context): Int {
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }
}