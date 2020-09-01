package com.miss.myretrofit.retrofit.annotation

/**
 *   Created by Vola on 2020/9/1.
 */
@Target(AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(val value: String)
