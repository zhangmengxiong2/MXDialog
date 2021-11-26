package com.mx.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mx.dialog.progress.MXLoadingDialog
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.tip.MXTipDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showTip(view: View) {
        startActivity(Intent(this, TipDialogActivity::class.java))
    }

    fun showConfirm(view: View) {
        MXTipDialog.confirm(this, "请确认") { confirm ->
        }
    }

    fun showError(view: View) {
        MXTipDialog.error(this, "错误提示")
    }

    fun showSuccess(view: View) {
        MXTipDialog.success(this, "成功提示")
    }

    fun showWarn(view: View) {
        MXTipDialog.warn(this, "Warn提示")
    }

    fun showLoading(view: View) {
        MXLoadingDialog(this).apply {
            setCancelable(false)
            setPosition(MXDialogPosition.CENTER.also {
//                it.translationX = 50
//                it.translationY = -100
            })
            setDismissDelay(3)
//            setIndeterminateDrawable(resources.getDrawable(com.mx.dialog.R.drawable.mx_dialog_icon_error))
            setMessage("我在加载中...")
        }.show()
    }

    fun showToast(view: View) {
    }
}