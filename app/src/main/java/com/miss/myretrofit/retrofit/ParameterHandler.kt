package com.miss.myretrofit.retrofit

/**
 *   Created by Vola on 2020/9/1.
 */
abstract class ParameterHandler {

    abstract fun apply(serviceMethod: ServiceMethod, value: String)


    internal class QueryParameterHandler(var key: String): ParameterHandler() {

        override fun apply(serviceMethod: ServiceMethod, value: String) {
            TODO("Not yet implemented")
        }
    }

    class FiledParameterHandler(var key: String) :
        ParameterHandler() {

        override fun apply(serviceMethod: ServiceMethod, value: String) {
            TODO("Not yet implemented")
        }
    }




}