package com.mx.dialog.tip

import android.view.Gravity

/**
 * position of dialog content
 */
class MXDialogPosition {
    var gravity = Gravity.CENTER
    var marginTop: Int? = null // dp
    var marginBottom: Int? = null // dp

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