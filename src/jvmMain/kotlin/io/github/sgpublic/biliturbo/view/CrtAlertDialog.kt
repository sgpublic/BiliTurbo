package io.github.sgpublic.biliturbo.view

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import io.github.sgpublic.biliturbo.core.util.Log
import io.github.sgpublic.biliturbo.core.util.SslSupport
import java.io.File

class CrtAlertDialogState {
    var successShow by mutableStateOf<File?>(null)
    var exception by mutableStateOf<Exception?>(null)
}

@Composable
fun CrtAlertDialog(
    icon: Painter,
    exception: Exception,
    onCloseRequest: () -> Unit
) {
    val ViewModule by remember { mutableStateOf(CrtAlertDialogState()) }
    Window(
        onCloseRequest = onCloseRequest,
        title = "BiliTurbo requires a certificate file to start",
        resizable = false,
        state = rememberWindowState(
            size = DpSize(600.dp, 380.dp),
            // 设置初始位置为屏幕正中
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        icon = icon,
    ) {
        MaterialTheme {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(60.dp, 20.dp, 60.dp, 10.dp).weight(1f),
                    ) {
                        val scroll = rememberLazyListState()
                        LazyColumn(
                            state = scroll,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {
                                Text(
                                    text = "BiliTurbo failed to load the certificate file, you need to put the CA certificate file " +
                                            "(certificate.crt) and private key file (private.der) into the cert folder of the " +
                                            "current running directory!"
                                )
                            }
                            item {
                                StackTraceText(
                                    exception = exception
                                )
                            }
                        }
                        VerticalScrollbar(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            adapter = rememberScrollbarAdapter(scroll)
                        )
                    }
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            try {
                                ViewModule.successShow = SslSupport.createCert()
                            } catch (e: java.lang.Exception) {
                                Log.e("Certificate generation error", e)
                                ViewModule.exception = e
                            }
                        },
                    ) {
                        Text("No certificate file? Auto-generate now!")
                    }
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            onCloseRequest.invoke()
                        }
                    ) {
                        Text("I want to generate manually.")
                    }
                }
                if (ViewModule.successShow != null) {
                    Card(
                        modifier = Modifier.width(300.dp)
                            .wrapContentHeight()
                            .align(Alignment.Center),
                        elevation = 5.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp, 10.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text("The certificate is successfully generated, please import the generated " +
                                    "certificate into the \"Trusted Root Certification Authorities\" store of the system.")
                            Button(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onClick = {
                                    Runtime.getRuntime().exec("explorer.exe /select, ${ViewModule.successShow!!.absoluteFile}")
                                    ViewModule.successShow = null
                                    onCloseRequest.invoke()
                                }
                            ) {
                                Text("Open in explorer")
                            }
                        }
                    }
                } else if (ViewModule.exception != null) {
                    Card(
                        modifier = Modifier.width(440.dp)
                            .heightIn(max = 300.dp)
                            .align(Alignment.Center),
                        elevation = 5.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp, 10.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                val scroll = rememberLazyListState()
                                LazyColumn(
                                    state = scroll,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    item {
                                        Text("Certificate generation error")
                                    }
                                    item {
                                        StackTraceText(
                                            exception = ViewModule.exception
                                        )
                                    }
                                }
                                VerticalScrollbar(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    adapter = rememberScrollbarAdapter(scroll)
                                )
                            }
                            Button(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onClick = {
                                    ViewModule.exception = null
                                },
                            ) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        }
    }
}