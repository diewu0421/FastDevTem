package com.epro.fastdevtem.activity.login

import android.content.Context

import com.epro.fastdevtem.mvp.BasePresenterImpl
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import kotlin.concurrent.thread


class LoginPresenter : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {

    override fun loadData() {
//        thread {
//            Thread.sleep(2000)
            FuelManager.instance.baseHeaders = mapOf("Authorization" to "Basic aXBhZC5hcHBzLmR4LmNvbTpaenNfRDFkJUZoR1FmN1k=")

//            FuelManager.instance.basePath =
//            val result = Fuel.get(URL).responseString()

//            view.showData(String(result.second.data))

                Fuel.get(URL).responseString{request, response, result ->
//                    val (data,error) = result
//                    if (error != null) {
//                        view.showData(data?:"")
//                    }else {
//                        view.showData(data?:"")
//
//                    }

                    view.showData(String(response.data))
                }
//        }
    }

    companion object {
        val URL = "https://dms.dx.com/svc/v2.5/api/Categorys?bIndex=1&bSize=8&naIndex=1&naSize=16&naOrderBy={naOrderBy}&naWhere={naWhere}&naCategoryId=null&naRelatedType=newarrival"

    }
}
