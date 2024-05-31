package com.edurda77.qrworker.ui.uikit

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.qrworker.R
import com.edurda77.qrworker.ui.MainEvent
import com.edurda77.qrworker.ui.theme.black
import com.edurda77.qrworker.ui.theme.grey
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterContent(
    modifier: Modifier = Modifier,
    filtersQueries: List<String>,
    event: (MainEvent) -> Unit,
    showBottomSheet: MutableState<Boolean>,
    sheetState: SheetState
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (index in filtersQueries.indices) {
            Log.d("test werty", "index - $index, item ${filtersQueries[index]}")
            if (index!=0) {
                Spacer(modifier = modifier.height(10.dp))
            }
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = black,
                ),
                placeholder = {
                    Text(
                        text = "Введите ${index + 1} условие поиска",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = grey,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        if(filtersQueries.size>1) {
                            event(MainEvent.DeleteItemFromFilterList(index))
                        }
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_close_24),
                            contentDescription = "",
                            tint = black
                        )
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_close_24),
                        contentDescription = "",
                        tint = black
                    )
                },
                value = filtersQueries[index],
                onValueChange = {
                    if (index >= filtersQueries.size-1) {
                        event(MainEvent.AddItemInFilteredList)
                    }
                    event(MainEvent.UpdateFilteredList(
                        index = index,
                        query = it
                    ))
                }
            )
        }
        Button(
            modifier = modifier
                .padding(10.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                }
                event(MainEvent.OnSearch)
            }) {
            Text(
                text = stringResource(R.string.apply_filter),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .fillMaxWidth()
            )
        }
    }
}