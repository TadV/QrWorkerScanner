package com.edurda77.qrworker.ui

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.edurda77.qrworker.R
import com.edurda77.qrworker.ui.uikit.scannerQR
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun AuthorisationScreen(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    event: (MainEvent) -> Unit,
) {
    val isEnabledCamera = remember {
        mutableStateOf(false)
    }
    val qrCodeBounds = remember { mutableStateOf<Rect?>(null) }
    val window = LocalView.current
    window.keepScreenOn = true
    if (isEnabledCamera.value) {
        AndroidView(
            factory = { context ->
                scannerQR(
                    context = context,
                    lifecycleOwner = lifecycleOwner,
                    cameraProviderFuture = cameraProviderFuture
                ) { result ->
                    if (result.length == 13) {
                        isEnabledCamera.value = false
                        window.keepScreenOn = false
                    }
                    event(MainEvent.TryAuthorization(result))
                }
            },
            modifier = modifier.fillMaxSize()
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            Button(
                modifier = modifier
                    .align(alignment = Alignment.Center)
                    .padding(10.dp),
                onClick = {
                isEnabledCamera.value = true
            }) {
                Text(
                    text = stringResource(R.string.autorisation),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
            }
        }
    }
}