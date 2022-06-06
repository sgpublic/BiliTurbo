package io.github.sgpublic.biliturbo.core.util

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.Marker
import org.slf4j.MarkerFactory
import kotlin.system.exitProcess

/**
 * 自定义日志输出封装
 */
object Log {
    @JvmStatic
    private val logger: Logger get() {
        return LoggerFactory.getLogger(Class.forName(Throwable().stackTrace[2].className))
    }

    /**
     * 生成日志标记
     * @param ch 产生日志的 Channel
     */
    @JvmStatic
    private fun marker(ch: Channel): Marker {
        return MarkerFactory.getMarker(ch.id().asShortText())
    }

    /**
     * 生成日志标记
     * @param ctx 产生日志的 ChannelHandlerContext
     */
    @JvmStatic
    private fun marker(ctx: ChannelHandlerContext): Marker {
        return marker(ctx.channel())
    }

    /**
     * 普通 TRACE 日志
     * @param message 日志信息
     */
    @JvmStatic
    fun t(message: Any){
        logger.trace(message.toString())
    }

    /**
     * 附带 Channel 信息的 TRACE 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     */
    @JvmStatic
    fun t(ch: Channel, message: Any){
        logger.trace(marker(ch), message.toString())
    }

    /**
     * 附带 Channel 信息的 TRACE 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     */
    @JvmStatic
    fun t(ctx: ChannelHandlerContext, message: Any){
        logger.trace(marker(ctx), message.toString())
    }

    /**
     * 附带 Channel 信息的 DEBUG 日志
     * @param message 日志信息
     */
    @JvmStatic
    fun d(message: Any){
        logger.debug(message.toString())
    }

    /**
     * 附带 Throwable 的 DEBUG 日志
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun d(message: Any, throwable: Throwable){
        logger.debug(message.toString(), throwable)
    }

    /**
     * 附带 Channel 信息的 DEBUG 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     */
    @JvmStatic
    fun d(ch: Channel, message: Any){
        logger.debug(marker(ch), message.toString())
    }

    /**
     * 附带 Channel 信息、Throwable 的 DEBUG 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun d(ch: Channel, message: Any, throwable: Throwable){
        logger.debug(marker(ch), message.toString(), throwable)
    }

    /**
     * 附带 Channel 信息的 DEBUG 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     */
    @JvmStatic
    fun d(ctx: ChannelHandlerContext, message: Any){
        logger.debug(marker(ctx), message.toString())
    }

    /**
     * 附带 Channel 信息、Throwable 的 DEBUG 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun d(ctx: ChannelHandlerContext, message: Any, throwable: Throwable){
        logger.debug(marker(ctx), message.toString(), throwable)
    }

    /**
     * 普通 INFO 日志
     * @param message 日志信息
     */
    @JvmStatic
    fun i(message: Any){
        logger.info(message.toString())
    }

    /**
     * 附带 Throwable 的 INFO 日志
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun i(message: Any, throwable: Throwable){
        logger.info(message.toString(), throwable)
    }

    /**
     * 附带 Channel 信息的 INFO 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     */
    @JvmStatic
    fun i(ch: Channel, message: Any){
        logger.info(marker(ch), message.toString())
    }

    /**
     * 附带 Channel 信息、Throwable 的 INFO 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun i(ch: Channel, message: Any, throwable: Throwable){
        logger.info(marker(ch), message.toString(), throwable)
    }

    /**
     * 附带 Channel 信息的 INFO 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     */
    @JvmStatic
    fun i(ctx: ChannelHandlerContext, message: Any){
        logger.info(marker(ctx), message.toString())
    }

    /**
     * 附带 Channel 信息、Throwable 的 INFO 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun i(ctx: ChannelHandlerContext, message: Any, throwable: Throwable){
        logger.info(marker(ctx), message.toString(), throwable)
    }

    /**
     * 普通 WARN 日志
     * @param message 日志信息
     */
    @JvmStatic
    fun w(message: Any){
        logger.warn(message.toString())
    }

    /**
     * 附带 Throwable 的 WARN 日志
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun w(message: Any, throwable: Throwable){
        logger.warn(message.toString(), throwable)
    }

    /**
     * 附带 Channel 信息的 WARN 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     */
    @JvmStatic
    fun w(ch: Channel, message: Any){
        logger.warn(marker(ch), message.toString())
    }

    /**
     * 附带 Channel 信息、Throwable 的 WARN 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun w(ch: Channel, message: Any, throwable: Throwable){
        logger.warn(marker(ch), message.toString(), throwable)
    }

    /**
     * 附带 Channel 信息的 WARN 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     */
    @JvmStatic
    fun w(ctx: ChannelHandlerContext, message: Any){
        logger.warn(marker(ctx), message.toString())
    }

    /**
     * 附带 Channel 信息、Throwable 的 WARN 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun w(ctx: ChannelHandlerContext, message: Any, throwable: Throwable){
        logger.warn(marker(ctx), message.toString(), throwable)
    }

    /**
     * 普通 ERROR 日志
     * @param message 日志信息
     */
    @JvmStatic
    fun e(message: Any){
        logger.error(message.toString())
    }

    /**
     * 附带 Throwable 的 ERROR 日志
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun e(message: Any, throwable: Throwable){
        logger.error(message.toString(), throwable)
    }

    /**
     * 普通 ERROR 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     */
    @JvmStatic
    fun e(ch: Channel, message: Any){
        logger.error(marker(ch), message.toString())
    }

    /**
     * 附带 Throwable 的 ERROR 日志
     * @param ch 当前 Channel
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun e(ch: Channel, message: Any, throwable: Throwable){
        logger.error(marker(ch), message.toString(), throwable)
    }

    /**
     * 普通 ERROR 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     */
    @JvmStatic
    fun e(ctx: ChannelHandlerContext, message: Any){
        logger.error(marker(ctx), message.toString())
    }

    /**
     * 附带 Throwable 的 ERROR 日志
     * @param ctx 当前 ChannelHandlerContext
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun e(ctx: ChannelHandlerContext, message: Any, throwable: Throwable){
        logger.error(marker(ctx), message.toString(), throwable)
    }

    /**
     * 普通 ERROR 日志，打印后结束进程
     * @param message 日志信息
     */
    @JvmStatic
    fun f(message: Any){
        logger.error(message.toString())
        exitProcess(0)
    }

    /**
     * 附带 Throwable 的 ERROR 日志，打印后结束进程
     * @param message 日志信息
     * @param throwable 日志附带的异常堆栈信息
     */
    @JvmStatic
    fun f(message: Any, throwable: Throwable){
        logger.error(message.toString(), throwable)
        exitProcess(0)
    }
}