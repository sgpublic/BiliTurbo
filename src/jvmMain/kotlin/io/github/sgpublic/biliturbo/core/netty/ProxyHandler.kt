package io.github.sgpublic.biliturbo.core.netty

import io.github.sgpublic.biliturbo.core.util.HostPort
import io.netty.channel.ChannelHandlerContext

interface ProxyHandler {
    fun sendToServer(address: HostPort, ctx: ChannelHandlerContext, msg: Any) { }

    fun sendToClient(address: HostPort, ctx: ChannelHandlerContext, msg: Any) { }
}