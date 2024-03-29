package io.github.sgpublic.biliturbo.core.logback.filter

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.AbstractMatcherFilter
import ch.qos.logback.core.spi.FilterReply
import io.github.sgpublic.biliturbo.Application

/**
 * 控制台输出过滤器
 */
class ConsoleFilter: AbstractMatcherFilter<ILoggingEvent>() {
    companion object {
        @JvmStatic
        private val debug = Level.DEBUG
        @JvmStatic
        private val product = Level.WARN
    }

    /**
     * 若日志来自自己，则根据是否 DEBUG 过滤
     */
    private fun filterOnSelf(event: ILoggingEvent): FilterReply {
        val level = if (Application.DEBUG) debug else product
        return if (event.level.isGreaterOrEqual(level)) {
            FilterReply.NEUTRAL
        } else {
            FilterReply.DENY
        }
    }

    /**
     * 若日志来自外部，则始终按照等级 [Level.WARN] 过滤
     */
    private fun filterOnOther(event: ILoggingEvent): FilterReply {
        return FilterReply.NEUTRAL.takeIf {
            event.level.isGreaterOrEqual(Level.WARN)
        } ?: FilterReply.DENY
    }

    override fun decide(event: ILoggingEvent): FilterReply {
        if (!isStarted) {
            return FilterReply.NEUTRAL
        }
        val from = event.loggerName.let {
            return@let it.startsWith("io.github.illusson")
                    || it.startsWith("io.github.sgpublic")
                    || it.startsWith("io.github.Application")
        }
        return if (from) filterOnSelf(event)
        else filterOnOther(event)
    }
}