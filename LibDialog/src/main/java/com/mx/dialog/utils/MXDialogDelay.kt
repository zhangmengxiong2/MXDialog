package com.mx.dialog.utils

import android.os.Handler
import android.os.Looper

class MXDialogDelay {
    private val mHandler = Handler(Looper.getMainLooper())
    private var isActive = false
    private var ticketTime = 0
    private var ticketLeft = 0
    private var ticketCall: ((Boolean, Int, Int) -> Unit)? = null

    fun setTicketCall(call: ((finish: Boolean, maxSecond: Int, remindSecond: Int) -> Unit)?) {
        ticketCall = call
    }

    fun setDelayTime(delay: Int) {
        if (isActive) throw java.lang.Exception("已经开始的弹窗不能再设置计时！")

        ticketTime = delay
        ticketLeft = delay
    }

    fun start() {
        isActive = true
        mHandler.removeCallbacksAndMessages(null)
        mHandler.post(ticketRun)
    }

    fun stop() {
        ticketTime = 0
        ticketLeft = 0
        isActive = false
        mHandler.removeCallbacksAndMessages(null)
    }

    private val ticketRun = object : Runnable {
        override fun run() {
            if (!isActive) return
            try {
                if (ticketTime > 0) {
                    val next = ticketLeft--
                    if (next > 0) {
                        ticketCall?.invoke(false, ticketTime, next)
                    } else {
                        ticketCall?.invoke(true, ticketTime, 0)
                    }
                }
            } catch (e: Exception) {
            } finally {
                mHandler.postDelayed(this, 1000)
            }
        }
    }
}