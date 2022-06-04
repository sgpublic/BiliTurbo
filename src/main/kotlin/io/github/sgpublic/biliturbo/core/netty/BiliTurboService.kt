package io.github.sgpublic.biliturbo.core.netty

import io.github.sgpublic.biliturbo.core.netty.handler.HttpProxyHandler
import io.github.sgpublic.biliturbo.core.netty.handler.HttpsProxyHandler
import io.github.sgpublic.biliturbo.core.netty.handler.SocketProxyHandler
import io.github.sgpublic.biliturbo.core.util.Log
import io.github.sgpublic.biliturbo.core.util.addPipelineLast
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.util.AttributeKey
import java.net.InetSocketAddress

class BiliTurboService private constructor(
    private val port: Int,
): Thread() {
    private val boss = NioEventLoopGroup()
    private val worker = NioEventLoopGroup()

    override fun run() {
        try {
            onProxy()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            this.interrupt()
        }
    }

    private fun onProxy() {
        val b = ServerBootstrap()
        b.group(boss, worker)
            .channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 100)
            .handler(LoggingHandler(LogLevel.INFO))
            .childHandler(object : ChannelInitializer<Channel>() {
                override fun initChannel(ch: Channel) {
                    ch.addPipelineLast(
                        HttpRequestDecoder(),
                        HttpResponseEncoder(),
                        HttpObjectAggregator(65536),
                        HttpProxyHandler(),
                        HttpsProxyHandler(),
                        SocketProxyHandler()
                    )
                }
            })

        b.bind(port).sync()
            .channel().closeFuture().sync()
    }

    override fun interrupt() {
        worker.shutdownGracefully()
        boss.shutdownGracefully()
        super.interrupt()
    }

    companion object {
        val DST_ADDRESS: AttributeKey<InetSocketAddress> = AttributeKey.valueOf("CLIENT_REQUEST")
        val IS_HTTPS: AttributeKey<Boolean> = AttributeKey.valueOf("IS_HTTPS")

        private var biliTurboService: BiliTurboService? = null
        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
        private val lock: Object = Object()

        fun start(port: Int = 23333) {
            synchronized(lock) {
                biliTurboService?.let {
                    Log.d("Proxy already started.")
                    return
                }
                biliTurboService = BiliTurboService(port).also {
                    it.start()
                }
                Log.i("Proxy started.")
            }
        }

        fun isStarted() = biliTurboService != null && biliTurboService!!.isAlive

        fun stop() {
            synchronized(lock) {
                if (biliTurboService == null) {
                    Log.d("Proxy already stopped.")
                    return
                }
                biliTurboService?.interrupt()
                biliTurboService = null
                Log.i("Proxy stopped.")
            }
        }
    }
}