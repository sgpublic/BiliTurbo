package io.github.sgpublic.biliturbo.core.logback.converter

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent

class ChannelFileConverter: ClassicConverter() {
    override fun convert(event: ILoggingEvent): String {
        event.marker?.let {
            return it.name
        }
        return ""
    }
}