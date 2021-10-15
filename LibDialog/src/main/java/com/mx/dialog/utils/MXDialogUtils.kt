package com.mx.dialog.utils

import android.content.Context
import kotlin.math.roundToInt

internal object MXDialogUtils {
    /**
     * dp - px单位转换
     */
    fun dp2px(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density + 0.5f).roundToInt()
    }

    /**
     * 获取顶部状态栏高度：px
     */
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    /**
     * 获取底部导航栏高度：px
     */
    fun getNavigationBarHeight(context: Context): Int {
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }
}