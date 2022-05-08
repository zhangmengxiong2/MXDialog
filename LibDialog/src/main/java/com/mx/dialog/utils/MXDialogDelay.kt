package com.mx.dialog.utils

import android.os.Handler
import android.os.Looper

internal class MXDialogDelay {
    private val mHandler = Handler(Looper.getMainLooper())
    private var isActive = false
    private var ticketTime = 0
    private var ticketLeft = 0
    private var ticketCall: ((Boolean, Int, Int) -> Unit)? = null

    fun setTicketCall(call: ((finish: Boolean, maxSecond: Int, remindSecond: Int) -> Unit)?) {
        ticketCall = call
    }

    fun setDelayTime(delay: Int) {
        if (isActive) {
            stop()
            ticketTime = delay
            ticketLeft = delay
            if (delay > 0) {
                start()
            }
        } else {
            ticketTime = delay
            ticketLeft = delay
        }
    }

    fun start() {
        isActive = true
        mHandler.removeCallbacksAndMessages(null)
        if (ticketTime > 0) {
            mHandler.post(ticketRun)
        }
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
                    MXUtils.log("倒计时：$next")
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