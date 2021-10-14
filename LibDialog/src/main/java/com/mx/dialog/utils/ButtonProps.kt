package com.mx.dialog.utils

data class ButtonProps(
    val visible: Boolean = true,
    val text: CharSequence? = null,
    val color: Int? = null,
    val onclick: (() -> Unit)? = null
)