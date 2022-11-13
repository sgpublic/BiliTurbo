package io.github.sgpublic.biliturbo.core.util

import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest

fun HttpRequest.dstAddress(): HostPort {
    val arr = headers().getAsString("Host").split(":")
    val host: String = arr[0]
    var port = 80
    if (arr.size > 1) {
        port = arr[1].toInt()
    } else if (uri().startsWith("https")) {
        port = 443
    }

    return HostPort(host, port)
}

data class HostPort(
    val hostName: String,
    val port: Int,
) {
    override fun toString(): String {
        return "$hostName:$port"
    }
}

fun HttpRequest.getPath(): String {
    val uri = uri()
    return uri.takeIf { !it.contains("?") }
        ?: uri.split("?")[0]
}

fun FullHttpRequest.getContent(): String {
    return if (method() == HttpMethod.POST) {
        this.content().toString(Charsets.UTF_8)
    } else { "" }
}