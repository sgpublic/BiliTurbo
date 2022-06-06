package io.github.sgpublic.biliturbo.view

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import io.github.sgpublic.biliturbo.core.util.Log

@Composable
fun ExceptionDialog(
    icon: Painter,
    exception: Collection<Exception>,
    onCloseRequest: () -> Unit
) {
    Window(
        onCloseRequest = {
            Log.d("ExceptionDialog closing")
            onCloseRequest()
        },
        title = "Something went wrong with BiliTurbo!",
        resizable = false,
        state = rememberWindowState(
            size = DpSize(860.dp, 470.dp),
            // 设置初始位置为屏幕正中
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        icon = icon
    ) {
        MaterialTheme {

        }
    }
}