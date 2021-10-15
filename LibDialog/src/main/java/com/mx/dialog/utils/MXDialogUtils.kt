package com.mx.dialog.utils

import android.content.Context
import kotlin.math.roundToInt

internal object MXDialogUtils {
    fun dp2px(context: Context, dp: Double): Int {
        return (dp * context.resources.displayMetrics.density + 0.5f).roundToInt()
    }
}