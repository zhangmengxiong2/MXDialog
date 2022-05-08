package com.mx.dialog

import android.content.Context
import android.view.Gravity
import com.mx.dialog.list.MXListDialog
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.tip.MXDialogType
import com.mx.dialog.tip.MXTipDialog
import com.mx.dialog.utils.MXUtils

object MXDialog {
    fun setDebug(debug: Boolean) {
        MXUtils.setDebug(debug)
    }

    /**
     * 需要用户确认的操作，无法返回且只能点击确认/取消
     * @param message 消息内容
     * @param title 标题 默认=“温馨提示”
     * @param actionButtonText 确认按钮文字
     * @param cancelButtonText 取消按钮文字
     * @param onActionClick 操作点击回调
     */
    fun confirm(
        context: Context,
        message: CharSequence,
        title: CharSequence? = null,
        actionButtonText: CharSequence? = null,
        cancelButtonText: CharSequence? = null,
        cancelable: Boolean = true,
        onActionClick: ((confirm: Boolean) -> Unit)? = null
    ) {
        val dialog = MXTipDialog(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setCancelable(cancelable)
        dialog.setActionBtn(text = actionButtonText) { onActionClick?.invoke(true) }
        if (cancelable) {
            dialog.setCancelBtn(text = cancelButtonText) {
                onActionClick?.invoke(false)
            }
            dialog.setOnCancelListener {
                onActionClick?.invoke(false)
            }
        }
        dialog.setTipType(MXDialogType.WARN)
        dialog.show()
    }

    /**
     * @param message 内容
     * @param title 标题
     * @param actionButtonText 活动按钮文字
     * @param cancelButtonText 取消按钮文字
     * @param dismissDelay x秒后弹窗消失
     * @param cancelable 是否响应返回按钮、是否点击空白处消失
     * @param dialogType Icon类型
     * @param onActionClick 按钮点击响应
     */
    fun tip(
        context: Context, message: CharSequence, title: CharSequence? = null,
        actionButtonText: CharSequence? = null,
        onActionClick: ((confirm: Boolean) -> Unit)? = null,
        cancelable: Boolean = true,
        cancelButtonText: CharSequence? = null,
        onCancelListener: (() -> Unit)? = null,
        dismissDelay: Int? = null, dialogType: MXDialogType? = null
    ) {
        val dialog = MXTipDialog(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setCancelable(cancelable)
        dialog.setDismissDelay(dismissDelay)

        if (cancelable) {
            dialog.setCancelBtn(
                visible = cancelButtonText != null,
                text = cancelButtonText
            ) { onActionClick?.invoke(false) }

            dialog.setOnCancelListener(onCancelListener)
        }

        dialog.setActionBtn(
            text = actionButtonText
                ?: context.resources.getString(R.string.mx_dialog_button_action_text)
        ) { onActionClick?.invoke(true) }
        dialog.setTipType(dialogType)

        dialog.show()
    }

    fun select(
        context: Context,
        list: List<String>,
        selectIndex: Int? = null,
        title: CharSequence? = null,
        cancelable: Boolean = true,
        contentMaxHeightRatio: Float = 1f,
        contentRadiusDP: Float = 10f,
        contentMarginDP: Float = 20f,
        position: MXDialogPosition = MXDialogPosition.BOTTOM,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = Gravity.LEFT or Gravity.CENTER_VERTICAL,
        select: ((index: Int) -> Unit)
    ) {
        MXListDialog(context).apply {
            setTitle(title)
            setContentMaxHeightRatio(contentMaxHeightRatio)
            setContentCornerRadius(contentRadiusDP)
            setContentPosition(position)
            setContentMargin(contentMarginDP)
            setCancelable(cancelable)
            setItems(
                list,
                selectIndex = selectIndex,
                textColor = textColor,
                textSizeSP = textSizeSP,
                textGravity = textGravity,
                onSelect = select
            )
        }.show()
    }

    fun selectMulti(
        context: Context,
        list: List<String>,
        selectIndexList: List<Int>? = null,
        title: CharSequence? = null,
        cancelable: Boolean = true,
        contentMaxHeightRatio: Float = 1.2f,
        contentRadiusDP: Float = 10f,
        contentMarginDP: Float = 20f,
        position: MXDialogPosition = MXDialogPosition.BOTTOM,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = Gravity.LEFT or Gravity.CENTER_VERTICAL,
        select: ((list: List<Int>) -> Unit)
    ) {
        MXListDialog(context).apply {
            setTitle(title)
            setContentMaxHeightRatio(contentMaxHeightRatio)
            setContentCornerRadius(contentRadiusDP)
            setContentPosition(position)
            setContentMargin(contentMarginDP)
            setCancelable(cancelable)
            setMultipleItems(
                list,
                selectIndexList = selectIndexList,
                textColor = textColor,
                textSizeSP = textSizeSP,
                textGravity = textGravity,
                onSelect = select
            )
        }.show()
    }

}