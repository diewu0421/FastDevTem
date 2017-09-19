package com.epro.fastdevtem.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.epro.fastdevtem.R
import com.epro.fastdevtem.widget.CustomDialog

/**
 * 弹出框工具类
 * @author Zenglw
 * @version 1.0
 * @date 2017-6-15
 */

class DialogManager private constructor(builder: Builder) {
    private val mContext: Context
    private val inputType: Int?
    private var etContent: String? = null
    private var width: Int = 0
    private val contentView: View?
    private val title: String?
    private var dialog: CustomDialog? = null
    private val hint: String?

    init {
        this.contentView = builder.contentView
        this.title = builder.title
        this.mContext = builder.context
        this.width = builder.width
        this.etContent = builder.etContent
        this.inputType = builder.inputType
        this.hint = builder.hint
    }


    companion object {
        fun createDialog(context: Context, layoutId:Int = -1,layoutView:View? = null) :Dialog?{
            var activity = context as Activity
            if (activity.parent != null) {
                activity = activity.parent
            }
            if (activity.isFinishing) {
                return null
            }else {
                if (layoutId != -1) {
                    val view: View = context.layoutInflater.inflate(layoutId, null)
                    return CustomDialog(context, 0, 0, view, R.style.DialogTheme)
                }

                layoutView?.run {
                    return CustomDialog(context, 0, 0, layoutView, R.style.DialogTheme)
                }

            }
            return null
        }
    }

    fun show(): DialogManager {

        if (this.dialog == null) {
            var activity = mContext as Activity
            if (activity.parent != null) {
                activity = activity.parent
            }
            if (activity.isFinishing) {
                return this
            }
            val dialog = CustomDialog(activity, 0, 0, contentView, R.style.DialogTheme)
            dialog.show()
            //获得dialog所在的窗体对象
            val window = dialog.window
            //得到参数
            val params = window!!.attributes
            //动态设置dialog的宽度为屏幕宽度的3/4
            //默认的width为屏幕宽度的四分之三
//        if (width == 0) {
//            width = mContext.getResources().displayMetrics.widthPixels * 4 / 5
//        }
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            //把设置好的参数重新设置给dialog
            window.attributes = params
            window.setContentView(contentView)
            this.dialog = dialog
        }else {
            this.dialog?.show()
        }
        return this
    }

    fun dismiss() {
        if (this.dialog!!.isShowing) {
            this.dialog!!.dismiss()
        }
    }


    class Builder(var context: Context) {
        var contentView: View? = null
        var title: String? = null
        var width: Int = 0
        var etContent: String? = null
        var inputType: Int = 0
        var hint: String? = null

        fun setContentView(view: View): Builder {
            this.contentView = view
            return this
        }

        fun setContentView(@LayoutRes resId: Int): Builder {
            contentView = LayoutInflater.from(context).inflate(resId, null)
            return this
        }


        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setWidth(width: Int): Builder {
            this.width = width
            return this
        }

        fun setInputType(inputType: Int): Builder {
            this.inputType = inputType
            return this
        }

        fun setHint(hint: String): Builder {
            this.hint = hint
            return this
        }

        fun setEtContent(etContent: String): Builder {
            this.etContent = etContent
            return this
        }

        fun setClick(viewId: Int, listener: View.OnClickListener): Builder {
            if (contentView != null) {
                contentView!!.findViewById(viewId).setOnClickListener(listener)
            }
            return this
        }

        fun setText(viewId: Int, text: CharSequence): Builder {
            if (contentView != null) {
                val findView = contentView!!.findViewById(viewId)
                if (findView is TextView) {
                    findView.text = text
                }
            }
            return this
        }

        fun setViewClick(listener: View.OnClickListener, vararg ids: Int): Builder {
            if (contentView == null) {
                throw NullPointerException("Please call method setContentView before call it.")
            }

            for (id in ids) {
                if (id != -1) contentView!!.findViewById(id).setOnClickListener(listener)
            }

            return this
        }

        fun build(): DialogManager {
            return DialogManager(this)
        }
    }

}
