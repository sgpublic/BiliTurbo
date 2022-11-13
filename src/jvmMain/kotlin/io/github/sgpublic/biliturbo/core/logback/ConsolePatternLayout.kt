package io.github.sgpublic.biliturbo.core.logback

import ch.qos.logback.classic.PatternLayout
import io.github.sgpublic.biliturbo.core.logback.converter.ChannelConsoleConverter
import io.github.sgpublic.biliturbo.core.logback.converter.ChannelFileConverter
import io.github.sgpublic.biliturbo.core.logback.converter.TraceConverter

/**
 * 添加自定义参数
 */
class ConsolePatternLayout: PatternLayout() {
    companion object {
        init {
            DEFAULT_CONVERTER_MAP["chc"] = ChannelConsoleConverter::class.java.name
            DEFAULT_CONVERTER_MAP["chf"] = ChannelFileConverter::class.java.name
            DEFAULT_CONVERTER_MAP["trace"] = TraceConverter::class.java.name
        }
    }
}