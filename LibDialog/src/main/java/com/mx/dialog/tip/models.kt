package com.mx.dialog.tip

import android.view.Gravity

/**
 * position of dialog content
 */
class MXCardPosition {
    var gravity = Gravity.CENTER

    var translationX: Int? = null //X轴偏移 dp
    var translationY: Int? = null //Y轴偏移 dp

    companion object {
        val TOP: MXCardPosition
            get() = MXCardPosition().apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                translationY = 20
            }

        val CENTER: MXCardPosition
            get() = MXCardPosition().apply {
                gravity = Gravity.CENTER
            }

        val BOTTOM: MXCardPosition
            get() = MXCardPosition().apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                translationY = -40
            }
    }
}

enum class MXBtnStyle {
    /**
     * 圆角带边距
     */
    Rounded,

    /**
     * 背景填充，无边距
     */
    FillBackground,

    /**
     * 空白填充，设置焦点文字颜色
     */
    ActionFocus;
}

enum class MXPosition {
    LEFT, RIGHT
}

/**
 * type of dialog icon
 */
enum class MXType {
    SUCCESS,
    WARN,
    ERROR,
    NONE
}