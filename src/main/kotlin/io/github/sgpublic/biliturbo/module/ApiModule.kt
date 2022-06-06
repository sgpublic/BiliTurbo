package io.github.sgpublic.biliturbo.module

import io.github.sgpublic.biliturbo.base.BiliTurboProxyModule
import io.github.sgpublic.biliturbo.core.util.Log
import io.github.sgpublic.biliturbo.core.util.dstAddress
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpResponse
import java.math.BigInteger
import kotlin.random.Random

class ApiModule private constructor(
    private val actions: LinkedHashSet<BiliTurboProxyModule>
) {
    companion object {
        val TS: Long get() = System.currentTimeMillis() / 1000
        val TS_STR: String get() = (System.currentTimeMillis() / 1000).toString()
        val TS_FULL: Long get() = System.currentTimeMillis()
        val TS_FULL_STR: String get() = System.currentTimeMillis().toString()
        val RANDOM_TS: BigInteger get() = BigInteger(System.currentTimeMillis().toString() +
                Random.nextInt(1000, 10000))

        private val actions = linkedSetOf<BiliTurboProxyModule>()
        fun getInstance(): ApiModule {
            return ApiModule(actions)
        }

        fun init(vararg actions: BiliTurboProxyModule) {
            ApiModule.actions.clear()
            ApiModule.actions.addAll(actions)
        }
    }

    fun getTurboResponse(request: HttpRequest, response: FullHttpResponse): HttpResponse {
        val hostName = request.dstAddress().hostName
        if (!hostName.contains("bilibili.com")) {
            return response
        }
        Log.d("${request.method()} $hostName${request.uri()}")
        var resp: FullHttpResponse = response
        for (module in actions) {
            if (!module.match(request)) {
                continue
            }
            resp = module.handleResponse(request, response)
            break
        }
        return resp
    }
}