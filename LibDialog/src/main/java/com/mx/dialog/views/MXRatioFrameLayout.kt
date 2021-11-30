package com.mx.dialog.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * 可以设置最大宽高比的FrameLayout
 * 当高度超过设置的宽高比时，高度会固定成对应的高度
 */
internal class MXRatioFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var mMaxHeightRatio: Float = 0f

    fun setMaxHeightRatio(height: Float) {
        mMaxHeightRatio = height
        postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val maxHeight = widthSize * mMaxHeightRatio
        if (maxHeight > 0) {
            heightSize = if (heightSize <= maxHeight) heightSize else maxHeight.toInt()
        }

        val maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            heightSize,
            heightMode
        )
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec)
    }
}