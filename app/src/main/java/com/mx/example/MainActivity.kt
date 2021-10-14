package com.mx.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mx.dialog.tip.TipDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.tipDialog).setOnClickListener {
            TipDialog(this).apply {
                setTitle("提示")
                setMessage("这是一个提示！！！")
                setOnCancelListener {

                }
                setInActiveBtn {

                }
                setActiveBtn {

                }
            }.show()
        }
    }
}