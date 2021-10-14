package com.mx.dialog.toast

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mx.dialog.R

object MXToast {
    private var snackBarShowing: Snackbar? = null
    fun show(view: View, message: String, duration: Int = Snackbar.LENGTH_LONG) {
        snackBarShowing?.dismiss()
        snackBarShowing = null

        val snackBar = Snackbar.make(view, message, duration)
        val resource = view.resources
        snackBar.setBackgroundTint(resource.getColor(R.color.mx_color_snack_bg))
        snackBar.setTextColor(resource.getColor(R.color.mx_color_focus))
        snackBar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (transientBottomBar == snackBarShowing) {
                    snackBarShowing = null
                }
            }
        })
        snackBar.show()
        snackBarShowing = snackBar
    }

    internal fun contextFindView(context: Context): View? {
        return when (context) {
            is Dialog -> {
                context.actionBar?.customView
            }
            is android.app.Fragment -> {
                context.activity?.findViewById<ViewGroup>(android.R.id.content)
            }
            is androidx.fragment.app.Fragment -> {
                context.activity?.findViewById<ViewGroup>(android.R.id.content)
            }
            is Activity -> {
                context.findViewById<ViewGroup>(android.R.id.content)
            }
            else -> null
        }
    }
}

fun Activity.toast(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    val view = MXToast.contextFindView(this) ?: return
    MXToast.show(view, message, duration)
}

fun android.app.Fragment.toast(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    val view = MXToast.contextFindView(this.activity) ?: return
    MXToast.show(view, message, duration)
}

fun androidx.fragment.app.Fragment.toast(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    val view = this.activity?.let { MXToast.contextFindView(it) } ?: return
    MXToast.show(view, message, duration)
}

fun View.toast(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    MXToast.show(this, message, duration)
}