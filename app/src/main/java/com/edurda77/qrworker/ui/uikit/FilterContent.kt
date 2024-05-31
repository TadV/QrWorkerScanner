package com.edurda77.qrworker.ui.uikit

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.qrworker.R
import com.edurda77.qrworker.ui.MainEvent
import com.edurda77.qrworker.ui.theme.black

//@SuppressLint("MutableCollectionMutableState")
@Composable
fun FilterContent(
    modifier: Modifier = Modifier,
    filtersQueries: List<String>,
    event: (MainEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (index in filtersQueries.indices) {
            Log.d("test werty", "index - $index, item ${filtersQueries[index]}")
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = black,
                ),
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
    }
}