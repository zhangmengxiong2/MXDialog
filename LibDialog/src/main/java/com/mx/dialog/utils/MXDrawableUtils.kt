package com.mx.dialog.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable

internal object MXDrawableUtils {
    /**
     * 构建圆角背景图
     */
    fun buildGradientDrawable(context: Context, colorRes: Int, cornerDP: Float): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = MXUtils.dp2px(context, cornerDP).toFloat()
        val color = context.resources.getColor(colorRes)
        drawable.setColor(color)
        return drawable
    }

    /**
     * 构建圆角背景图
     */
    fun buildGradientDrawable(
        context: Context,
        colorRes: Int,
        cornerArrayDP: FloatArray,
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(
            MXUtils.dp2px(context, cornerArrayDP[0]).toFloat(),
            MXUtils.dp2px(context, cornerArrayDP[0]).toFloat(),
            MXUtils.dp2px(context, cornerArrayDP[1]).toFloat(),
            MXUtils.dp2px(context, cornerArrayDP[1]).toFloat(),
            MXUtils.dp2px(context, cornerArrayDP[2]).toFloat(),
            MXUtils.dp2px(context, cornerArrayDP[2]).toFloat(),
            MXUtils.dp2px(context, cornerArrayDP[3]).toFloat(),
            MXUtils.dp2px(context, cornerArrayDP[3]).toFloat()
        )
        val color = context.resources.getColor(colorRes)
        drawable.setColor(color)
        return drawable
    }
}