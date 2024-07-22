package com.edurda77.qrworker.ui.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.qrworker.R
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.ui.theme.grey
import com.edurda77.qrworker.ui.theme.white

@Composable
fun DialogApprovedOperations(
    modifier: Modifier = Modifier,
    approvedTechOperations: List<TechOperation>,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(approvedTechOperations) { operation ->
            Card(
                modifier = modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = white
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 20.dp
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = modifier.weight(6f),
                    ) {
                        operation.techOperationName?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(500),
                                    color = grey,
                                )
                            )
                        }
                        Spacer(modifier = modifier.height(10.dp))
                        Text(
                            text = "${stringResource(R.string.worker)} ${operation.workerFIO}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(500),
                                color = grey,
                            )
                        )
                    }
                    VerticalDivider(
                        modifier = modifier.fillMaxHeight(),
                        thickness = 2.dp,
                        color = grey
                    )
                    Spacer(modifier = modifier.width(5.dp))
                    Column(
                        modifier = modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = operation.quantity.toString(),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                                color = grey,
                            )
                        )
                        if (operation.unit != null) {
                            Spacer(modifier = modifier.height(5.dp))
                            Text(
                                text = operation.unit,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight(500),
                                    color = grey,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}