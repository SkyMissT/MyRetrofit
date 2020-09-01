package com.miss.myretrofit.retrofit.annotation

/**
 *   Created by Vola on 2020/9/1.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(val value: String = "")

