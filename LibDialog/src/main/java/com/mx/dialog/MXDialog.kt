package com.mx.dialog

import android.content.Context
import android.view.Gravity
import com.mx.dialog.list.MXListDialog
import com.mx.dialog.tip.MXCardPosition
import com.mx.dialog.tip.MXPosition
import com.mx.dialog.tip.MXTipDialog
import com.mx.dialog.tip.MXType
import com.mx.dialog.utils.IMXLifecycle
import com.mx.dialog.utils.MXUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MXDialog {
    fun setDebug(debug: Boolean) {
        MXUtils.setDebug(debug)
    }

    private val lifecycleList = ArrayList<IMXLifecycle>()
    internal fun getLifecycleList(): List<IMXLifecycle> {
        return lifecycleList.toList()
    }

    fun addLifecycle(lifecycle: IMXLifecycle) {
        lifecycleList.add(lifecycle)
    }

    fun removeLifecycle(lifecycle: IMXLifecycle) {
        lifecycleList.remove(lifecycle)
    }

    /**
     * 需要用户确认的操作，无法返回且只能点击确认/取消
     * @param message 消息内容
     * @param title 标题 默认=“温馨提示”
     * @param actionButtonText 确认按钮文字
     * @param cancelButtonText 取消按钮文字
     * @param onActionClick 操作点击回调、
     * @param maxContentRatio 内容最大高度比
     */
    fun confirm(
        context: Context,
        message: CharSequence,
        title: CharSequence? = null,
        actionButtonText: CharSequence? = null,
        cancelButtonText: CharSequence? = null,
        cancelable: Boolean = true,
        cancelableOnTouchOutside: Boolean = true,
        maxContentRatio: Float = 1f,
        cancelPosition: MXPosition = MXPosition.LEFT,
        onActionClick: ((confirm: Boolean) -> Unit)? = null
    ): MXTipDialog {
        val dialog = MXTipDialog(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setMaxContentRatio(maxContentRatio)
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelableOnTouchOutside)
        dialog.addActionBtn(text = actionButtonText) { onActionClick?.invoke(true) }
        if (cancelable) {
            dialog.setCancelBtn(text = cancelButtonText) {
                onActionClick?.invoke(false)
            }
            dialog.setOnCancelListener {
                onActionClick?.invoke(false)
            }
            dialog.setCancelPosition(cancelPosition)
        }
        dialog.setTipType(MXType.WARN)
        dialog.show()
        return dialog
    }

    suspend fun confirmSync(
        context: Context,
        message: CharSequence,
        title: CharSequence? = null,
        actionButtonText: CharSequence? = null,
        cancelButtonText: CharSequence? = null,
        cancelable: Boolean = true,
        cancelableOnTouchOutside: Boolean = true,
        maxContentRatio: Float = 1f,
        cancelPosition: MXPosition = MXPosition.LEFT
    ): Boolean = withContext(Dispatchers.Main) {
        var hasConfirm = false
        val lock = Object()
        confirm(
            context, message, title,
            actionButtonText, cancelButtonText,
            cancelable, cancelableOnTouchOutside,
            maxContentRatio, cancelPosition
        ) { confirm ->
            hasConfirm = confirm
            synchronized(lock) { lock.notifyAll() }
        }
        return@withContext withContext(Dispatchers.IO) {
            synchronized(lock) { lock.wait() }
            hasConfirm ?: false
        }
    }

    /**
     * 显示提示信息，不需要返回操作
     * @param message 内容
     * @param title 标题
     * @param actionButtonText 活动按钮文字
     * @param maxContentRatio 内容最大高度比
     * @param dismissDelay x秒后弹窗消失
     * @param dialogType Icon类型
     */
    fun tip(
        context: Context, message: CharSequence, title: CharSequence? = null,
        actionButtonText: CharSequence? = null, maxContentRatio: Float = 1f,
        dismissDelay: Int? = null, dialogType: MXType? = null
    ): MXTipDialog {
        val dialog = MXTipDialog(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setCancelable(true)
        dialog.setMaxContentRatio(maxContentRatio)
        dialog.setDismissDelay(dismissDelay)
        dialog.setCancelBtn(visible = false)

        dialog.addActionBtn(
            text = actionButtonText
                ?: context.resources.getString(R.string.mx_dialog_button_action_text)
        )
        dialog.setTipType(dialogType)
        dialog.show()
        return dialog
    }

    suspend fun tipSync(
        context: Context, message: CharSequence, title: CharSequence? = null,
        actionButtonText: CharSequence? = null, maxContentRatio: Float = 1f,
        dismissDelay: Int? = null, dialogType: MXType? = null
    ) = withContext(Dispatchers.Main) {
        val lock = Object()
        val dialog = tip(
            context,
            message,
            title,
            actionButtonText,
            maxContentRatio,
            dismissDelay,
            dialogType
        )
        dialog.setOnDismissListener {
            synchronized(lock) { lock.notifyAll() }
        }
        dialog.show()

        return@withContext withContext(Dispatchers.IO) {
            synchronized(lock) { lock.wait() }
        }
    }

    fun select(
        context: Context,
        list: List<String>,
        selectIndex: Int? = null,
        title: CharSequence? = null,
        cancelable: Boolean = true,
        contentMaxHeightRatio: Float = 1f,
        contentRadiusDP: Float = 10f,
        contentMarginDP: Float = 20f,
        position: MXCardPosition = MXCardPosition.BOTTOM,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = Gravity.LEFT or Gravity.CENTER_VERTICAL,
        onCancelListener: (() -> Unit)? = null,
        select: ((index: Int) -> Unit)
    ): MXListDialog {
        val dialog = MXListDialog(context)
        dialog.setTitle(title)
        dialog.setContentMaxHeightRatio(contentMaxHeightRatio)
        dialog.setContentCornerRadius(contentRadiusDP)
        dialog.setContentPosition(position)
        dialog.setContentMargin(contentMarginDP)
        dialog.setCancelable(cancelable)
        dialog.setOnCancelListener(onCancelListener)
        dialog.setItems(
            list,
            selectIndex = selectIndex,
            textColor = textColor,
            textSizeSP = textSizeSP,
            textGravity = textGravity,
            onSelect = select
        )
        dialog.show()
        return dialog
    }

    suspend fun selectSync(
        context: Context,
        list: List<String>,
        selectIndex: Int? = null,
        title: CharSequence? = null,
        cancelable: Boolean = true,
        contentMaxHeightRatio: Float = 1f,
        contentRadiusDP: Float = 10f,
        contentMarginDP: Float = 20f,
        position: MXCardPosition = MXCardPosition.BOTTOM,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = Gravity.LEFT or Gravity.CENTER_VERTICAL
    ): Int? = withContext(Dispatchers.Main) {
        var checkedIndex: Int? = null
        val lock = Object()
        select(
            context = context,
            list = list,
            selectIndex = selectIndex,
            title = title,
            cancelable = cancelable,
            contentMaxHeightRatio = contentMaxHeightRatio,
            contentRadiusDP = contentRadiusDP,
            contentMarginDP = contentMarginDP,
            position = position,
            textColor = textColor,
            textSizeSP = textSizeSP,
            textGravity = textGravity,
            onCancelListener = { synchronized(lock) { lock.notifyAll() } }
        ) { index ->
            checkedIndex = index
            synchronized(lock) { lock.notifyAll() }
        }

        return@withContext withContext(Dispatchers.IO) {
            synchronized(lock) { lock.wait() }
            checkedIndex
        }
    }

    fun selectMulti(
        context: Context,
        list: List<String>,
        selectIndexList: List<Int>? = null,
        title: CharSequence? = null,
        cancelable: Boolean = true,
        contentMaxHeightRatio: Float = 1.2f,
        contentRadiusDP: Float = 10f,
        contentMarginDP: Float = 20f,
        position: MXCardPosition = MXCardPosition.BOTTOM,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = Gravity.LEFT or Gravity.CENTER_VERTICAL,
        onCancelListener: (() -> Unit)? = null,
        select: ((list: List<Int>) -> Unit)
    ): MXListDialog {
        val dialog = MXListDialog(context)
        dialog.setTitle(title)
        dialog.setContentMaxHeightRatio(contentMaxHeightRatio)
        dialog.setContentCornerRadius(contentRadiusDP)
        dialog.setContentPosition(position)
        dialog.setContentMargin(contentMarginDP)
        dialog.setCancelable(cancelable)
        dialog.setOnCancelListener(onCancelListener)
        dialog.setMultipleItems(
            list,
            selectIndexList = selectIndexList,
            textColor = textColor,
            textSizeSP = textSizeSP,
            textGravity = textGravity,
            onSelect = select
        )
        dialog.show()
        return dialog
    }

    suspend fun selectMultiSync(
        context: Context,
        list: List<String>,
        selectIndexList: List<Int>? = null,
        title: CharSequence? = null,
        cancelable: Boolean = true,
        contentMaxHeightRatio: Float = 1.2f,
        contentRadiusDP: Float = 10f,
        contentMarginDP: Float = 20f,
        position: MXCardPosition = MXCardPosition.BOTTOM,
        textColor: Int? = null,
        textSizeSP: Float? = null,
        textGravity: Int? = Gravity.LEFT or Gravity.CENTER_VERTICAL
    ): List<Int>? = withContext(Dispatchers.Main) {
        var checkedIndex: List<Int>? = null
        val lock = Object()
        selectMulti(
            context = context,
            list = list,
            selectIndexList = selectIndexList,
            title = title,
            cancelable = cancelable,
            contentMaxHeightRatio = contentMaxHeightRatio,
            contentRadiusDP = contentRadiusDP,
            contentMarginDP = contentMarginDP,
            position = position,
            textColor = textColor,
            textSizeSP = textSizeSP,
            textGravity = textGravity,
            onCancelListener = { synchronized(lock) { lock.notifyAll() } }
        ) { index ->
            checkedIndex = index
            synchronized(lock) { lock.notifyAll() }
        }

        return@withContext withContext(Dispatchers.IO) {
            synchronized(lock) { lock.wait() }
            checkedIndex
        }
    }

    private fun tipWithType(context: Context, message: String, type: MXType) {
        MXTipDialog(context).apply {
            setTipType(type)
            setMessage(message)
            setCancelable(false)
        }.show()
    }

    fun warn(context: Context, message: String) = tipWithType(context, message, MXType.WARN)
    fun success(context: Context, message: String) = tipWithType(context, message, MXType.SUCCESS)
    fun error(context: Context, message: String) = tipWithType(context, message, MXType.ERROR)
}