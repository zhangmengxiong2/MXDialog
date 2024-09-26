package com.mx.example

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.mx.dialog.MXDialog
import com.mx.dialog.progress.MXLoadingDialog
import com.mx.dialog.tip.MXCardPosition
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    protected val immersionBar: ImmersionBar by lazy {
        ImmersionBar.with(this).autoDarkModeEnable(true)
    }

    protected fun setFullScreen() {
        immersionBar.reset()
        immersionBar.hideBar(BarHide.FLAG_HIDE_BAR)
        immersionBar.keyboardEnable(true)
        immersionBar.init()
    }

    protected fun setTransparentStatusBar(
        statusBarDarkFont: Boolean = true,
        hideNavigation: Boolean = false
    ) {
        immersionBar.reset()
        immersionBar.transparentStatusBar()
        if (hideNavigation) {
            immersionBar.hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
        } else {
            immersionBar.hideBar(BarHide.FLAG_SHOW_BAR)
        }

        immersionBar.statusBarDarkFont(statusBarDarkFont)
        immersionBar.keyboardEnable(true)
        immersionBar.navigationBarColorInt(Color.WHITE)
        immersionBar.init()
    }

    protected fun setStatusBarColor(color: Int, statusBarDarkFont: Boolean = false) {
        immersionBar.reset()
        immersionBar.statusBarColorInt(color)
        immersionBar.statusBarDarkFont(statusBarDarkFont)
        immersionBar.fitsSystemWindows(true)
        immersionBar.navigationBarColorInt(Color.WHITE)
        immersionBar.hideBar(BarHide.FLAG_SHOW_BAR)
        immersionBar.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        setTransparentStatusBar(statusBarDarkFont = false, hideNavigation = false)
        setStatusBarColor(Color.GREEN)

//        MXDialog.addLifecycle(object : IMXLifecycle {
//            override fun onCreate(context: Context, dialog: Dialog) {
//                if (context is Activity) {
//                    ImmersionBar.with(context, dialog).transparentBar().init()
//                }
//            }
//
//            override fun onDismiss(context: Context, dialog: Dialog) {
//                if (context is Activity) {
//                    ImmersionBar.destroy(context, dialog)
//                }
//            }
//        })
//        setFullScreen()
    }

    fun showTip(view: View) {
        startActivity(Intent(this, TipDialogActivity::class.java))
    }

    fun showConfirm(view: View) {
//        MXDialog.confirm(this, "请确认") { confirm ->
//            Toast.makeText(this@MainActivity, "操作回调 confirm=$confirm", Toast.LENGTH_SHORT).show()
//        }
        lifecycleScope.launch {
            val confirm = MXDialog.confirmSync(this@MainActivity, "请确认")
            MXDialog.tip(this@MainActivity, "确认：$confirm")
        }
    }

    fun showError(view: View) {
        MXDialog.error(this, "错误提示", "错误")
    }

    fun showSuccess(view: View) {
        MXDialog.success(this, "成功提示", "成功")
    }

    fun showWarn(view: View) {
        MXDialog.warn(this, "Warn提示", "提示")
    }

    fun showLoading(view: View) {
//        MXUpgradeDialog(this).apply {
//            setCancelable(true)
//            setTitle("发现更新")
//            setMessage(Html.fromHtml("1:xxx<br />2:xxx<br />3:xxx<br />1:xxx<br />2:xxx<br />3:xxx<br />1:xxx<br />2:xxx<br />3:xxx<br />4:xxx"))
//            setIUpgrade(
//                MXUpgradeImp(
//                    "https://5a694755beae180ed219fdf5d2238691.rdt.tfogc.com:49156/dldir1.qq.com/weixin/android/weixin8022android2140_arm64.apk?mkey=6273e8db6676c7899fedb5fcebc4779b&arrive_key=302432739767&cip=175.10.24.12&proto=https",
//                    this@MainActivity
//                )
//            )
//
////            setIndeterminateDrawable(resources.getDrawable(com.mx.dialog.R.drawable.mx_dialog_icon_error))
////            setMessage("我在加载中... ${MXProgressDialog.REPLACE_PROGRESS}")
//        }.show()
        MXLoadingDialog(this).apply {
            setCancelable(false) //设置是否可手动返回
            setCardPosition(MXCardPosition.CENTER.apply {
                // 和TipDialog的位置使用一样
//                translationY = -10
//                translationX = 20
            })
            setDismissDelay(3) // 设置Dialog3秒后自动消失
            setMessage("我在加载中...")
        }.show()
    }

    fun showToast(view: View) {
        Toast.makeText(this, "asd", Toast.LENGTH_SHORT).show()
    }

    fun showSelect(view: View) {
        lifecycleScope.launch {
            val index = MXDialog.selectMultiSync(
                this@MainActivity,
                list = ('A'..'Z').toMutableList().map { it.toString() },
                title = "多选！",
                selectIndexList = arrayListOf(0, 4),
                cancelable = false
            )
            MXDialog.tip(this@MainActivity, "点击了：$index")
        }
    }
}