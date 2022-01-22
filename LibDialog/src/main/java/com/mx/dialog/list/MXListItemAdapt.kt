package com.mx.dialog.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mx.dialog.R

internal class MXListItemAdapt(context: Context, val list: ArrayList<String> = ArrayList()) :
    ArrayAdapter<String>(context, R.layout.mx_dialog_list_item, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val content = list.getOrNull(position)
        var viewHolder: ViewHolder? = null
        val view: View
        if (convertView == null) {
            view =
                LayoutInflater.from(context).inflate(R.layout.mx_dialog_list_item, parent, false)
            viewHolder = ViewHolder(view.findViewById(R.id.infoTxv))
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.infoTxv.text = content

        return view
    }

    data class ViewHolder(val infoTxv: TextView)
}