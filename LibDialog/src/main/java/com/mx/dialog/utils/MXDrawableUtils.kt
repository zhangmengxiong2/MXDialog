package com.mx.dialog.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable

internal object MXDrawableUtils {
    /**
     * 构建圆角背景图
     */
    fun buildGradientDrawable(context: Context, cornerDP: Float, color: Int): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = MXDialogUtils.dp2px(context, cornerDP).toFloat()
        drawable.setColor(color)
        return drawable
    }
}