@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.epro.fastdevtem.util
import com.epro.fastdevtem.request.SSLSocketFactoryEx
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.*
import javax.net.ssl.HostnameVerifier

/**
 * Fuel网络请求封装类
 * @author zlw
 * @date 2017/9/26
 */

object FuelTool {
//    init {
////        FuelManager.instance.basePath = Constant.BASE_URL.substring(0, Constant.BASE_URL.length - 1)
//        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Basic U1FheUtiY3ZPRFBtQ2RVTGFBUFQzdz09OjE0SlBqS1E0V0xrYTNmdUNaczVWZEE9PQ==")
//
//        if (true) {
//            FuelManager.instance.socketFactory = SSLSocketFactoryEx()
//            FuelManager.instance.hostnameVerifier = HostnameVerifier { _, _ -> true }
//        }
//    }

    //超时时间
    val TIME_OUT = 60000

    val list: ArrayList<Request> by lazy { arrayListOf<Request>() }

    val disposableList: ArrayList<Disposable> by lazy { arrayListOf<Disposable>() }

    /*****************************************************************************************************************************************************************/
    /**
     * Fuel的初始化
     */
    fun init(){
        FuelManager.instance.baseHeaders = mapOf("Authorization" to "Basic U1FheUtiY3ZPRFBtQ2RVTGFBUFQzdz09OjE0SlBqS1E0V0xrYTNmdUNaczVWZEE9PQ==")

        if (true) {
            FuelManager.instance.socketFactory = SSLSocketFactoryEx()
            FuelManager.instance.hostnameVerifier = HostnameVerifier { _, _ -> true }
        }
    }

    /*****************************************************************************************************************************/
    fun cancelAll() {
        if (list.isNotEmpty()) {
            list.forEach { it.cancel() }
        }

        if (disposableList.isNotEmpty()) {
            disposableList.forEach { it.dispose() }
        }
    }

    /*****************************************************************************************************************************/
    @JvmOverloads
    fun <T> doSyncRequest(url: String,
                          method: FuelMethod = FuelMethod.GET,
                          params: List<Pair<String, Any?>>? = null,
                          headers: List<Pair<String, Any>?>? = null,
                          requestBody: Any? = null,
                          tranformModel: Boolean = true,
                          action: ((T?, Pair<Response, Result<String, FuelError>>?) -> Unit)? = null): T? {
        val request = when (method) {
            FuelMethod.GET -> {
                Fuel.get(url, params).timeout(TIME_OUT).timeoutRead(TIME_OUT).apply {
                    headers?.forEach {
                        header(it)
                    }
                }
            }

            FuelMethod.POST -> {
                Fuel.post(url, params).timeout(TIME_OUT).timeoutRead(TIME_OUT).apply {
                    headers?.forEach {
                        header(it)
                    }
                    requestBody?.let {
                        body(it.toString())
                    }
                }
            }

            FuelMethod.DELETE -> {
            }

            FuelMethod.PUT -> {

            }
        } as Request

        val (requestResult: Request?, response: Response?, result: Result<String, FuelError>?)
                = request?.run { request.responseString() }

        Logger.e("requestResult = " + requestResult.toString() + "\nresponse = \n" + response.toString() + "\nresult = \n" + result.component1())

        result?.component2()?.run {
            return null
        }

        if (tranformModel){
            return Gson().fromJson<T>(result?.component1(), object : TypeToken<T>() {}.type)
        }else {
            return result?.component1() as? T

        }

    }
}

/*****************************************************************************************************************************/
enum class FuelMethod {
    GET, PUT, DELETE, POST
}

/*****************************************************************************************************************************/
@JvmOverloads
fun <T> getCountry(action: (T?) -> Unit) {
    async(UI) {
        val deferred = bg {

        }
        action(deferred.await() as? T)
    }
}

/*****************************************************************************************************************************/
