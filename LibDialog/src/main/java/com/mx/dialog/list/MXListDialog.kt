package com.mx.dialog.list

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.utils.MXTextProp

open class MXListDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseListDialog(context, fullScreen) {
    private var onItemClick: ((Int) -> Unit)? = null
    private val listItemProp = MXTextProp()
    private val listAdapt by lazy {
        MXBaseListAdapt<String>(context, R.layout.mx_dialog_list_item) { itemView, item ->
            val infoTxv = itemView.findViewById<TextView>(R.id.infoTxv)
            listItemProp.attachTextHeight(infoTxv, R.dimen.mx_dialog_size_list_item_height)
            listItemProp.attachTextColor(infoTxv, R.color.mx_dialog_color_text)
            listItemProp.attachTextSize(infoTxv, R.dimen.mx_dialog_text_size_content)
            listItemProp.attachTextGravity(infoTxv, Gravity.CENTER)

            infoTxv?.text = item
        }
    }


    override fun initDialog() {
        super.initDialog()
        if (listView == null) return

        listView?.adapter = listAdapt
        listView?.setOnItemClickListener { parent, view, position, id ->
            onItemClick?.invoke(position)
            dismiss()
        }
    }

    fun setItems(
        list: List<String>,
        itemHeightDP: Float? = null,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = null,
        onItemClick: ((index: Int) -> Unit)? = null
    ) {
        listAdapt.list.clear()
        listAdapt.list.addAll(list)
        this.onItemClick = onItemClick
        this.listItemProp.textHeightDP = itemHeightDP
        this.listItemProp.textColor = textColor
        this.listItemProp.textSizeSP = textSizeSP
        this.listItemProp.textGravity = textGravity

        initDialog()
    }
}