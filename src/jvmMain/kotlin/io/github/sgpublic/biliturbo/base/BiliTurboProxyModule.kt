package io.github.sgpublic.biliturbo.base

import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpRequest

abstract class BiliTurboProxyModule {
    abstract fun match(request: HttpRequest): Boolean
    abstract fun handleResponse(request: HttpRequest, response: FullHttpResponse): FullHttpResponse

    override fun hashCode(): Int {
        return this.javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    class CodeMessage: java.io.Serializable {
        val code: Int = -1
        val message: String = ""
    }
}