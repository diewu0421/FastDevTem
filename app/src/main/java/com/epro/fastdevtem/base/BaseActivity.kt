package com.epro.fastdevtem.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.epro.fastdevtem.R
import com.epro.fastdevtem.mvp.BasePresenterImpl
import com.epro.fastdevtem.mvp.BaseView
import com.epro.fastdevtem.mvp.MVPBaseActivity
import com.epro.fastdevtem.util.DialogManager
import com.orhanobut.logger.Logger

/**
 * BaseActivity
 * @author zlw
 * @date 2017/9/17
 */
abstract class BaseActivity<V : BaseView, T : BasePresenterImpl<V>> : MVPBaseActivity<V, T>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        init()
    }

    abstract fun init()

    abstract val layoutId: Int

    fun registerClickEvent(block: (View) -> Unit, vararg views: View)
            = views.forEach { it.setOnClickListener { block(it) } }

    fun toast(message: String) {
        if (message.isNotEmpty()) {
            if (mToast == null) {
                mToast = Toast.makeText(this,message,Toast.LENGTH_SHORT).also { it.show() }
                return
            }
            mToast?.run {
                cancel()
                mToast = Toast.makeText(this@BaseActivity,message,Toast.LENGTH_SHORT).also { it.show() }
            }
        }
    }

    private var mToast: Toast? = null

    fun showLoadingDialog(){
        if (!isFinishing) {
            dialog?.show()
        }
    }

    fun hideLoadingDialog(){
        if (dialog?.isShowing == true) {
            dialog?.hide()
        }
    }

    private val dialog:Dialog? by lazy {
//        DialogManager.Builder(this)
//                .setContentView(R.layout.dialog_loading)
//                .setText(R.id.tipTextView, "正在加载中...")
//                .build()
        DialogManager.createDialog(this,R.layout.dialog_loading)
    }

}
