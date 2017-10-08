@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.epro.fastdevtem.util

import com.epro.fastdevtem.base.BaseActivity
import com.epro.fastdevtem.mvp.BaseView
import com.epro.fastdevtem.mvp.getHandler
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
import java.lang.reflect.Type

/**
 * Extension
 * 一些类的拓展发在这个文件里面
 * @author zlw
 * @date 2017/9/21
 */
/**
 * 异步请求
 */
@Suppress("IMPLICIT_CAST_TO_ANY")
@JvmOverloads
fun <T> BaseView.doRequest(url: String,
                           method: FuelMethod = FuelMethod.GET,
                           params: List<Pair<String, Any?>>? = null,
                           headers: List<Pair<String, Any>?>? = null,
                           requestBody: Any? = null,
                           tranformModel: Boolean = true,
                           cls: Type? = null,
                           before: (Request.() -> Unit)? = null,
                           action: ((T?, Pair<Response, Result<String, FuelError>>?) -> Unit)? = null) {
    val request = when (method) {
        FuelMethod.GET -> {
            Fuel.get(url, params).timeout(FuelTool.TIME_OUT).timeoutRead(FuelTool.TIME_OUT).apply {
                headers?.forEach {
                    header(it)
                }
            }
        }

        FuelMethod.POST -> {
            Fuel.post(url,params).timeout(FuelTool.TIME_OUT).timeoutRead(FuelTool.TIME_OUT).apply {
                headers?.forEach {
                    header(it)
                }
                requestBody?.let {
                    this.body(it.toString())
                }
            }
        }
        FuelMethod.DELETE -> { }
        FuelMethod.PUT -> { }
    } as? Request

    before?.run { request?.before() }

    Logger.e("请求信息为:\n" + request.toString())

    request?.rx_responseString()
            ?.doOnSubscribe {
                //子线程 做准备工作 防止阻塞UI线程
            }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe {
                // 主线程的准备工作 有些ui的操作需要在主线程操作
                this.asType { it.showLoadingDialog() }
            }
            ?.doAfterSuccess { t: Pair<Response, Result<String, FuelError>>? ->
            }
            ?.subscribe { t1: Pair<Response, Result<String, FuelError>>?, t2: Throwable? ->
                this.asType { it.dismissLoadingDialog() }
                t2?.run {
                    com.orhanobut.logger.Logger.e("${method.name} 失败 Throwable = \n" + JsonFormat.format(t2.toString()))
                    action?.invoke(null, t1)
                    return@subscribe
                }

                t1?.second?.component2()?.run {
                    com.orhanobut.logger.Logger.e("${method.name} 失败 FuelError = \n" + JsonFormat.format(toString()) + "\nrequest = " + JsonFormat.format(t1.first.toString()))
                    action?.invoke(null, t1)
                    return@subscribe

                }

                //运行在主线程当中
                t1?.second?.component1()?.apply {
                    com.orhanobut.logger.Logger.e("${method.name} 成功\n" + JsonFormat.format(this))
                }
                val resultString = t1?.component2()?.component1()

                try {
                    if (!tranformModel) {
                        action?.invoke(resultString as? T, t1)
                    } else {
                        if (cls != null) {
//                            val data = isSuccess(resultString)
                            if (resultString != null) {
                                action?.invoke(Gson().fromJson(resultString.toString(), cls), t1)
                            } else {
                                action?.invoke(null, t1)
                            }
                        } else {
                            action?.invoke(null, t1)
                        }
                    }
                } catch (e: Exception) {
                    Logger.e("请求出错 : " + e.message)
                }

            }
            ?.add2DisposableList()

}

/****************************************************************************************************************************************************************************************
 * 同步的请求
 * url: String,
method: FuelMethod = FuelMethod.GET,
params: List<Pair<String, Any?>>? = null,
headers: List<Pair<String, Any>?>? = null,
requestBody: Any? = null,
tranformModel: Boolean = true,
cls: Type? = null,
before: (Request.() -> Unit)? = null,
action: ((T?, Pair<Response, Result<String, FuelError>>?) -> Unit)? = null)
 */
@JvmOverloads
fun <T> oSyncRequest(url: String,
                      method: FuelMethod = FuelMethod.GET,
                      params: List<Pair<String, Any?>>? = null,
                      headers: List<Pair<String, Any>?>? = null,
                      requestBody: Any? = null,
                      tranformModel: Boolean = true,
                      cls:Class<T>? = null,
                      before: (Request.() -> Unit)? = null) :T? {
    val request = when (method) {
        FuelMethod.GET -> {
            Fuel.get(url, params).timeout(FuelTool.TIME_OUT).timeoutRead(FuelTool.TIME_OUT).apply {
                headers?.forEach {
                    header(it)
                }
            }
        }

        FuelMethod.POST -> {
            Fuel.post(url, params).timeout(FuelTool.TIME_OUT).timeoutRead(FuelTool.TIME_OUT).apply {
                headers?.forEach {
                    header(it)
                }
                requestBody?.let {
                    body(it.toString())
                }
            }
        }

        FuelMethod.DELETE -> { }

        FuelMethod.PUT -> { }
    } as Request

    before?.run { request.before() }

    val (requestResult: Request?, response: Response?, result: Result<String, FuelError>?)
            = request.run { request.responseString() }

    Logger.e("requestResult = " + requestResult.toString() + "\nresponse = \n" + response.toString() + "\nresult = \n" + result.component1())

    result.component2()?.run { return null }

    val resultString = result.component1()
    //根据不同的后台返回的字段，判断是不是正确请求了

    return if (tranformModel){
        Gson().fromJson(resultString, cls)
    }else {
        @Suppress("UNCHECKED_CAST")
        result.component1() as? T
    }

}

/*****************************************************************************************************************************/
fun Disposable.add2DisposableList() {
    FuelTool.disposableList.add(this)
}

/*****************************************************************************************************************************/
fun Request.add2List() = FuelTool.list.add(this)

/*****************************************************************************************************************************/
class PlusHashMap(val map: HashMap<String, Any?>) : MutableMap<String, Any?> by map {
    override fun put(key: String, value: Any?): Any? {
        map.put(key, value)
        return map
    }
}

/*****************************************************************************************************************************/

inline fun <T> T.invokeInHandler(crossinline block: T.() -> Unit)
        = (this as? BaseView)?.getHandler()?.post { block() }


/*****************************************************************************************************************************/
inline fun <T> T.asType(block: T.(BaseActivity<*, *>) -> Unit) {
    (this as? BaseActivity<*, *>)?.run {
        block(this)
    }
}