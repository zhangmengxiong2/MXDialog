package com.mx.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager

open class BaseDialog(context: Context) : Dialog(context, R.style.MXDialog_FullScreen) {
    private var onCancelListener: DialogInterface.OnCancelListener? = null

    private var isDialogCancelable = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.let { window ->
            window.decorView.setPadding(0, 0, 0, 0)
            window.decorView.systemUiVisibility = View.GONE
            window.attributes.let { lp ->
                lp.y = 0
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.MATCH_PARENT
                window.attributes = lp
            }
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        onCancelListener = listener
    }

    override fun setCancelable(flag: Boolean) {
        isDialogCancelable = flag
        super.setCancelable(flag)
    }

    fun isCancelable() = isDialogCancelable
    override fun onBackPressed() {
        if (isDialogCancelable) {
            dismiss()
            dispatchOnCancelListener()
        }
    }

    fun dispatchOnCancelListener() {
        onCancelListener?.onCancel(this)
        onCancelListener = null
    }
}