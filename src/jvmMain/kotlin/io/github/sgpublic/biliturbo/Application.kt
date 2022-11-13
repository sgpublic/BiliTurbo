// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package io.github.sgpublic.biliturbo

import androidx.compose.ui.window.application
import io.github.sgpublic.biliturbo.core.netty.BiliTurboService
import io.github.sgpublic.biliturbo.view.MainWindow

object Application {
    val DEBUG get() = debug
    private var debug = false

    @JvmStatic
    fun main(args: Array<String>) {
        debug = args.contains("--debug")
        start()
    }

    @JvmStatic
    fun start() {
        application {
            MainWindow(
                onCloseRequest = {
                    BiliTurboService.stop()
                    exitApplication()
                }
            )
        }
    }
}

