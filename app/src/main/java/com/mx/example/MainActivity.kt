package com.mx.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mx.dialog.tip.MXTipDialog
import com.mx.dialog.tip.MXTipGravity
import com.mx.dialog.tip.MXTipType
import com.mx.dialog.toast.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun showTip(view: View) {
        MXTipDialog(this).apply {
//            setCancelable(false)
            setTitle("提示")
            setMessage("这是一个提示！！！这是一个提示！！！这是一个提示！！！这是一个提示！！！这是一个提示！！！这是一个提示！！！这是一个提示！！！这是一个提示！！！")
            setDismissDelay(2)
//            setGravity(MXTipGravity.BOTTOM)
//            setTipType(MXTipType.SUCCESS)
//            setOnCancelListener {
//                toast("onCancelListener")
//            }
            setInActiveBtn(text = "不要") {
                toast("InActiveBtn Click")
            }
            setActiveBtn {
                toast("ActiveBtn Click")
            }
        }.show()
    }

    fun showToast(view: View) {
        toast("提示！！！！！！！")
    }
}