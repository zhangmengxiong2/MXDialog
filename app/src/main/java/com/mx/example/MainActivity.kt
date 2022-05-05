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
            setCancelable(false)
            setMessage(Html.fromHtml("1:xxx<br />2:xxx<br />3:xxx<br />4:xxx"))
            setIUpgrade(
                "https://5a694755beae180ed219fdf5d2238691.rdt.tfogc.com:49156/dldir1.qq.com/weixin/android/weixin8022android2140_arm64.apk?mkey=6273e8db6676c7899fedb5fcebc4779b&arrive_key=302432739767&cip=175.10.24.12&proto=https",
                imxUpgrade
            )

//            setIndeterminateDrawable(resources.getDrawable(com.mx.dialog.R.drawable.mx_dialog_icon_error))
//            setMessage("我在加载中... ${MXProgressDialog.REPLACE_PROGRESS}")
        }.show()
    }

    private val imxUpgrade = object : IMXUpgrade {
        override fun download(dialog: Dialog, url: String, progress: (Int) -> Unit): File? {
            val file = File(dialog.context.cacheDir, "aaa.apk")
            file.createNewFile()
            val result = HttpDataHelp.download(url, file, progress)
            return if (result == true && file.exists()) file else null
        }

        override fun install(context: Context, file: File) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val type = "application/vnd.android.package-archive"
            if (Build.VERSION.SDK_INT >= 24) {
                val uri = FileProvider.getUriForFile(
                    this@MainActivity,
                    "${this@MainActivity.packageName}.fileProvider",
                    file
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(uri, type)
            } else {
                intent.setDataAndType(Uri.fromFile(file), type)
            }
            startActivity(intent)
        }
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