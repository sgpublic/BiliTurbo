package io.github.sgpublic.biliturbo.core.util

fun <R> init(init: () -> R): R {
    return init()
}