package com.mx.dialog.utils

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mx.dialog.R

enum class MXButtonType {
    /**
     * 圆角带边距
     */
    Rounded,

    /**
     * 填充，无边距
     */
    Normal;

    companion object {
        fun attach(
            type: MXButtonType,
            content: LinearLayout?,
            cancelBtn: TextView?,
            actionBtn: TextView?,
            cornerDP: Float
        ) {
            val context = content?.context ?: return
            when (type) {
                Rounded -> {
                    val padding =
                        context.resources.getDimensionPixelOffset(R.dimen.mx_dialog_size_divider_space)
                    content.setPadding(padding, padding, padding, padding)
                    cancelBtn?.setBackgroundResource(R.drawable.mx_dialog_btn_bg_cancel_circular)
                    actionBtn?.setBackgroundResource(R.drawable.mx_dialog_btn_bg_action_circular)

                    val lp = (cancelBtn?.layoutParams as LinearLayout.LayoutParams?)
                    lp?.rightMargin = padding
                    cancelBtn?.layoutParams = lp

                    val lp1 = (content.layoutParams as LinearLayout.LayoutParams)
                    lp1.height = MXDialogUtils.dp2px(context, 65f)
                    content.layoutParams = lp1
                }
                Normal -> {
                    val isCancelShow = (cancelBtn?.visibility == View.VISIBLE)

                    content.setPadding(0, 0, 0, 0)
                    cancelBtn?.background = MXDrawableUtils.buildGradientDrawable(
                        context, R.color.mx_dialog_color_transparent,
                        floatArrayOf(0f, 0f, 0f, cornerDP)
                    )
                    actionBtn?.background = MXDrawableUtils.buildGradientDrawable(
                        context, R.color.mx_dialog_color_focus,
                        floatArrayOf(0f, 0f, cornerDP, if (isCancelShow) 0f else cornerDP)
                    )

                    val lp = (cancelBtn?.layoutParams as LinearLayout.LayoutParams?)
                    lp?.rightMargin = 0
                    cancelBtn?.layoutParams = lp

                    val lp1 = (content.layoutParams as LinearLayout.LayoutParams)
                    lp1.height = MXDialogUtils.dp2px(context, 50f)
                    content.layoutParams = lp1
                }
            }
        }
    }
}