package io.github.sgpublic.biliturbo.core.util

import java.io.Closeable

fun Closeable.closeQuickly() {
    try {
        close()
    } catch (_: Exception) {}
}