package com.epro.fastdevtem

import android.app.Application
import android.util.Log
import com.epro.fastdevtem.util.CommonConfig
import com.epro.fastdevtem.util.FuelTool
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * Created by ZLW on 2017/9/12.
 */
class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //初始化log
        initLog()
        //初始化配置
        initConfig()
        initFuel()
    }

    private fun initFuel() {
        //在这里初始化Fuel的属性
//        FuelManager.instance.basePath = "http://120.77.56.109/"
        FuelTool.init()
    }

    private fun initConfig() {
        val id = resources.getIdentifier("config", "raw", packageName)
        if (id != 0) {
            val inputStream = resources.openRawResource(id)
            CommonConfig.init(inputStream)
        } else {
            Logger.e(resources.getString(R.string.init_config_error))
        }
    }

    private fun initLog() {

        val formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("FastDev")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {

            override fun isLoggable(priority: Int, tag: String?): Boolean {
                Log.e("MApplication", "${BuildConfig.DEBUG}")
                return BuildConfig.DEBUG
            }
        })
    }

}