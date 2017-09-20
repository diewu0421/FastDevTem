package com.epro.fastdevtem.mvp


import java.lang.ref.WeakReference

open class BasePresenterImpl<V : BaseView> : BasePresenter<V> {
    private val mView: V? = null

    private var reference: WeakReference<V>? = null

    override fun attachView(view: V) {

        //        mView=view;
        reference = WeakReference(view)
    }

    override fun detachView() {

        //        mView=null;
        if (reference != null) {
            reference = null
        }
    }

    val view: V?
        get() = reference?.get()
}
