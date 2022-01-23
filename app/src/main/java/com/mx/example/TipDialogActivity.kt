package com.mx.example

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.tip.MXDialogType
import com.mx.dialog.tip.MXTipDialog
import com.mx.dialog.utils.MXButtonType
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
        tipDialog.setCardPosition(position)
        tipDialog.setButtonType(MXButtonType.Rounded)
        tipDialog.setOnCancelListener {
            Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show()
        }
        titleEdt.addTextChangedListener {
            tipDialog.setTitle(it)
        }
        infoEdt.addTextChangedListener {
            tipDialog.setMessage(it)
        }
        widthRatioEdt.addTextChangedListener {
            tipDialog.setMaxContentRatio(it?.toString()?.toFloatOrNull() ?: 0f)
        }
        tipDialog.setMaxContentRatio(0.5f)

        radioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tipDialog.setCardBackgroundRadius(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

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
                tipDialog.setDialogBackGroundColor(Color.parseColor(colors[index]))
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
        marginEdt.addTextChangedListener {
            tipDialog.setCardMargin(it?.toString()?.toFloatOrNull() ?: 0f)
        }
        tipDialog.setActionBtn(visible = true) {
            Toast.makeText(this, "点击确认", Toast.LENGTH_SHORT).show()
        }
        tipDialog.setCancelBtn(visible = true) {
            Toast.makeText(this, "点击取消", Toast.LENGTH_SHORT).show()
        }
    }
}