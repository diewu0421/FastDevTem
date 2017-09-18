package com.epro.fastdevtem.activity.login

import android.content.Context

import com.epro.fastdevtem.mvp.BasePresenterImpl
import kotlin.concurrent.thread


class LoginPresenter : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {

    override fun loadData() {
        thread {
            Thread.sleep(2000)

            view.showData()
        }
    }
}
