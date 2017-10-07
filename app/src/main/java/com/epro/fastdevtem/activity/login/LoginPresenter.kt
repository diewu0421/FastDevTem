package com.epro.fastdevtem.activity.login

import com.epro.fastdevtem.entity.ShipWayModel
import com.epro.fastdevtem.mvp.BasePresenterImpl
import com.epro.fastdevtem.util.doRequest
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import com.orhanobut.logger.Logger


class LoginPresenter : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {

    override fun loadData() {

        view?.doRequest("https://svcml01.dxqas.com/DMS/token/api/Products/GetShippingMethod?sku=1138&country=US",
                cls = ShipWayModel::class.java,
                headers = listOf("ShipTo" to "US")){
            t: ShipWayModel?, _: Pair<Response, Result<String, FuelError>>? ->
            Logger.e("shipWayModel  = " + t?.toString())

        }

    }
}
