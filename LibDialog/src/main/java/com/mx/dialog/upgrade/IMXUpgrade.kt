package com.mx.dialog.upgrade

import android.app.Dialog
import android.content.Context
import java.io.File

interface IMXUpgrade {
    fun download(dialog: Dialog, url: String, progress: ((Int) -> Unit)): File?

    fun install(context: Context, file: File)
}