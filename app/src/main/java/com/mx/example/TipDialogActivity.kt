package com.mx.example

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.tip.MXDialogType
import com.mx.dialog.tip.MXTipDialog
import kotlinx.android.synthetic.main.activity_tip_dialog.*

class TipDialogActivity : AppCompatActivity() {
    private val tipDialog by lazy { MXTipDialog(this) }
    private val position = MXDialogPosition()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_dialog)
        initView()
    }

    private fun initView() {
        tipDialog.setPosition(position)
        titleEdt.addTextChangedListener {
            tipDialog.setTitle(it)
        }
        infoEdt.addTextChangedListener {
            tipDialog.setMessage(it)
        }


        positionGroup.children.forEachIndexed { index, view ->
            val gravitys = arrayOf(Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM)
            view.setOnClickListener {
                position.gravity = gravitys[index]
                tipDialog.show()
            }
        }
        backgroundGroup.children.forEachIndexed { index, view ->
            val colors = arrayOf("#33000000", "#66000000", "#99000000")
            view.setOnClickListener {
                tipDialog.setBackGroundColor(Color.parseColor(colors[index]))
                tipDialog.show()
            }
        }
        iconGroup.children.forEachIndexed { index, view ->
            val types = arrayOf(
                MXDialogType.NONE,
                MXDialogType.SUCCESS,
                MXDialogType.WARN,
                MXDialogType.ERROR
            )
            view.setOnClickListener {
                tipDialog.setTipType(types[index])
                tipDialog.show()
            }
        }
        marginTopEdt.addTextChangedListener {
            position.marginTop = it?.toString()?.toIntOrNull() ?: 0
        }
        marginBottomEdt.addTextChangedListener {
            position.marginBottom = it?.toString()?.toIntOrNull() ?: 0
        }
        tipDialog.setActionBtn(true)
        tipDialog.setCancelBtn(true)
    }
}