package com.epro.fastdevtem.mvp;


import java.lang.ref.WeakReference;

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V>{
    private V mView;

    private WeakReference<V> reference;

    @Override
    public void attachView(V view) {

//        mView=view;
        reference = new WeakReference<V>(view);
    }

    @Override
    public void detachView() {

//        mView=null;
        if (reference != null) {
            reference = null;
        }
    }

    public V getView(){
        return reference.get();
    }
}
