package com.edurda77.qrworker.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.qrworker.R
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.domain.utils.selectDate
import com.edurda77.qrworker.ui.theme.black
import com.edurda77.qrworker.ui.theme.grey
import com.edurda77.qrworker.ui.theme.lightBlue
import com.edurda77.qrworker.ui.theme.lightGrey
import com.edurda77.qrworker.ui.uikit.ItemTechOperation
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun WorkScreen(
    modifier: Modifier = Modifier,
    techOperations: List<TechOperation>,
    query: String,
    user: String,
    message: String,
    workState: WorkState,
    event: (MainEvent) -> Unit,
) {
    BackHandler {
        event(MainEvent.ChangeAppState(AppState.WorkScan(WorkState.ReadyScanState)))
    }
    val context = LocalContext.current
    val barcodeLauncher = rememberLauncherForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG)
                .show()
        } else {
            Log.d("test view model", "screen code ${result.contents}")
            event(MainEvent.ScanOpzs(result.contents))
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
            LaunchedEffect(message) {
                if (message.isNotBlank()) {
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        WorkState.ReadyScanState -> {
            LaunchedEffect(message) {
                if (message.isNotBlank()) {
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            Scaffold(
                modifier = modifier
                    .fillMaxSize(),
                containerColor = lightBlue,
                floatingActionButton = {

                }
            ) { paddings ->
                Column(
                    modifier = modifier
                        .padding(paddings)
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            val orderNumber = if (techOperations.isNotEmpty()) techOperations.first().orderNumber else ""
                            Text(
                                text = "${stringResource(R.string.order)} $orderNumber",
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight(700),
                                    color = black,
                                )
                            )
                            Spacer(modifier = modifier.height(10.dp))
                            if (techOperations.isNotEmpty()) {
                                Row(
                                    modifier = modifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                    //horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Text(
                                        text = techOperations.first().productionDivision,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(500),
                                            color = black,
                                        )
                                    )
                                    Spacer(modifier = modifier.width(5.dp))
                                    Text(
                                        text = techOperations.first().orderData.selectDate(),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(500),
                                            color = grey,
                                        )
                                    )
                                }
                            }
                        }
                        IconButton(
                            modifier = modifier.size(70.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = lightGrey
                            ),
                            onClick = {
                                event(MainEvent.ChangeAppState(AppState.WorkScan(WorkState.ProcessScannerState)))
                            }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_qr_code_scanner_24),
                                contentDescription = "",
                                tint = black
                            )
                        }
                        /*Button(onClick = {
                            event(MainEvent.ChangeAppState(AppState.WorkScan(WorkState.ProcessScannerState)))
                        }) {
                            Text(text = stringResource(R.string.qr_scan))
                        }
                        Button(onClick = {
                            event(MainEvent.UploadForce)
                        }) {
                            Text(text = stringResource(R.string.upload_data))
                        }*/
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            color = black,
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_search_24),
                                contentDescription = "",
                                tint = black
                            )
                        },
                        value = query,
                        onValueChange = {
                            event(MainEvent.OnSearch(it))
                        }
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    LazyColumn(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(techOperations) { operation ->
                            ItemTechOperation(
                                user = user,
                                techOperation = operation,
                                event = event
                            )
                        }
                    }
                }
            }
        }
    }
}