package io.github.sgpublic.biliturbo.core.netty.handler

import io.github.sgpublic.biliturbo.core.netty.BiliTurboService
import io.github.sgpublic.biliturbo.core.netty.ProxyHandler
import io.github.sgpublic.biliturbo.core.util.addPipelineLast
import io.github.sgpublic.biliturbo.core.util.dstAddress
import io.github.sgpublic.biliturbo.core.util.getAttrOrDefault
import io.github.sgpublic.biliturbo.core.util.removePipeline
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.handler.codec.http.*
import io.netty.util.ReferenceCountUtil
import java.net.InetSocketAddress

class HttpProxyHandler: ChannelInboundHandlerAdapter(), ProxyHandler {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is HttpRequest) {
            ctx.fireChannelRead(msg)
            return
        }
        val request = ctx.getAttrOrDefault(BiliTurboService.DST_ADDRESS, msg.dstAddress())
        if (msg.method() == HttpMethod.CONNECT) {
            ctx.writeAndFlush(DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus(200, "Connection established")
            ))
            ctx.channel().removePipeline(
                HttpRequestDecoder::class.java,
                HttpResponseEncoder::class.java,
                HttpObjectAggregator::class.java
            )
            ReferenceCountUtil.release(msg)
            return
        }
        if (ctx.getAttrOrDefault(BiliTurboService.IS_HTTPS, false)) {
            ctx.fireChannelRead(msg)
        } else {
            sendToServer(request, ctx, msg)
        }
    }

    override fun sendToServer(request: InetSocketAddress, ctx: ChannelHandlerContext, msg: Any) {
        val bootstrap = Bootstrap()
        bootstrap.group(ctx.channel().eventLoop())
            .channel(ctx.channel().javaClass)
            .handler(object : ChannelInitializer<Channel>() {
                override fun initChannel(ch: Channel) {
                    ch.addPipelineLast(
                        HttpRequestEncoder(),
                        HttpResponseDecoder(),
                        HttpObjectAggregator(6553600),
                        PlaintextResponseHandler(ctx.channel()),
                    )
                }
            })
        val cf = bootstrap.connect(request.hostName, request.port)
        cf.addListener(object : ChannelFutureListener {
            override fun operationComplete(future: ChannelFuture) {
                if (future.isSuccess) {
                    future.channel().writeAndFlush(msg)
                } else {
                    ctx.channel().close()
                }
            }
        })
    }
}