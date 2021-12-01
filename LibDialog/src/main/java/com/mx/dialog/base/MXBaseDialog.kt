package com.mx.dialog.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
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
open class MXBaseDialog(context: Context) : Dialog(context, R.style.MXDialog_FullScreen) {
    private val dialogDelay = MXDialogDelay()
    private var onCancelListener: DialogInterface.OnCancelListener? = null

    private var isDialogCancelable = true
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
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

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        onCancelListener = listener
    }

    override fun setCancelable(cancelable: Boolean) {
        isDialogCancelable = cancelable
        super.setCancelable(cancelable)
    }

    fun isCancelable() = isDialogCancelable
    override fun onBackPressed() {
        if (isDialogCancelable) {
            dismiss()
            dispatchOnCancelListener()
        }
    }

    override fun dismiss() {
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

    fun dispatchOnCancelListener() {
        onCancelListener?.onCancel(this)
        onCancelListener = null
    }
}