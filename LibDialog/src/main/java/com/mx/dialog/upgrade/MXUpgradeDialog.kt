package com.mx.dialog.upgrade

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseCardDialog

open class MXUpgradeDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseCardDialog(context, fullScreen) {
    private var titleTxv: TextView? = null
    private var msgTxv: TextView? = null
    private var actionBtn: TextView? = null

    private var titleStr: CharSequence? = null
    private var msgStr: CharSequence? = null

    override fun getContentLayoutId(): Int {
        return R.layout.mx_dialog_upgrade
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCardMargin(40f)
        titleTxv = findViewById(R.id.titleTxv)
        msgTxv = findViewById(R.id.msgTxv)
        actionBtn = findViewById(R.id.actionBtn)
    }

    override fun initDialog() {
        super.initDialog()
        titleTxv?.text = titleStr
        msgTxv?.text = msgStr
    }


    override fun setTitle(title: CharSequence?) {
        titleStr = title
        initDialog()
    }

    override fun setTitle(titleId: Int) {
        titleStr = context.getString(titleId)
        initDialog()
    }

    fun setMessage(message: CharSequence?) {
        msgStr = message
        initDialog()
    }

    open fun start() {}
}