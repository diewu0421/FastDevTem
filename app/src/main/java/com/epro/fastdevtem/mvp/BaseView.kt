package com.epro.fastdevtem.mvp

import android.content.Context
import com.epro.fastdevtem.base.BaseActivity


interface BaseView {
    val context: Context
}

fun BaseView.getHandler() = (context as? BaseActivity<*,*>)?.mBaseHandler

public inline fun <T> T.invokeInHandler(crossinline block: T.() -> Unit)
        = (this as? BaseView)?.getHandler()?.post { block() }


inline fun <T> T.asType(block: T.(BaseActivity<*,*>) -> Unit) {
    (this as? BaseActivity<*,*>)?.run {
        block(this)
    }
}


//{
//
//    if (this is BaseView) {
//        getHandler()?.post{block()}
//    }
//}