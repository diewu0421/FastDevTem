package com.epro.fastdevtem.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.epro.fastdevtem.R
import com.epro.fastdevtem.mvp.BasePresenterImpl
import com.epro.fastdevtem.mvp.BaseView
import com.epro.fastdevtem.mvp.MVPBaseActivity
import com.epro.fastdevtem.util.DialogManager
import com.epro.fastdevtem.util.FuelTool
import java.lang.ref.WeakReference

/**
 * BaseActivity
 * @author zlw
 * @date 2017/9/17
 */
abstract class BaseActivity<V : BaseView, T : BasePresenterImpl<V>> : MVPBaseActivity<V, T>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        mBaseHandler = BaseHandler(this)
        init()
    }

    abstract fun init()

    abstract val layoutId: Int

    fun registerClickEvent(block: (View) -> Unit, vararg views: View)
            = views.forEach { it.setOnClickListener { block(it) } }

    fun toast(message: String, view: View? = null, anim: Int? = null) {
        if (message.isNotEmpty()) {
            if (mToast == null) {
                mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT).also { it.show();makeAnimation(view, anim) }
                return
            }
            mToast?.run {
                cancel()
                mToast = Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).also { it.show();makeAnimation(view, anim) }
            }
        }

    }

    /**
     * 使某一个view做动画
     */
    private fun makeAnimation(view: View?, anim: Int?) {
        if (view != null && anim != null) {
            view.startAnimation(AnimationUtils.loadAnimation(this, anim))
        }
    }

    private var mToast: Toast? = null

    /**
     * 显示加载对话框，如需取消
     * @see BaseActivity.hideLoadingDialog
     */
    fun showLoadingDialog(){
        if (!isFinishing) {
            dialog?.show()
        }
    }

    /**
     * 隐藏加载对话框，如需显示
     * @see BaseActivity.showLoadingDialog
     */
    fun dismissLoadingDialog(){
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    private val dialog:Dialog? by lazy {
        DialogManager.createDialog(this,R.layout.dialog_loading)
    }

    /***************************************************************************************************************************************************************************************************
     * 设置按钮不可点击
     */
    fun setButtonState(view: View, state: Int) {
        when (state) {
            1 -> {
                view.run {
                    //设置按钮可点击的状态
//                    setBackgroundResource(R.drawable.bt_red_shape)
                    isEnabled = true
                }

            }

            2 -> {
                view.run {
                    //设置按钮为不可点击的状态
                    setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                    isEnabled = false

                }
            }
        }
    }


    class BaseHandler(activity:Activity) : Handler(){

        private val mWeakReference = WeakReference<Activity>(activity)

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }

    }

    var mBaseHandler:BaseHandler? = null

    override fun onDestroy() {
        super.onDestroy()
        FuelTool.cancelAll()
    }

}
