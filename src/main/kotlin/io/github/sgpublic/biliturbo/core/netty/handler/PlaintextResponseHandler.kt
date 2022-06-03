package io.github.sgpublic.biliturbo.core.netty.handler

import io.github.sgpublic.biliturbo.core.util.Log
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.DefaultHttpContent
import io.netty.handler.codec.http.DefaultHttpResponse
import io.netty.handler.codec.http.FullHttpRequest
import java.nio.charset.Charset

class PlaintextResponseHandler(
    private val client: Channel
): ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val content = when (msg) {
            is FullHttpRequest -> msg.content().toString(Charset.defaultCharset())
            is DefaultHttpResponse -> msg.toString()
            is DefaultHttpContent -> msg.content().toString(Charset.defaultCharset())
            else -> msg.toString()
        }
        Log.d("[HttpProxyResponseHandler] resolve data: \n$content")
        client.writeAndFlush(msg)
    }
}