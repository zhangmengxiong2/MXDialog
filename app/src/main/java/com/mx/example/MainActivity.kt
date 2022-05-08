package com.mx.example

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.mx.dialog.MXDialog
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.tip.MXDialogType
import com.mx.dialog.upgrade.IMXUpgrade
import com.mx.dialog.upgrade.MXUpgradeDialog
import java.io.File

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
            setCancelable(true)
            setMessage(Html.fromHtml("1:xxx<br />2:xxx<br />3:xxx<br />4:xxx"))
            setIUpgrade(
                MXUpgradeImp(
                    "https://5a694755beae180ed219fdf5d2238691.rdt.tfogc.com:49156/dldir1.qq.com/weixin/android/weixin8022android2140_arm64.apk?mkey=6273e8db6676c7899fedb5fcebc4779b&arrive_key=302432739767&cip=175.10.24.12&proto=https",
                    this@MainActivity
                )
            )

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