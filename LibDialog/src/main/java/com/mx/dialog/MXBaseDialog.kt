package com.mx.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager

open class MXBaseDialog(context: Context) : Dialog(context, R.style.MXDialog_FullScreen) {
    private var onCancelListener: DialogInterface.OnCancelListener? = null

    private var isDialogCancelable = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.let { window ->
            window.setWindowAnimations(R.style.mx_dialog_animation)
            window.decorView.setPadding(0, 0, 0, 0)
            window.attributes.let { lp ->
                lp.y = 0
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.MATCH_PARENT
                window.attributes = lp
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.decorView.systemUiVisibility =
                    (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
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

    fun dispatchOnCancelListener() {
        onCancelListener?.onCancel(this)
        onCancelListener = null
    }
}