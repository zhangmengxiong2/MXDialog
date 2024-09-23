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
import com.mx.dialog.utils.MXButtonStyle
import com.mx.example.databinding.ActivityTipDialogBinding

class TipDialogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTipDialogBinding.inflate(layoutInflater) }
    private val tipDialog by lazy { MXTipDialog(this) }
    private val position = MXDialogPosition()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        tipDialog.setCardPosition(position)
        tipDialog.setButtonStyle(MXButtonStyle.ActionFocus)
        tipDialog.setOnCancelListener {
            Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show()
        }
        binding.titleEdt.addTextChangedListener {
            tipDialog.setTitle(it)
        }
        binding.infoEdt.addTextChangedListener {
            tipDialog.setMessage(it)
        }
        binding.widthRatioEdt.addTextChangedListener {
            tipDialog.setMaxContentRatio(it?.toString()?.toFloatOrNull() ?: 0f)
        }
        tipDialog.setMaxContentRatio(0.6f)

        binding.radioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tipDialog.setCardBackgroundRadius(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        binding.positionGroup.children.forEachIndexed { index, view ->
            val gravitys = arrayOf(Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM)
            view.setOnClickListener {
                position.gravity = gravitys[index]
                tipDialog.show()
            }
        }
        binding.backgroundGroup.children.forEachIndexed { index, view ->
            val colors = arrayOf("#33000000", "#66000000", "#99000000")
            view.setOnClickListener {
                tipDialog.setDialogBackGroundColor(Color.parseColor(colors[index]))
                tipDialog.show()
            }
        }
        binding.iconGroup.children.forEachIndexed { index, view ->
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
        binding.actionGroup.children.forEachIndexed { index, view ->
            val types = arrayOf(
                MXButtonStyle.Rounded,
                MXButtonStyle.FillBackground,
                MXButtonStyle.ActionFocus
            )
            view.setOnClickListener {
                tipDialog.setButtonStyle(types[index])
                tipDialog.show()
            }
        }
        binding.marginEdt.addTextChangedListener {
            tipDialog.setCardMargin(it?.toString()?.toFloatOrNull() ?: 0f)
        }
        tipDialog.addActionBtn {
            Toast.makeText(this, "点击确认", Toast.LENGTH_SHORT).show()
        }
//        tipDialog.setAction2Btn("Action2") {
//            Toast.makeText(this, "点击确认2", Toast.LENGTH_SHORT).show()
//        }
        tipDialog.setCancelBtn {
            Toast.makeText(this, "点击取消", Toast.LENGTH_SHORT).show()
        }
    }
}