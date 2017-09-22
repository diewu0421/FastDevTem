package droidhao.clevercity.util

import com.epro.fastdevtem.mvp.BaseView
import com.epro.fastdevtem.mvp.asType
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.rx.rx_responseString
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Extension
 * @author zlw
 * @date 2017/9/21
 */
fun Request.add2List() = FuelUtil.list.add(this)

fun Map<*, *>.plusParam(param: Pair<String, Any?>) {
//    this.


}

class PlusHashMap(val map: HashMap<String, Any?>) : MutableMap<String, Any?> by map {
    override fun put(key: String, value: Any?): Any? {
        map.put(key, value)
        return map
    }
}

fun Disposable.add2DisposableList() = FuelUtil.disposableList.add(this)

@JvmOverloads
fun <T> BaseView.doPost(url: String, params: List<Pair<String, Any?>>? = null, type:Class<T>? = null,
                        doOnSubscribe: (() -> Unit)? = null,
                        doAfterSuccess: (() -> Unit)? = null,
                        action: ((T?, Pair<Response, Result<String, FuelError>>?) -> Unit)? = null) {
    Fuel.post(url, params)
            .timeout(FuelUtil.TIME_OUT)
            .timeoutRead(FuelUtil.TIME_OUT)
            .rx_responseString()
            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{
                this.asType { it.showLoadingDialog() }
                doOnSubscribe?.invoke()
            }
            .doAfterSuccess { t: Pair<Response, Result<String, FuelError>>? ->
                this.asType { it.dismissLoadingDialog() }

                doAfterSuccess?.invoke()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t1: Pair<Response, Result<String, FuelError>>?, t2: Throwable? ->

                t2?.run {
                    Logger.e("doPost 失败 Throwable = \n" + t2.toString())
                    return@subscribe
                }

                //运行在主线程当中
                Logger.e("doPost 成功\n" + t1.toString())
                val resultString = t1?.component2()?.component1()

                if (type == null){
                    action?.invoke(resultString as? T, t1)
                }else {
                    action?.invoke(type.run { Gson().fromJson(resultString,this) }, t1)
                }

            }
            .add2DisposableList()
}