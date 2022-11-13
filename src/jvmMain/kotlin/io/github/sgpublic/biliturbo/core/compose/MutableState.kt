package io.github.sgpublic.biliturbo.core.compose

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import io.github.sgpublic.biliturbo.view.MainWindow
import kotlin.reflect.KProperty

operator fun <T> SnapshotStateList<T>.getValue(
    mainWindow: MainWindow, property: KProperty<*>
): SnapshotStateList<T> = this



operator fun <K, V> SnapshotStateMap<K, V>.getValue(
    mainWindow: MainWindow, property: KProperty<*>
): MutableMap<K, V> = this