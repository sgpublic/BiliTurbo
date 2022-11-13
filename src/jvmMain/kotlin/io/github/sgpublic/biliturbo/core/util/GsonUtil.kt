package io.github.sgpublic.biliturbo.core.util

import com.google.gson.Gson
import io.netty.handler.codec.http.FullHttpResponse
import okhttp3.Response
import java.io.Serializable
import kotlin.reflect.KClass

private val gson = Gson()

fun Serializable?.toGson(): String {
    return gson.toJson(this)
}

fun <T> Response.jsonBody(clazz: Class<T>): T {
    return gson.fromJson(this.body?.string(), clazz)
}

fun <T> FullHttpResponse.jsonBody(clazz: Class<T>): T {
    return gson.fromJson(this.content()?.toString(), clazz)
}

fun <T: Any> KClass<T>.fromJson(str: String): T {
    return gson.fromJson(str, java)
}