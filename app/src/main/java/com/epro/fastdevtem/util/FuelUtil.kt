package droidhao.clevercity.util

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import io.reactivex.disposables.Disposable

/**
 * FuelUtil
 * @author zlw
 * @date 2017/9/21
 */
object FuelUtil {
//    init {
//        FuelManager.instance.apply {
//            basePath = Config.BASE_URL
//        }
//    }

    val list: ArrayList<Request> = arrayListOf()

    val disposableList: ArrayList<Disposable> = arrayListOf()

    val TIME_OUT = 60000

    fun cancelAll(){
        if (list.isNotEmpty()) {
            list.forEach { it.cancel() }
        }

        if (disposableList.isNotEmpty()) {
            disposableList.forEach { it.dispose() }
        }
    }


    @JvmOverloads
    fun doGet(url: String, params: List<Pair<String, Any?>>,action:Request.() ->Unit) {
        Fuel.get(url, params)
                .timeout(TIME_OUT)
                .timeoutRead(TIME_OUT)
                .run { action();this}
                .add2List()
    }

    @JvmOverloads
    inline fun doPost(url: String, params: List<Pair<String, Any?>>,action:Request.() ->Unit) {
        Fuel.post(url, params)
                .timeout(TIME_OUT)
                .timeoutRead(TIME_OUT)
                .run { action();this}
                .add2List()
    }

//    @JvmOverloads
//    fun <T> BaseView.doPost(url: String, params: List<Pair<String, Any?>>, type:Class<T>? = null,
//                   doOnSubscribe: (() -> Unit)? = null,
//                   doAfterSuccess: (() -> Unit)? = null,
//                   action:(T?, Pair<Response, Result<String, FuelError>>?) ->Unit) {
//        Fuel.post(url, params)
//                .timeout(TIME_OUT)
//                .timeoutRead(TIME_OUT)
//                .rx_responseString()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe{
//                    doOnSubscribe?.invoke()
//                }
//                .doAfterSuccess { t: Pair<Response, Result<String, FuelError>>? ->
//                    //运行在子线程子线程《可以考虑存储到数据当中，现在暂时不考虑
//                    doAfterSuccess?.invoke()
//                }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { t1: Pair<Response, Result<String, FuelError>>?, t2: Throwable? ->
//
//                    t2?.run {
//                        Logger.e("doPost 失败 Throwable = \n" + t2.toString())
//                        return@subscribe
//                    }
//
//                    //运行在主线程当中
//                    Logger.e("doPost 成功\n" + t1.toString())
//                    val resultString = t1?.component2()?.component1()
//                    action(type?.run { Gson().fromJson(resultString,this) }, t1)
//                }
//                .add2DisposableList()
//    }



}


