package com.mx.dialog.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.mx.dialog.R
import com.mx.dialog.utils.MXDialogDelay

/**
 * 所有Dialog的基类
 * 1：所有弹窗设置成全屏弹窗
 * 2：处理顶部、底部状态栏透明
 * 3：处理软键盘自动收起
 * 4：统一OnCancel监听、统一返回按键处理
 * 5：设置弹窗延时消失
 */
open class MXBaseDialog(context: Context, private val fullScreen: Boolean = false) :
    Dialog(context, if (fullScreen) R.style.MXDialog_FullScreen else R.style.MXDialog_Base) {
    fun isFullScreen() = fullScreen

    private val dialogDelay = MXDialogDelay()

    // 弹窗消失回调
    private var onDismissListener: (() -> Unit)? = null

    // 返回功能回调
    private var onCancelListener: (() -> Unit)? = null

    // 当前Dialog是否可以手动取消
    private var isDialogCancelable = true

    // 当前Dialog在空白区域点击时是否会消失
    private var isDismissOnTouchOutside = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.let { window ->
            window.decorView.setPadding(0, 0, 0, 0)
            window.attributes.let { lp ->
                lp.y = 0
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.MATCH_PARENT
                window.attributes = lp
            }

            window.setWindowAnimations(R.style.mx_dialog_animation)
        }
        dialogDelay.setTicketCall { finish, maxSecond, remindSecond ->
            if (finish) {
                onDismissTicketEnd()
            } else {
                onDismissTicket(maxSecond, remindSecond)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialogDelay.start()
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        onDismissListener = { listener?.onDismiss(this) }
    }

    fun setOnDismissListener(listener: (() -> Unit)?) {
        onDismissListener = listener
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        onCancelListener = { listener?.onCancel(this) }
    }

    fun setOnCancelListener(listener: (() -> Unit)?) {
        onCancelListener = listener
    }

    override fun setCancelable(cancelable: Boolean) {
        isDialogCancelable = cancelable
        super.setCancelable(cancelable)
    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        isDismissOnTouchOutside = cancel
        super.setCanceledOnTouchOutside(cancel)
    }

    fun isCanceledOnTouchOutside() = isDismissOnTouchOutside
    fun isCancelable() = isDialogCancelable

    override fun onBackPressed() {
        if (isCancelable()) {
            dismiss()
            onCancelListener?.invoke()
        }
    }

    override fun dismiss() {
        if (isShowing) {
            onDismissListener?.invoke()
        }
        hideSoftInput()
        dialogDelay.stop()
        super.dismiss()
    }

    open fun setDismissDelay(delay: Int?) {
        dialogDelay.setDelayTime(delay ?: -1)
    }

    private fun hideSoftInput() {
        try {
            // 解决键盘弹出的问题
            val focus = currentFocus ?: return
            if (focus is EditText) {
                val imm = context.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                imm.hideSoftInputFromWindow(focus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (ignored: Exception) {
        }
    }

    /**
     * 消息倒计时
     * @param maxSecond 总时长
     * @param remindSecond 剩余时长
     */
    open fun onDismissTicket(maxSecond: Int, remindSecond: Int) {
    }

    /**
     * 消失倒计时完成
     */
    open fun onDismissTicketEnd() {
        dismiss()
    }
}