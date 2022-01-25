package com.mx.dialog.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter


internal class MXBaseListAdapt<T>(
    context: Context,
    private val resource: Int,
    val list: ArrayList<T> = ArrayList(),
    private val viewAttach: ((itemView: View, item: T?) -> Unit)
) : ArrayAdapter<T>(context, resource, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = list.getOrNull(position)
        val view: View =
            convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        viewAttach.invoke(view, item)
        return view
    }
}