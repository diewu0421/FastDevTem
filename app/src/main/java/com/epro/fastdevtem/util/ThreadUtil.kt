package com.epro.fastdevtem.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 线程池相关工具类
 * @author zlw
 * @date 2017/8/15
 */
class ThreadUtil {

    var threadPoolIntance: ExecutorService? = null
        private set

    var supportMaxPoolSize: Int = 0
        private set//最大支持线程大小，Cache模式下为无限制 -1

    init {
        //获取当前系统的CPU 数目
        val cpuNums = Runtime.getRuntime().availableProcessors()

        if (cpuNums >= 3) {
            //executorService = Executors.newCachedThreadPool();
            //supportMaxPoolSize = -1;
            supportMaxPoolSize = 10
            threadPoolIntance = Executors.newFixedThreadPool(supportMaxPoolSize)
        } else {
            supportMaxPoolSize = cpuNums * POOL_SIZE

            //ExecutorService通常根据系统资源情况灵活定义线程池大小
            threadPoolIntance = Executors.newFixedThreadPool(supportMaxPoolSize)

        }

    }

    companion object {

        val instance = ThreadUtil()

        //    private  ExecutorService threadPoll=Executors.newFixedThreadPool(5);

        /**
         * @Fields POOL_SIZE : 单个CPU线程池大小
         */
        private val POOL_SIZE = 4
    }
}
