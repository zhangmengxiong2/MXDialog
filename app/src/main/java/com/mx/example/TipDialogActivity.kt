package com.mx.example

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.mx.dialog.tip.MXBtnStyle
import com.mx.dialog.tip.MXPosition
import com.mx.dialog.tip.MXCardPosition
import com.mx.dialog.tip.MXType
import com.mx.dialog.tip.MXTipDialog
import com.mx.example.databinding.ActivityTipDialogBinding

class TipDialogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTipDialogBinding.inflate(layoutInflater) }
    private val tipDialog by lazy { MXTipDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        tipDialog.setButtonStyle(MXBtnStyle.ActionFocus)
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
            val gravitys = arrayOf(
                MXCardPosition.TOP, MXCardPosition.CENTER, MXCardPosition.BOTTOM
            )
            view.setOnClickListener {
                tipDialog.setCardPosition(gravitys[index])
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
                MXType.NONE,
                MXType.SUCCESS,
                MXType.WARN,
                MXType.ERROR
            )
            view.setOnClickListener {
                tipDialog.setTipType(types[index])
                tipDialog.show()
            }
        }
        binding.actionGroup.children.forEachIndexed { index, view ->
            val types = arrayOf(
                MXBtnStyle.Rounded,
                MXBtnStyle.FillBackground,
                MXBtnStyle.ActionFocus
            )
            view.setOnClickListener {
                tipDialog.setButtonStyle(types[index])
                tipDialog.show()
            }
        }
        binding.cancelPositionGroup.children.forEachIndexed { index, view ->
            val types = arrayOf(
                MXPosition.LEFT,
                MXPosition.RIGHT
            )
            view.setOnClickListener {
                tipDialog.setCancelPosition(types[index])
                tipDialog.show()
            }
        }
        binding.marginEdt.addTextChangedListener {
            tipDialog.setCardMargin(it?.toString()?.toFloatOrNull() ?: 0f)
        }
        tipDialog.setActionBtn {
            Toast.makeText(this, "点击确认", Toast.LENGTH_SHORT).show()
        }
//        tipDialog.addActionBtn("Action2") {
//            Toast.makeText(this, "点击确认2", Toast.LENGTH_SHORT).show()
//        }
        tipDialog.setCancelBtn {
            Toast.makeText(this, "点击取消", Toast.LENGTH_SHORT).show()
        }
    }
}