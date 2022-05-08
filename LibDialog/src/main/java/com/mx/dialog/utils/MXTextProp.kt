package com.mx.dialog.utils

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView

internal data class MXTextProp(
    var text: CharSequence? = null,
    var visible: Boolean = true,
    var textColor: Int? = null,
    var textSizeSP: Float? = null,
    var textHeightDP: Float? = null,
    var textStyle: Int? = null,
    var textGravity: Int? = null,
    var onclick: (() -> Unit)? = null
) {
    fun attachTextColor(textView: TextView?, defaultRes: Int) {
        val view = textView ?: return
        val context = view.context ?: return
        val color = textColor ?: context.resources.getColor(defaultRes)
        view.setTextColor(color)
    }

    fun attachTextStyle(textView: TextView?, defaultType: Int) {
        val view = textView ?: return
        val style = textStyle ?: defaultType
        view.setTypeface(Typeface.DEFAULT, style)
    }

    fun attachTextSize(textView: TextView?, defaultRes: Int) {
        val view = textView ?: return
        val context = view.context ?: return
        val textSizeSP = textSizeSP
        if (textSizeSP != null) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSP)
        } else {
            view.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(defaultRes)
            )
        }
    }

    fun attachTextHeight(textView: TextView?, defaultRes: Int) {
        val view = textView ?: return
        val context = view.context ?: return
        view.minHeight = textHeightDP?.let {
            MXUtils.dp2px(context, it)
        } ?: context.resources.getDimensionPixelOffset(defaultRes)
    }

    fun attachTextGravity(textView: TextView?, defaultGravity: Int) {
        val view = textView ?: return
        view.gravity = textGravity ?: defaultGravity
    }
}