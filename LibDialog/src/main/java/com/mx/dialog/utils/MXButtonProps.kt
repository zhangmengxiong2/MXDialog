package com.mx.dialog.utils

internal data class MXButtonProps(
    val text: CharSequence? = null,
    val visible: Boolean = true,
    val textColor: Int? = null,
    val onclick: (() -> Unit)? = null
)