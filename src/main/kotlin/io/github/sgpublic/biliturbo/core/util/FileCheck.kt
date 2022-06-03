package io.github.sgpublic.biliturbo.core.util

import java.io.File

fun File.fileExist(): Boolean {
    return exists() && isFile
}