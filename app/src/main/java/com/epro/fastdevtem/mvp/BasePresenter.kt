package com.epro.fastdevtem.mvp


interface BasePresenter<V : BaseView> {
    fun attachView(view: V)

    fun detachView()
}
