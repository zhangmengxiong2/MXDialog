package com.mx.dialog.tip

import android.view.Gravity

/**
 * position of dialog content
 */
class MXDialogPosition {
    var gravity = Gravity.CENTER

    var translationX: Int? = null //X轴偏移 dp
    var translationY: Int? = null //Y轴偏移 dp

    companion object {
        val TOP: MXDialogPosition
            get() = MXDialogPosition().apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                translationY = 20
            }

        val CENTER: MXDialogPosition
            get() = MXDialogPosition().apply {
                gravity = Gravity.CENTER
            }

        val BOTTOM: MXDialogPosition
            get() = MXDialogPosition().apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                translationY = -40
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