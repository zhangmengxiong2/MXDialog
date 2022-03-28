package com.mx.example

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mx.dialog.progress.MXLoadingDialog
import com.mx.dialog.list.MXListDialog
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.tip.MXDialogType
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
            Toast.makeText(this@MainActivity, "操作回调 confirm=$confirm", Toast.LENGTH_SHORT).show()
        }
    }

    fun showError(view: View) {
        MXTipDialog.show(this, "错误提示", "错误", dialogType = MXDialogType.ERROR)
    }

    fun showSuccess(view: View) {
        MXTipDialog.show(this, "成功提示", "成功", dialogType = MXDialogType.SUCCESS)
    }

    fun showWarn(view: View) {
        MXTipDialog.show(this, "Warn提示", "提示", dialogType = MXDialogType.WARN)
    }

    fun showLoading(view: View) {
        MXLoadingDialog(this).apply {
            setCancelable(false)
            setCardPosition(MXDialogPosition.CENTER.also {
//                it.translationX = 50
//                it.translationY = -100
            })
            setOnCancelListener {
                Toast.makeText(this@MainActivity, "退出回调", Toast.LENGTH_SHORT).show()
            }
            setDismissDelay(3)
//            setIndeterminateDrawable(resources.getDrawable(com.mx.dialog.R.drawable.mx_dialog_icon_error))
            setMessage("我在加载中...")
        }.show()
    }

    fun showToast(view: View) {
        Toast.makeText(this, "asd", Toast.LENGTH_SHORT).show()
    }

    fun showSelect(view: View) {
        MXListDialog(this).apply {
            setTitle("请选择")
//            setContentMaxHeightRatio(1.5f)
            setContentCornerRadius(10f)
            setContentPosition(MXDialogPosition.BOTTOM)
            setContentMargin(horizontal = 20f, vertical = 20f)
            setCancelable(false)
//            setCanceledOnTouchOutside(false)
            val list = ('A'..'Z').toMutableList().map { it.toString() }
//            setMultipleItems(
//                list,
//                selectIndexList = arrayListOf(2),
//                textColor = Color.RED,
//                textSizeSP = 12f,
//                itemHeightDP = 52f,
//                textGravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
//            ) { index ->
//                MXTipDialog.confirm(this@MainActivity, "点击了：$index")
//            }
            setItems(
                list,
                selectIndex = 2,
//                textColor = Color.RED,
//                textSizeSP = 14f,
//                textStyle = Typeface.BOLD_ITALIC,
//                itemHeightDP = 55f,
                textGravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            ) { index ->
                MXTipDialog.confirm(this@MainActivity, "点击了：$index")
            }
        }.show()
    }
}