package io.github.sgpublic.biliturbo.core.netty.handler

import io.github.sgpublic.biliturbo.core.netty.BiliTurboService
import io.github.sgpublic.biliturbo.core.netty.ProxyHandler
import io.github.sgpublic.biliturbo.core.util.HostPort
import io.github.sgpublic.biliturbo.core.util.addPipelineLast
import io.github.sgpublic.biliturbo.core.util.getAttr
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*

class SocketProxyHandler: ChannelInboundHandlerAdapter(), ProxyHandler {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val request = ctx.getAttr(BiliTurboService.DST_ADDRESS)
        sendToServer(request, ctx, msg)
    }

    override fun sendToServer(address: HostPort, ctx: ChannelHandlerContext, msg: Any) {
        val cf = Bootstrap().group(ctx.channel().eventLoop())
            .channel(ctx.channel().javaClass)
            .handler(object : ChannelInitializer<Channel>() {
                override fun initChannel(ch: Channel) {
                    ch.addPipelineLast(SocksResponseHandler())
                }
            })
            .connect(address.hostName, address.port)
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

    class SocksResponseHandler: ChannelInboundHandlerAdapter() {
        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
            ctx.channel().writeAndFlush(msg)
        }
    }
}