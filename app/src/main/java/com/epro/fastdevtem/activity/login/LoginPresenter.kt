package com.epro.fastdevtem.activity.login

import com.epro.fastdevtem.mvp.BasePresenterImpl
import com.epro.fastdevtem.mvp.asType
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import droidhao.clevercity.util.doPost


class LoginPresenter : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {

    override fun loadData() {
        view?.asType zlw@ {
            this.doPost("index.php/api/Cart/cart2.html"){
                s: String?, pair: Pair<Response, Result<String, FuelError>>? ->
                this.showData(s?:"")
            }

        }
    }
}
