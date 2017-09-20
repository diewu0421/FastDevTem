package com.epro.fastdevtem.activity.login

import com.epro.fastdevtem.mvp.BasePresenterImpl
import com.epro.fastdevtem.mvp.invokeInHandler
import kotlin.concurrent.thread


class LoginPresenter : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {

    override fun loadData() {
        thread {
            Thread.sleep(2000)

//            view.getHandler()?.post { view.showData() }

//            view.invokeByHandler<LoginContract.View>(view::showData)
            view.invokeInHandler { showData() }
        }
    }
}
