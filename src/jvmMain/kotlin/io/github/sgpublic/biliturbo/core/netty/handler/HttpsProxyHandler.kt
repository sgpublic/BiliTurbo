package io.github.sgpublic.biliturbo.core.netty.handler

import io.github.sgpublic.biliturbo.core.netty.BiliTurboService
import io.github.sgpublic.biliturbo.core.netty.ProxyHandler
import io.github.sgpublic.biliturbo.core.util.*
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.*

class HttpsProxyHandler: ChannelInboundHandlerAdapter(), ProxyHandler {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val request = ctx.getAttr(BiliTurboService.DST_ADDRESS)
        if (msg is HttpRequest) {
            sendToServer(request, ctx, msg)
        } else if (msg is ByteBuf && msg.getByte(0).toInt() == 22) {
            sendToClient(request, ctx, msg)
        }
    }

    override fun sendToServer(address: HostPort, ctx: ChannelHandlerContext, msg: Any) {
        val client = ctx.channel()
        val bootstrap = Bootstrap()
        bootstrap.group(NioEventLoopGroup())
            .channel(NioSocketChannel::class.java)
            .handler(object : ChannelInitializer<Channel>() {
                override fun initChannel(ch: Channel) {
                    ch.addPipelineLast(
                        SslSupport.newClientSslHandler(ch.alloc(), address),
                        HttpClientCodec(),
                        HttpObjectAggregator(6553600),
                        PlaintextResponseHandler(msg as HttpRequest, client)
                    )
                }
            })
        val cf = bootstrap.connect(address.hostName, address.port)
        cf.addListener(object : ChannelFutureListener {
            override fun operationComplete(future: ChannelFuture) {
                if (!future.isSuccess) {
                    return
                }
                future.channel().writeAndFlush(msg)
            }
        })
    }

    override fun sendToClient(address: HostPort, ctx: ChannelHandlerContext, msg: Any) {
        ctx.addPipelineFirst(
            HttpRequestDecoder(),
            HttpResponseEncoder(),
            SslSupport.newServerSslHandler(ctx.alloc(), address.hostName),
        )
        ctx.addPipelineLast(
            HttpObjectAggregator(65536),
        )
        ctx.pipeline().fireChannelRead(msg)
        ctx.setAttr(BiliTurboService.IS_HTTPS, true)
    }
}