package com.epro.fastdevtem.util

import java.io.InputStream
import java.util.*

/**
 * 初始化公共配置
 * @author zlw
 * @date 2017.09.12
 */
object CommonConfig{
    private var properties:Properties? = null
    var BASE_URL:String = ""
    var HTTPS_VERIFY:Boolean = false
    fun init(inputStream: InputStream) {
        properties = Properties()
        properties?.load(inputStream)
        BASE_URL = getProperty("BASE_URL")
        HTTPS_VERIFY = getPropertyBoolean("HTTPS_VERIFY")
    }

    //通过key查找对应的String类型的属性
    fun getProperty(name:String) = properties?.get(name.toLowerCase(Locale.getDefault())).toString()

    //通过key获取对应的布尔属性
    fun getPropertyBoolean(name:String) = properties?.get(name.toLowerCase(Locale.getDefault())).toString().toBoolean()
}