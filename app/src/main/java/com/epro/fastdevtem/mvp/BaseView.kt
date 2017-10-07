package com.epro.fastdevtem.mvp

import android.content.Context
import com.epro.fastdevtem.base.BaseActivity


interface BaseView {
    val context: Context
}

fun BaseView.getHandler() = (context as? BaseActivity<*,*>)?.mBaseHandler
