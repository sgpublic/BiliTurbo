// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package io.github.sgpublic.biliturbo

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.sgpublic.biliturbo.core.netty.BiliTurboService
import io.github.sgpublic.biliturbo.module.ApiModule
import io.github.sgpublic.biliturbo.module.web.SessionPageModule

object Application {
    val DEBUG get() = debug
    private var debug = false

    @JvmStatic
    fun main(args: Array<String>) {
        debug = args.contains("--debug")
        ApiModule.init(
            SessionPageModule()
        )
        start()
    }

    @JvmStatic
    @Composable
    @Preview
    fun App() {
        var status by remember { mutableStateOf("click button to start.") }

        MaterialTheme {
            Column {
                Text(status)
                Button(onClick = {
                    BiliTurboService.start()
                    status = "started."
                }) {
                    Text("start")
                }
                Button(onClick = {
                    BiliTurboService.stop()
                    status = "stopped."
                }) {
                    Text("stop")
                }
            }
        }
    }

    @JvmStatic
    fun start() {
        application {
            Window(onCloseRequest = {
                BiliTurboService.stop()
                this.exitApplication()
            }) {
                App()
            }
        }
    }
}

