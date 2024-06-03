package com.edurda77.qrworker.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.qrworker.R
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


@Composable
fun AuthorisationScreenZxing(
    modifier: Modifier = Modifier,
    event: (MainEvent) -> Unit,
    authorizationState: AuthorizationState,
) {
    val context = LocalContext.current
    val barcodeLauncher = rememberLauncherForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show()
        } else {
            Log.d("test scan", "Scanned: ${result.contents}")
            Toast.makeText(
                context,
                context.getString(R.string.scanned) + result.contents,
                Toast.LENGTH_LONG
            ).show()
            if (result.contents.length == 13) {
                event(MainEvent.TryAuthorization(result.contents))
            }
        }
    }
    when (authorizationState) {
        AuthorizationState.EnterState -> {
            Box(modifier = modifier.fillMaxSize()) {
                Button(
                    modifier = modifier
                        .align(alignment = Alignment.Center)
                        .padding(10.dp),
                    onClick = {
                        event(MainEvent.ChangeAppState(AppState.Authorization(AuthorizationState.ScannerState)))
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
        AuthorizationState.ScannerState -> {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            options.setPrompt(stringResource(R.string.scan_pass))
            options.setCameraId(0)
            options.setBeepEnabled(false)
            options.setBarcodeImageEnabled(true)
            barcodeLauncher.launch(options)
        }
    }
}