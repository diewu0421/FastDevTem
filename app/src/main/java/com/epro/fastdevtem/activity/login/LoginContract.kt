package com.epro.fastdevtem.activity.login

import android.content.Context

import com.epro.fastdevtem.mvp.BasePresenter
import com.epro.fastdevtem.mvp.BaseView


class LoginContract {
    interface View : BaseView {
        fun showData(result:String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadData()
    }
}

