package com.mx.dialog.utils

import android.content.res.ColorStateList
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
     * 背景填充，无边距
     */
    FillBackground,

    /**
     * 空白填充，设置焦点文字颜色
     */
    ActionFocus;

    companion object {
        fun attach(
            type: MXButtonType,
            content: LinearLayout?,
            cancelBtn: TextView?,
            actionBtn: TextView?,
            btnDivider: View?,
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

                    val lp1 = (content.layoutParams as LinearLayout.LayoutParams)
                    lp1.height = MXDialogUtils.dp2px(context, 65f)
                    content.layoutParams = lp1


                    val lp = (btnDivider?.layoutParams as LinearLayout.LayoutParams?)
                    lp?.leftMargin = padding / 2
                    lp?.rightMargin = padding / 2
                    btnDivider?.layoutParams = lp
                    btnDivider?.visibility = View.INVISIBLE
                }
                FillBackground -> {
                    val isCancelShow = (cancelBtn?.visibility == View.VISIBLE)

                    content.setPadding(0, 0, 0, 0)
                    cancelBtn?.background = MXDrawableUtils.buildGradientDrawable(
                        context, R.color.mx_dialog_color_transparent,
                        floatArrayOf(0f, 0f, 0f, cornerDP)
                    )
                    actionBtn?.background = MXDrawableUtils.buildGradientDrawable(
                        context, R.color.mx_dialog_color_action,
                        floatArrayOf(0f, 0f, cornerDP, if (isCancelShow) 0f else cornerDP)
                    )

                    val lp1 = (content.layoutParams as LinearLayout.LayoutParams)
                    lp1.height = MXDialogUtils.dp2px(context, 50f)
                    content.layoutParams = lp1

                    val lp = (btnDivider?.layoutParams as LinearLayout.LayoutParams?)
                    lp?.leftMargin = 0
                    lp?.rightMargin = 0
                    btnDivider?.layoutParams = lp
                    btnDivider?.visibility = View.VISIBLE
                }
                ActionFocus -> {
                    val isCancelShow = (cancelBtn?.visibility == View.VISIBLE)

                    content.setPadding(0, 0, 0, 0)
                    cancelBtn?.background = MXDrawableUtils.buildGradientDrawable(
                        context, R.color.mx_dialog_color_transparent,
                        floatArrayOf(0f, 0f, 0f, cornerDP)
                    )
                    actionBtn?.background = MXDrawableUtils.buildGradientDrawable(
                        context, R.color.mx_dialog_color_transparent,
                        floatArrayOf(0f, 0f, cornerDP, if (isCancelShow) 0f else cornerDP)
                    )
                    actionBtn?.setTextColor(ColorStateList.valueOf(content.resources.getColor(R.color.mx_dialog_color_action)))

                    val lp1 = (content.layoutParams as LinearLayout.LayoutParams)
                    lp1.height = MXDialogUtils.dp2px(context, 50f)
                    content.layoutParams = lp1

                    val lp = (btnDivider?.layoutParams as LinearLayout.LayoutParams?)
                    lp?.leftMargin = 0
                    lp?.rightMargin = 0
                    btnDivider?.layoutParams = lp
                    btnDivider?.visibility = View.VISIBLE
                }
            }
        }
    }
}