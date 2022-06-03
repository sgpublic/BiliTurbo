package io.github.sgpublic.biliturbo.core.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
     * 普通 TRACE 日志
     * @param message 日志信息
     */
    @JvmStatic
    fun t(message: Any){
        logger.trace(message.toString())
    }

    /**
     * 普通 DEBUG 日志
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
     * 普通 INFO 日志
     * @param message 日志信息
     */
    @JvmStatic
    fun i(message: Any){
        logger.info(message.toString())
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