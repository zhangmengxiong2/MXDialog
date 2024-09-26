package com.mx.dialog.utils

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.tip.MXBtnStyle

internal object MXBtnAttach {
    fun attach(style: MXBtnStyle, content: LinearLayout, cornerDP: Float) {
        val btns = (0 until content.childCount).mapNotNull {
            content.getChildAt(it) as TextView?
        }.toMutableList()
        attachDivider(style, content)
        val cancelBtn = btns.firstOrNull { it.getTag(R.id.mxCancelBtn) == true }
        attachCancel(style, content, cancelBtn, cornerDP)

        btns.forEachIndexed { index, view ->
            if (view != cancelBtn) {
                attachAction(style, content, view, cornerDP)
            }
        }

        content.background = MXDrawableUtils.buildGradientDrawable(
            content.context, R.color.mx_dialog_color_background_button,
            floatArrayOf(0f, 0f, cornerDP, cornerDP)
        )
    }

    private fun attachDivider(style: MXBtnStyle, content: LinearLayout) {
        val context = content.context ?: return
        var index = (content.childCount - 1)
        while (index > 0) {
            val divider = LayoutInflater.from(context).inflate(
                R.layout.mx_content_divider_btn, content, false
            )
            content.addView(divider, index)
            when (style) {
                MXBtnStyle.Rounded -> {
                    val padding = context.resources.getDimensionPixelOffset(
                        R.dimen.mx_dialog_size_divider_normal
                    )
                    val lp = (divider.layoutParams as LinearLayout.LayoutParams?)
                    lp?.leftMargin = padding / 2
                    lp?.rightMargin = padding / 2
                    divider.layoutParams = lp
                    divider.visibility = View.INVISIBLE
                }

                MXBtnStyle.FillBackground, MXBtnStyle.ActionFocus -> {
                    val lp = (divider.layoutParams as LinearLayout.LayoutParams?)
                    lp?.leftMargin = 0
                    lp?.rightMargin = 0
                    divider.layoutParams = lp
                    divider.visibility = View.VISIBLE
                }
            }
            index--
        }
    }

    private fun attachCancel(
        style: MXBtnStyle, content: LinearLayout, cancelBtn: TextView?, cornerDP: Float
    ) {
        val context = content.context ?: return
        val index = content.indexOfChild(cancelBtn ?: return)
        val isFirstView = (index == 0)
        val isLastView = (index == content.childCount - 1)

        when (style) {
            MXBtnStyle.Rounded -> {
                cancelBtn.setBackgroundResource(R.drawable.mx_dialog_btn_bg_cancel_circular)
            }

            MXBtnStyle.FillBackground -> {
                cancelBtn.background = MXDrawableUtils.buildGradientDrawable(
                    context, R.color.mx_dialog_color_transparent,
                    floatArrayOf(
                        0f, 0f,
                        if (isFirstView) cornerDP else 0f,
                        if (isLastView) cornerDP else 0f
                    )
                )
            }

            MXBtnStyle.ActionFocus -> {
                cancelBtn.background = MXDrawableUtils.buildGradientDrawable(
                    context, R.color.mx_dialog_color_transparent,
                    floatArrayOf(0f, 0f, 0f, cornerDP)
                )
            }
        }
    }

    private fun attachAction(
        style: MXBtnStyle, content: LinearLayout,
        actionBtn: TextView, cornerDP: Float
    ) {
        val context = content.context ?: return

        val index = content.indexOfChild(actionBtn ?: return)
        val isFirstView = (index == 0)
        val isLastView = (index == content.childCount - 1)
        when (style) {
            MXBtnStyle.Rounded -> {
                actionBtn.setBackgroundResource(R.drawable.mx_dialog_btn_bg_action_circular)

                val lp1 = (content.layoutParams as LinearLayout.LayoutParams)
                lp1.height = MXUtils.dp2px(context, 65f)
                content.layoutParams = lp1
                val padding = context.resources.getDimensionPixelOffset(
                    R.dimen.mx_dialog_size_divider_normal
                )
                content.setPadding(padding, padding, padding, padding)
            }

            MXBtnStyle.FillBackground -> {
                actionBtn.background = MXDrawableUtils.buildGradientDrawable(
                    context, R.color.mx_dialog_color_action,
                    floatArrayOf(
                        0f, 0f,
                        if (isLastView) cornerDP else 0f,
                        if (isFirstView) cornerDP else 0f
                    )
                )

                val lp1 = (content.layoutParams as LinearLayout.LayoutParams)
                lp1.height = MXUtils.dp2px(context, 50f)
                content.layoutParams = lp1
                content.setPadding(0, 0, 0, 0)
            }

            MXBtnStyle.ActionFocus -> {
                actionBtn.background = MXDrawableUtils.buildGradientDrawable(
                    context, R.color.mx_dialog_color_transparent,
                    floatArrayOf(0f, 0f, cornerDP, cornerDP)
                )
                actionBtn.setTextColor(ColorStateList.valueOf(content.resources.getColor(R.color.mx_dialog_color_action)))

                val lp1 = (content.layoutParams as LinearLayout.LayoutParams)
                lp1.height = MXUtils.dp2px(context, 50f)
                content.layoutParams = lp1
                content.setPadding(0, 0, 0, 0)
            }
        }
    }
}