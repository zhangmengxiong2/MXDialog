package com.mx.dialog.utils

import android.content.Context
import com.mx.dialog.BuildConfig
import kotlin.math.roundToInt

internal object MXDialogUtils {

    fun log(message: Any) {
        if (BuildConfig.DEBUG) {
            println("MXDialog - $message")
        }
    }

    /**
     * dp - px单位转换
     */
    fun dp2px(context: Context, dp: Number): Int {
        if (dp.toDouble() <= 0.0) return 0
        return (dp.toDouble() * context.resources.displayMetrics.density + 0.5f).roundToInt()
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