package com.miss.myretrofit.retrofit

import com.miss.myretrofit.retrofit.annotation.Field
import com.miss.myretrofit.retrofit.annotation.GET
import com.miss.myretrofit.retrofit.annotation.POST
import com.miss.myretrofit.retrofit.annotation.Query
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import java.lang.reflect.Method

/**
 *   Created by Vola on 2020/9/1.
 */
class ServiceMethod {

    private var callFactory: Call.Factory? = null
    private var relativeUrl: String? = null
    private var hasBody:Boolean = false
    private var parameterHandler: Array<ParameterHandler?>? = null
    private lateinit var formBuild: FormBody.Builder
    lateinit var baseUrl: HttpUrl
    lateinit var httpMethod: String
    lateinit var urlBuilder: HttpUrl.Builder

    constructor(builder: Builder){
        baseUrl = builder.myRetrofit.baseUrl
        callFactory = builder.myRetrofit.callFactory

        httpMethod = builder.httpMethod!!
        relativeUrl = builder.relativeUrl
        hasBody = builder.hasBody
        parameterHandler = builder.parameterHandler

        //如果是有请求体,创建一个okhttp的请求体对象

        //如果是有请求体,创建一个okhttp的请求体对象
        if (hasBody) {
            formBuild = FormBody.Builder()
        }
    }


    //Post   把k-v 放到 请求体中
    fun addFiledParameter(key: String?, value: String?) {
        formBuild.add(key, value)
    }

    // get请求,  把 k-v 拼到url里面
    fun addQueryParameter(key: String?, value: String?) {
        if (urlBuilder == null) {
            urlBuilder = baseUrl.newBuilder(relativeUrl)!!
        }
        urlBuilder.addQueryParameter(key, value)
    }

    operator fun invoke(args: Array<Any>): Any? {
        /**
         * 1  处理请求的地址与参数
         */
        for (i in parameterHandler!!.indices) {
            val handlers = parameterHandler!![i]!!
            //handler内本来就记录了key,现在给到对应的value
            handlers.apply(this, args[i].toString())
        }

        //获取最终请求地址
        if (urlBuilder == null) {
            urlBuilder = baseUrl.newBuilder(relativeUrl)!!
        }
        val url: HttpUrl = urlBuilder.build()

        //请求体
        var formBody: FormBody? = null
        if (formBuild != null) {
            formBody = formBuild.build()
        }
        val request =
            Request.Builder().url(url).method(httpMethod, formBody).build()
        return callFactory!!.newCall(request)
    }

    class Builder {



        internal val myRetrofit: MyRetrofit
        private var methodAnnotations: Array<Annotation>
        private var parameterAnnotations: Array<Array<Annotation>>
        internal lateinit var parameterHandler: Array<ParameterHandler?>
        internal var httpMethod: String? = null
        internal var relativeUrl: String? = null
        internal var hasBody = false

        constructor(myRetrofit:MyRetrofit,method: Method){
            this.myRetrofit =myRetrofit
            this.methodAnnotations =method.annotations
            this.parameterAnnotations=method.parameterAnnotations
        }



        fun build(): ServiceMethod {
            /**
             * 1 解析方法上的注解, 只处理POST与GET
             */
            for (methodAnnotation in methodAnnotations) {
                if (methodAnnotation is POST) {
                    //记录当前请求方式
                    httpMethod = "POST"
                    //记录请求url的path
                    relativeUrl = methodAnnotation.value
                    // 是否有请求体
                    hasBody = true
                } else if (methodAnnotation is GET) {
                    httpMethod = "GET"
                    relativeUrl = methodAnnotation.value
                    hasBody = false
                }
            }
            /**
             * 2 解析方法参数的注解
             */
            val length = parameterAnnotations.size
            parameterHandler = arrayOfNulls(length)
            for (i in 0 until length) {
                // 一个参数上的所有的注解
                val annotations = parameterAnnotations[i]
                // 处理参数上的每一个注解
                for (annotation in annotations) {
                    //todo 可以加一个判断:如果httpMethod是get请求,现在又解析到Filed注解,可以提示使用者使用Query注解
                    if (annotation is Field) {
                        //得到注解上的value: 请求参数的key
                        val value: String = annotation.value
                        parameterHandler[i] = ParameterHandler.FiledParameterHandler(value)
                    } else if (annotation is Query) {
                        val value: String = annotation.value
                        parameterHandler[i] = ParameterHandler.QueryParameterHandler(value)
                    }
                }
            }
            return ServiceMethod(this)
        }

    }

}