package com.edurda77.qrworker.ui.uikit

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.edurda77.qrworker.ui.QrCodeAnalyzer
import com.google.common.util.concurrent.ListenableFuture


fun scannerQR(
    lifecycleOwner:LifecycleOwner,
    context: Context,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    onClick: (String) -> Unit
): PreviewView {

    val previewView = PreviewView(context)
    val preview = Preview.Builder().build()
    val selector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()
    preview.setSurfaceProvider(previewView.surfaceProvider)
    val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .setOutputImageRotationEnabled(true)
        .build()
    imageAnalysis.setAnalyzer(
        ContextCompat.getMainExecutor(context),
        QrCodeAnalyzer { result ->
            onClick(result)
        }
    )
    try {
        cameraProviderFuture.get().bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            imageAnalysis
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return previewView
}