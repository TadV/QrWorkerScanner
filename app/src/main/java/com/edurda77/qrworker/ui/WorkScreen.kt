package com.edurda77.qrworker.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.edurda77.qrworker.R
import com.edurda77.qrworker.domain.model.QrCode
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun WorkScreen(
    modifier: Modifier = Modifier,
    qrCodes: List<QrCode>,
    message: String,
    workState: WorkState,
    event: (MainEvent) -> Unit,
) {
    val context = LocalContext.current
    val barcodeLauncher = rememberLauncherForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
         Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG)
                .show()
        } else {
            Log.d("test view model", "screen code ${result.contents}")
            event(MainEvent.AddNewQrCode(result.contents))
        }
    }
    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            Toast.makeText(
                context,
                context.getString(R.string.error) + message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    when (workState) {
        WorkState.ProcessScannerState -> {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setPrompt(stringResource(R.string.scan_qr))
            options.setCameraId(0)
            options.setBeepEnabled(false)
            options.setBarcodeImageEnabled(true)
            barcodeLauncher.launch(options)
        }

        WorkState.ReadyScanState -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    event(MainEvent.ChangeAppState(AppState.WorkScan(WorkState.ProcessScannerState)))
                }) {
                    Text(text = stringResource(R.string.qr_scan))
                }
                Spacer(modifier = modifier.height(10.dp))
                LazyColumn(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(qrCodes) { qrCode ->
                        Card(
                            modifier = modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Gray
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 20.dp
                            ),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Column(
                                modifier = modifier.padding(5.dp)
                            ) {
                                Text(text = qrCode.codeUser)
                                Spacer(modifier = modifier.height(5.dp))
                                Text(text = qrCode.codeQr)
                                Spacer(modifier = modifier.height(5.dp))
                                Text(text = qrCode.timeOfScan)
                            }
                        }
                    }
                }
            }
        }
    }
}