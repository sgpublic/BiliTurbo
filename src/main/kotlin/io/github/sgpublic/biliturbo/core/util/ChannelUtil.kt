package io.github.sgpublic.biliturbo.core.util

import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.util.AttributeKey

fun ChannelHandlerContext.removePipeline(vararg names: Class<out ChannelHandler>): ChannelHandlerContext {
    pipeline().also {
        it.remove(*names)
    }
    return this
}

fun ChannelHandlerContext.addPipelineLast(vararg handlers: ChannelHandler): ChannelHandlerContext {
    for (handler: ChannelHandler in handlers) {
        pipeline().addLast(handler.javaClass.simpleName, handler)
    }
    return this
}

fun ChannelHandlerContext.addPipelineFirst(vararg handlers: ChannelHandler): ChannelHandlerContext {
    for (handler: ChannelHandler in handlers) {
        pipeline().addFirst(handler.javaClass.simpleName, handler)
    }
    return this
}

fun ChannelHandlerContext.addPipelineAfter(target: Class<out ChannelHandler>, handler: ChannelHandler): ChannelHandlerContext {
    pipeline().addAfter(target.simpleName, handler.javaClass.simpleName, handler)
    return this
}

fun ChannelHandlerContext.addPipelineBefore(target: Class<out ChannelHandler>, handler: ChannelHandler): ChannelHandlerContext {
    pipeline().addBefore(target.simpleName, handler.javaClass.simpleName, handler)
    return this
}

fun Channel.removePipeline(vararg names: Class<out ChannelHandler>): Channel {
    pipeline().also {
        it.remove(*names)
    }
    return this
}

fun Channel.addPipelineLast(vararg handlers: ChannelHandler): Channel {
    for (handler: ChannelHandler in handlers) {
        pipeline().addLast(handler.javaClass.simpleName, handler)
    }
    return this
}

fun Channel.addPipelineFirst(vararg handlers: ChannelHandler): Channel {
    for (handler: ChannelHandler in handlers) {
        pipeline().addFirst(handler.javaClass.simpleName, handler)
    }
    return this
}

fun Channel.addPipelineAfter(target: Class<out ChannelHandler>, handler: ChannelHandler): Channel {
    pipeline().addAfter(target.simpleName, handler.javaClass.simpleName, handler)
    return this
}

fun Channel.addPipelineBefore(target: Class<out ChannelHandler>, handler: ChannelHandler): Channel {
    pipeline().addBefore(target.simpleName, handler.javaClass.simpleName, handler)
    return this
}

fun ChannelPipeline.remove(vararg names: Class<out ChannelHandler>): ChannelPipeline {
    for (name: Class<out ChannelHandler> in names) {
        remove(name.simpleName)
    }
    return this
}

fun ChannelPipeline.addLast(vararg handlers: ChannelHandler): ChannelPipeline {
    for (handler: ChannelHandler in handlers) {
        addLast(handler.javaClass.simpleName, handler)
    }
    return this
}

fun ChannelPipeline.addFirst(vararg handlers: ChannelHandler): ChannelPipeline {
    for (handler: ChannelHandler in handlers) {
        addFirst(handler.javaClass.simpleName, handler)
    }
    return this
}

fun ChannelPipeline.addAfter(target: Class<out ChannelHandler>, handler: ChannelHandler): ChannelPipeline {
    addAfter(target.simpleName, handler.javaClass.simpleName, handler)
    return this
}

fun ChannelPipeline.addBefore(target: Class<out ChannelHandler>, handler: ChannelHandler): ChannelPipeline {
    addBefore(target.simpleName, handler.javaClass.simpleName, handler)
    return this
}

fun <T> Channel.setAttrIfAbsent(key: AttributeKey<T>, value: T) {
    attr(key).setIfAbsent(value)
}

fun <T> Channel.setAttr(key: AttributeKey<T>, value: T) {
    attr(key).set(value)
}

fun <T> Channel.getAttr(key: AttributeKey<T>): T {
    return attr(key).get()
}

fun <T> Channel.getAttrOrDefault(key: AttributeKey<T>, default: T): T {
    if (!hasAttr(key) && default != null) {
        setAttrIfAbsent(key, default)
    }
    return default.let { return@let getAttr(key) }
}

fun <T> ChannelHandlerContext.setAttrIfAbsent(key: AttributeKey<T>, value: T) {
    channel().setAttrIfAbsent(key, value)
}

fun <T> ChannelHandlerContext.setAttr(key: AttributeKey<T>, value: T) {
    channel().setAttr(key, value)
}

fun <T> ChannelHandlerContext.getAttr(key: AttributeKey<T>): T {
    return channel().getAttr(key)
}

fun <T> ChannelHandlerContext.getAttrOrDefault(key: AttributeKey<T>, default: T): T {
    return channel().getAttrOrDefault(key, default)
}