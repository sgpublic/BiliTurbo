package io.github.sgpublic.biliturbo.core.netty.handler

import io.github.sgpublic.biliturbo.module.ApiModule
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpRequest

class PlaintextResponseHandler(
    private val request: HttpRequest,
    private val client: Channel
): ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val resp = if (msg is FullHttpResponse) {
            ApiModule.getInstance()
                .getTurboResponse(request, msg)
        } else {
            msg
        }
        client.writeAndFlush(resp)
    }
}