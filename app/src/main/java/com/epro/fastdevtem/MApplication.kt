package com.epro.fastdevtem

import android.app.Application
import android.util.Log
import com.epro.fastdevtem.util.CommonConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * Created by ZLW on 2017/9/12.
 */
class MApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        //初始化log
        initLog()
        //初始化配置
        initConfig()
    }

    private fun initConfig() {
        val id = resources.getIdentifier("config", "raw", packageName)
        if (id != 0) {
            val inputStream = resources.openRawResource(id)
            CommonConfig.init(inputStream)
        }else {
            Logger.e(resources.getString(R.string.init_config_error))
        }
    }

    private fun initLog() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {

            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}