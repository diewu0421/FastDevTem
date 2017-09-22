package com.epro.fastdevtem.activity.login

import com.epro.fastdevtem.mvp.BasePresenterImpl
import com.epro.fastdevtem.mvp.asType
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import droidhao.clevercity.util.doPost
import kotlin.concurrent.thread


class LoginPresenter : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {

    override fun loadData() {
        thread {
            Thread.sleep(2000)

//            view.getHandler()?.post { view.showData() }

//            view.invokeByHandler<LoginContract.View>(view::showData)
//            view.invokeInHandler { showData() }

            view?.asType zlw@ {

                //            val map = HashMap<String, Any?>()
//            map.put("token", it.token)
//            map.put("consume_type", "4")

                //            it.handleData(BaseRequest(Config.PATH_CART2, TypeFactory.getType(Config.TYPE_CONFIRM_ORDER)!!, map),
//                    object : HandleData<ConfirmOrderBean1> {
//                        override fun handle(data: ConfirmOrderBean1) {
//                            Log.e("ConfirmOrderPresenter", data.toString())
//                            Log.e("ConfirmOrderPresenter", data.userInfo.user_money)
//
//                            //调用activity的方法展示数据
//                            view?.setAddress()
//                        }
//
//                        override fun fail() {}
//                    })


                //更加优雅的请求
//            FuelUtil.doPost(Config.PATH_CART2,
//                    listOf("token" to it.token, "consume_type" to 4),
//                    ConfirmOrderBean::class.java,
//                    { Logger.e("doonsubscrio");it.showLoadingDialog() },
//                    { Logger.e("doafter");it.dismissLoadingDialog() }) {
////                triple: Triple<ConfirmOrderBean?, Pair<Response, Result<String, FuelError>>?, Throwable?> ->
//                confirmOrderBean: ConfirmOrderBean?, pair: Pair<Response, Result<String, FuelError>>? ->
//                pair?.second?.component2()?.run {
//                    it.toast(this.toString())
//                    return@doPost
//                }
//
//                setAddress(confirmOrderBean)
//            }


                this.doPost("index.php/api/Cart/cart2.html"){
                    s: String?, pair: Pair<Response, Result<String, FuelError>>? ->
                    this.showData(s?:"")
                }

            }
        }
    }
}
