package io.github.sgpublic.biliturbo.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import io.github.sgpublic.biliturbo.core.netty.BiliTurboService
import io.github.sgpublic.biliturbo.core.util.SslSupport
import io.github.sgpublic.biliturbo.module.ApiModule
import io.github.sgpublic.biliturbo.module.SessionPageModule
import java.math.BigInteger

class MainWindow {
    init {
        ApiModule.init(
            SessionPageModule()
        )
    }

    var crtErr by mutableStateOf<Exception?>(null)
    var turbo by mutableStateOf(false)

    val excepts = mutableStateMapOf<BigInteger, Exception>()
}

@Composable
@Preview
fun ApplicationScope.MainWindow(onCloseRequest: () -> Unit) {
    @Suppress("LocalVariableName")
    val ViewModel by remember { mutableStateOf(MainWindow()) }
    val icon = painterResource("drawable/ic_launcher.png")
    Tray(
        icon = icon,
        menu = {
            CheckboxItem(
                text = "Start turbo",
                checked = ViewModel.turbo,
                onCheckedChange = {
                    ViewModel.turbo = it
                    if (it) {
                        BiliTurboService.start(
                            object : BiliTurboService.Callback {
                                override fun onStart() {
                                    ViewModel.turbo = true
                                }

                                override fun onStop() {
                                    ViewModel.turbo = false
                                }

                                override fun onException(e: Exception) {
                                    when (e) {
                                        is SslSupport.Exception -> {
                                            ViewModel.crtErr = e
                                        }
                                        else -> {
                                            ViewModel.excepts[ApiModule.RANDOM_TS] = e
                                        }
                                    }
                                }
                            }
                        )
                    } else {
                        BiliTurboService.stop()
                    }
                }
            )
            Item(
                text = "Exit",
                onClick = {
                    onCloseRequest()
                }
            )
        }
    )
    if (ViewModel.crtErr != null) {
        CrtAlertDialog(
            icon = icon,
            exception = ViewModel.crtErr!!,
            onCloseRequest = {
                ViewModel.crtErr = null
            }
        )
    }
    if (ViewModel.excepts.isNotEmpty()) {
        ExceptionDialog(
            icon = icon,
            exception = ViewModel.excepts.values,
            onCloseRequest = {
                ViewModel.excepts.clear()
            }
        )
    }
}