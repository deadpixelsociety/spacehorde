package com.spacehorde.service

fun <T : Any> service(clazz: Class<T>) = ServiceDelegate(clazz)

inline fun <reified T : Any> service() = ServiceDelegate(T::class.java)