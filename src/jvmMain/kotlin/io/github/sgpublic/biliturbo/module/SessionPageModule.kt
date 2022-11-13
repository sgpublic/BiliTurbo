package io.github.sgpublic.biliturbo.module

import io.github.sgpublic.biliturbo.base.BiliTurboProxyModule
import io.netty.bootstrap.Bootstrap
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpRequest
import java.util.regex.Pattern

class SessionPageModule: BiliTurboProxyModule() {
    override fun match(request: HttpRequest): Boolean {
        for (pattern in patterns) {
            if (pattern.matcher(request.uri()).matches()) {
                return true
            }
        }
        return false
    }

    override fun handleResponse(request: HttpRequest, response: FullHttpResponse): FullHttpResponse {
        if (response.status().code() != 404) {
            return response
        }
        val bootstrap = Bootstrap()

        return response
    }

    companion object {
        private val patterns = listOf(
            Pattern.compile("api\\.bilibili\\.com/pgc/view/pc/season\\?season_id=\\d+"),
        )
    }
}