package com.mx.dialog.list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.utils.MXTextProp

open class MXListDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseListDialog(context, fullScreen) {
    private var isSingleSelectMod = true
    private var onSingleSelect: ((Int) -> Unit)? = null
    private var onMultipleSelect: ((List<Int>) -> Unit)? = null
    private var showSelectTag: Boolean = false
    private val selectIndexList = HashSet<Int>()
    private val listItemProp = MXTextProp()
    private val listAdapt by lazy {
        MXBaseListAdapt<String>(context, R.layout.mx_dialog_list_item) { itemView, position, item ->
            val infoTxv = itemView.findViewById<TextView>(R.id.infoTxv)
            val selectTag = itemView.findViewById<ImageView>(R.id.selectTag)
            listItemProp.attachTextHeight(infoTxv, R.dimen.mx_dialog_size_list_item_height)
            listItemProp.attachTextColor(infoTxv, R.color.mx_dialog_color_text)
            listItemProp.attachTextSize(infoTxv, R.dimen.mx_dialog_text_size_content)
            listItemProp.attachTextGravity(infoTxv, Gravity.CENTER)
            if (showSelectTag) {
                if (selectIndexList.contains(position)) {
                    selectTag?.setImageResource(R.drawable.mx_dialog_icon_select)
                } else {
                    selectTag?.setImageResource(R.drawable.mx_dialog_icon_unselect)
                }
                selectTag?.visibility = View.VISIBLE
            } else {
                selectTag?.visibility = View.GONE
            }

            infoTxv?.text = item
        }
    }


    override fun initDialog() {
        super.initDialog()
        setAdapt(listAdapt)
        if (isSingleSelectMod) {
            setItemClick { position ->
                dismiss()
                onSingleSelect?.invoke(position)
            }
        } else {
            setItemClick { position ->
                if (selectIndexList.contains(position)) {
                    selectIndexList.remove(position)
                } else {
                    selectIndexList.add(position)
                }
                listAdapt.notifyDataSetChanged()
            }
            setActionClick {
                dismiss()
                val list = selectIndexList.toList().sorted()
                onMultipleSelect?.invoke(list)
            }
        }
    }

    override fun showActionBtn(): Boolean {
        return !isSingleSelectMod
    }

    /**
     * 设置多选列表项
     * @param list 数据集合
     * @param showSelectTag 是否显示 选中/未选中 标签
     * @param selectIndex 默认选中Index
     * @param itemHeightDP 数据Item文字行高度
     * @param textColor 数据Item文字颜色
     * @param textSizeSP 数据Item文字大小
     * @param textGravity 数据Item文字Gravity方向
     * @param onSelect 数据Item选中点击回调
     */
    fun setItems(
        list: List<String>,
        showSelectTag: Boolean? = null,
        selectIndex: Int? = null,
        itemHeightDP: Float? = null,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = null,
        onSelect: ((index: Int) -> Unit)? = null
    ) {
        listAdapt.list.clear()
        listAdapt.list.addAll(list)
        this.onSingleSelect = onSelect
        this.showSelectTag = showSelectTag ?: (selectIndex != null) //
        this.selectIndexList.clear()
        selectIndex?.let { this.selectIndexList.add(it) }
        this.listItemProp.textHeightDP = itemHeightDP
        this.listItemProp.textColor = textColor
        this.listItemProp.textSizeSP = textSizeSP
        this.listItemProp.textGravity = textGravity
        this.isSingleSelectMod = true

        initDialog()
    }

    /**
     * 设置单选列表项
     * @param list 数据集合
     * @param selectIndexList 默认选中Index列表
     * @param itemHeightDP 数据Item文字行高度
     * @param textColor 数据Item文字颜色
     * @param textSizeSP 数据Item文字大小
     * @param textGravity 数据Item文字Gravity方向
     * @param onSelect 数据Item选中点击回调
     */
    fun setMultipleItems(
        list: List<String>,
        selectIndexList: List<Int>? = null,
        itemHeightDP: Float? = null,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = null,
        onSelect: ((list: List<Int>) -> Unit)? = null
    ) {
        listAdapt.list.clear()
        listAdapt.list.addAll(list)
        this.onMultipleSelect = onSelect
        this.showSelectTag = true
        this.selectIndexList.clear()
        selectIndexList?.let { this.selectIndexList.addAll(it) }
        this.listItemProp.textHeightDP = itemHeightDP
        this.listItemProp.textColor = textColor
        this.listItemProp.textSizeSP = textSizeSP
        this.listItemProp.textGravity = textGravity
        this.isSingleSelectMod = false

        initDialog()
    }
}