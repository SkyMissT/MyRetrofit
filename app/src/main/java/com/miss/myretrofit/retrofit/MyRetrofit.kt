package com.miss.myretrofit.retrofit

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

/**
 *   Created by Vola on 2020/9/1.
 */
class MyRetrofit {
    val serviceMethodCache: ConcurrentHashMap<Method, ServiceMethod> = ConcurrentHashMap()
    var callFactory: Call.Factory
    var baseUrl: HttpUrl

    constructor(callFactory: Call.Factory, baseUrl: HttpUrl){
        this.callFactory =callFactory
        this.baseUrl = baseUrl
    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { _, method, args -> //解析这个method 上所有的注解信息
            val serviceMethod = loadServiceMethod(method)
            //args:
            serviceMethod!!.invoke(args)!!
        } as T
    }

    private fun loadServiceMethod(method: Method): ServiceMethod? {
        //先不上锁，避免synchronized的性能损失
        var result = serviceMethodCache[method]
        if (result != null) return result
        //多线程下，避免重复解析,
        synchronized(serviceMethodCache) {
            result = serviceMethodCache[method]
            if (result == null) {
                result = ServiceMethod.Builder(this,method).build()
                serviceMethodCache[method] = result!!
            }
        }
        return result
    }

    class Builder {
        private lateinit var baseUrl: HttpUrl

        //Okhttp->OkhttClient
        private lateinit var callFactory: Call.Factory

        fun callFactory(factory: Call.Factory?): Builder {
            callFactory = factory!!
            return this
        }

        fun baseUrl(baseUrl: String?): Builder {
            this.baseUrl = HttpUrl.get(baseUrl)
            return this
        }

        fun build(): MyRetrofit {
            checkNotNull(baseUrl) { "Base URL required." }
            var callFactory = callFactory
            if (callFactory == null) {
                callFactory = OkHttpClient()
            }
            return MyRetrofit(callFactory, baseUrl!!)
        }
    }

}