package com.edurda77.qrworker.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.window.Dialog
import com.edurda77.qrworker.R
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.domain.utils.selectDate
import com.edurda77.qrworker.ui.theme.black
import com.edurda77.qrworker.ui.theme.blue
import com.edurda77.qrworker.ui.theme.grey
import com.edurda77.qrworker.ui.theme.lightBlue
import com.edurda77.qrworker.ui.theme.lightGrey
import com.edurda77.qrworker.ui.theme.white
import com.edurda77.qrworker.ui.uikit.DialogApprovedOperations
import com.edurda77.qrworker.ui.uikit.FilterContent
import com.edurda77.qrworker.ui.uikit.ItemTechOperation
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkScreen(
    modifier: Modifier = Modifier,
    techOperations: List<TechOperation>,
    approvedTechOperations: List<TechOperation>,
    conflictTechOperations: List<TechOperation>,
    filtersQueries: List<String>,
    user: String,
    message: String,
    workState: WorkState,
    isConflict:Boolean,
    event: (MainEvent) -> Unit,
) {
    BackHandler {
        event(MainEvent.ChangeAppState(AppState.WorkScan(WorkState.ReadyScanState)))
    }
    var query = remember { mutableStateOf("") }
    val isExpanded = remember { mutableStateOf(false) }
    val isExpandedApproved = remember { mutableStateOf(false) }
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
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

    if (isExpanded.value&&isConflict) {
        Dialog(
            onDismissRequest = { isExpanded.value = false }) {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(conflictTechOperations) { operation ->
                    ItemTechOperation(
                        user = user,
                        techOperation = operation,
                        event = event
                    )
                }
            }
        }
    }

    if (isExpandedApproved.value) {
        Dialog(onDismissRequest = {
            isExpandedApproved.value = false
        }) {
            DialogApprovedOperations(approvedTechOperations = approvedTechOperations)
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
           /* LaunchedEffect(message) {
                if (message.isNotBlank()) {
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }*/
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
                bottomBar = {
                    Row(
                        modifier = modifier.fillMaxWidth().padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            modifier = modifier.weight(5f),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = blue.copy(alpha = 0.5f),
                                containerColor = blue
                            ),
                            enabled = techOperations.isNotEmpty(),
                            contentPadding = PaddingValues(vertical = 10.dp),
                            shape = RoundedCornerShape(15.dp),
                            onClick = {
                                event(MainEvent.UploadSelectedTechOperations)
                            }) {
                            Text(
                                text = stringResource(R.string.confirm),
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight(700),
                                    color = white,
                                )
                            )
                        }
//                        Spacer(modifier = modifier.width(5.dp))
//                        IconButton(
//                            modifier = modifier.size(60.dp),
//                            colors = IconButtonDefaults.iconButtonColors(
//                                containerColor = lightGrey
//                            ),
//                            onClick = {
//                                showBottomSheet.value = true
//                            }) {
//                            Icon(
//                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_alt_24),
//                                contentDescription = "",
//                                tint = black
//                            )
//                        }
                    }
                }
            ) { paddings ->
                if (showBottomSheet.value) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet.value = false
                        },
                        sheetState = sheetState
                    ) {
                        FilterContent(
                            filtersQueries = filtersQueries,
                            showBottomSheet = showBottomSheet,
                            sheetState = sheetState,
                            event = event
                        )
                    }
                }
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
                        Column(
                            modifier = modifier.weight(1f),
                        ) {
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
                                Text(
                                    text = techOperations.first().productionDivision,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(500),
                                        color = black,
                                    )
                                )
                                Spacer(modifier = modifier.height(5.dp))
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
                        value = query.value,
                        onValueChange = {
                            query.value = it
                            event(MainEvent.OnSearch(it))
                        }
                    )
                    /*if (isConflict) {
                        Spacer(modifier = modifier.height(10.dp))
                        IconButton(
                            modifier = modifier,
                            onClick = {
                                isExpanded.value = true
                            }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_error_outline_24),
                                contentDescription = "",
                                tint = red
                            )
                        }
                    }*/
//                    Row(
//                        modifier = modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceAround
//                    ) {
//                        Button(onClick = {
//                            isExpandedApproved.value = true
//                            event(MainEvent.GetApprovedTechOperationPrevDay)
//                        }) {
//                            Text(text = stringResource(R.string.prev_day))
//                        }
//                        Button(onClick = {
//                            isExpandedApproved.value = true
//                            event(MainEvent.GetApprovedTechOperationCurrentMonth)
//                        }) {
//                            Text(text = stringResource(R.string.current_month))
//                        }
//                    }
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