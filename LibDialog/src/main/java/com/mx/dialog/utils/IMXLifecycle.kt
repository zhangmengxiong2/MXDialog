package com.mx.dialog.utils

import android.app.Dialog
import android.content.Context

/**
 * 插件生命周期注入方法
 */
interface IMXLifecycle {
    fun onCreate(context: Context, dialog: Dialog)
    fun onDismiss(context: Context, dialog: Dialog)
}