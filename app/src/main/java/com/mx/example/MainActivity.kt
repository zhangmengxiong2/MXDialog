package com.mx.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mx.dialog.MXDialog
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.tip.MXDialogType
import com.mx.dialog.upgrade.MXUpgradeDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showTip(view: View) {
        startActivity(Intent(this, TipDialogActivity::class.java))
    }

    fun showConfirm(view: View) {
        MXDialog.confirm(this, "请确认") { confirm ->
            Toast.makeText(this@MainActivity, "操作回调 confirm=$confirm", Toast.LENGTH_SHORT).show()
        }
    }

    fun showError(view: View) {
        MXDialog.tip(this, "错误提示", "错误", dialogType = MXDialogType.ERROR)
    }

    fun showSuccess(view: View) {
        MXDialog.tip(this, "成功提示", "成功", dialogType = MXDialogType.SUCCESS)
    }

    fun showWarn(view: View) {
        MXDialog.tip(this, "Warn提示", "提示", dialogType = MXDialogType.WARN)
    }

    fun showLoading(view: View) {
        MXUpgradeDialog(this).apply {
            setCancelable(false)
            setCardPosition(MXDialogPosition.CENTER.also {
//                it.translationX = 50
//                it.translationY = -100
            })
            setOnCancelListener {
                Toast.makeText(this@MainActivity, "退出回调", Toast.LENGTH_SHORT).show()
            }
            setDismissDelay(30)
//            setIndeterminateDrawable(resources.getDrawable(com.mx.dialog.R.drawable.mx_dialog_icon_error))
//            setMessage("我在加载中... ${MXProgressDialog.REPLACE_PROGRESS}")
        }.show()
    }

    fun showToast(view: View) {
        Toast.makeText(this, "asd", Toast.LENGTH_SHORT).show()
    }

    fun showSelect(view: View) {
        MXDialog.select(
            this,
            list = ('A'..'Z').toMutableList().map { it.toString() },
            selectIndex = 0,
//            cancelable = false
        ) { index ->
            MXDialog.confirm(this@MainActivity, "点击了：$index")
        }
    }
}