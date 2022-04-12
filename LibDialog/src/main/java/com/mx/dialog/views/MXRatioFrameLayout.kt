package com.mx.dialog.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.min

/**
 * 可以设置最大宽高比的FrameLayout
 * 当高度超过设置的宽高比时，高度会固定成对应的高度
 */
class MXRatioFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var mMaxHeightRatio: Float = 0f
    private var mMaxHeight: Int = 0

    fun setMaxHeightRatio(ratio: Float) {
        mMaxHeightRatio = ratio
        requestLayout()
    }

    fun setMaxHeight(maxHeight: Int) {
        mMaxHeight = maxHeight
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mMaxHeightRatio <= 0f && mMaxHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        if (mMaxHeightRatio > 0) {
            heightSize = min((widthSize * mMaxHeightRatio).toInt(), heightSize)
        }
        if (mMaxHeight > 0) {
            heightSize = min(mMaxHeight, heightSize)
        }

        val maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            heightSize,
            heightMode
        )
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec)
    }
}