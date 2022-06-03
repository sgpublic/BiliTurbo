package io.github.sgpublic.biliturbo.core.netty

import io.netty.channel.ChannelHandlerContext
import java.net.InetSocketAddress

interface ProxyHandler {
    fun sendToServer(request: InetSocketAddress, ctx: ChannelHandlerContext, msg: Any) { }

    fun sendToClient(request: InetSocketAddress, ctx: ChannelHandlerContext, msg: Any) { }
}