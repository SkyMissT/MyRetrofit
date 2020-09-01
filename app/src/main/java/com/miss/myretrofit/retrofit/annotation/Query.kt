package com.miss.myretrofit.retrofit.annotation

/**
 *   Created by Vola on 2020/9/1.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Query(val value: String)
