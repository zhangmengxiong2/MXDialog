package com.mx.dialog.tip

import android.view.Gravity

/**
 * position of dialog content
 */
class MXDialogPosition {
    var gravity = Gravity.CENTER

    var marginTop: Int? = null // 容器顶部边距 dp
    var marginBottom: Int? = null // 容器底部边距 dp

    var translationX: Int? = null //X轴偏移 dp
    var translationY: Int? = null //Y轴偏移 dp

    companion object {
        val TOP = MXDialogPosition().apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }
        val CENTER = MXDialogPosition().apply {
            gravity = Gravity.CENTER
        }
        val BOTTOM = MXDialogPosition().apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        }
    }
}

/**
 * type of dialog icon
 */
enum class MXDialogType {
    SUCCESS,
    WARN,
    ERROR,
    NONE
}