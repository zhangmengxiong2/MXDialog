package com.mx.dialog.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable

internal object MXDrawableUtils {
    /**
     * 构建圆角背景图
     */
    fun buildGradientDrawable(context: Context, color: Int, cornerDP: Float): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = MXDialogUtils.dp2px(context, cornerDP).toFloat()
        drawable.setColor(color)
        return drawable
    }

    /**
     * 构建圆角背景图
     */
    fun buildGradientDrawable(
        context: Context,
        color: Int,
        cornerArrayDP: FloatArray,
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(
            MXDialogUtils.dp2px(context, cornerArrayDP[0]).toFloat(),
            MXDialogUtils.dp2px(context, cornerArrayDP[0]).toFloat(),
            MXDialogUtils.dp2px(context, cornerArrayDP[1]).toFloat(),
            MXDialogUtils.dp2px(context, cornerArrayDP[1]).toFloat(),
            MXDialogUtils.dp2px(context, cornerArrayDP[2]).toFloat(),
            MXDialogUtils.dp2px(context, cornerArrayDP[2]).toFloat(),
            MXDialogUtils.dp2px(context, cornerArrayDP[3]).toFloat(),
            MXDialogUtils.dp2px(context, cornerArrayDP[3]).toFloat()
        )
        drawable.setColor(color)
        return drawable
    }
}