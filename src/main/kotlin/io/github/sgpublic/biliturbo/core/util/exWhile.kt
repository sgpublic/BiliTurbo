package io.github.sgpublic.biliturbo.core.util

fun <R> exWhile(ref: () -> R, check: (R) -> Boolean, action: (R) -> Unit) {
    var value: R = ref()
    while (check(value)) {
        action(value)
        value = ref()
    }
}