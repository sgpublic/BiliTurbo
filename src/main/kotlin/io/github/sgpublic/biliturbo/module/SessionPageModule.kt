package io.github.sgpublic.biliturbo.module

import io.github.sgpublic.biliturbo.base.BiliTurboProxyModule
import io.netty.bootstrap.Bootstrap
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpRequest
import java.util.regex.Pattern

class SessionPageModule: BiliTurboProxyModule() {
    override fun match(request: HttpRequest): Boolean {
        return pattern.matcher(request.uri()).matches()
    }

    override fun handleResponse(request: HttpRequest, response: FullHttpResponse): FullHttpResponse {
        if (response.status().code() != 404) {
            return response
        }
        val bootstrap = Bootstrap()

        return response
    }

    companion object {
        private val pattern = Pattern.compile("/bangumi/play/ss\\d+")
    }
}