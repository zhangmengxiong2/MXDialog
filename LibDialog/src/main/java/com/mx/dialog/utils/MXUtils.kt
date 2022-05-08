package com.mx.dialog.utils

import android.content.Context
import com.mx.dialog.BuildConfig
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

internal object MXUtils {
    private var debug = BuildConfig.DEBUG
    fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    fun log(message: Any) {
        if (debug) {
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

    fun getScreenWidth(appContext: Context): Int = min(
        appContext.resources.displayMetrics.widthPixels,
        appContext.resources.displayMetrics.heightPixels
    )

    fun getScreenHeight(appContext: Context): Int = max(
        appContext.resources.displayMetrics.widthPixels,
        appContext.resources.displayMetrics.heightPixels
    )

    private fun getScreenDensity(appContext: Context): Float {
        val d = appContext.resources.displayMetrics.density
        if (d <= 0) return 1f
        return d
    }

    fun Float.asString(): String {
        return DecimalFormat("#0.00").format(this)
    }

    fun getScreenWidthDP(appContext: Context): Int =
        (getScreenWidth(appContext) / getScreenDensity(appContext)).roundToInt()

    fun getScreenHeightDP(appContext: Context): Int =
        (getScreenHeight(appContext) / getScreenDensity(appContext)).roundToInt()
}