package com.edurda77.qrworker.ui.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.qrworker.R
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.ui.MainEvent
import com.edurda77.qrworker.ui.theme.grey
import com.edurda77.qrworker.ui.theme.lightGrey
import com.edurda77.qrworker.ui.theme.white
import com.edurda77.qrworker.ui.theme.yellow

@Composable
fun ItemTechOperation(
    modifier: Modifier = Modifier,
    user: String,
    techOperation: TechOperation,
    event: (MainEvent) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (techOperation.workerCode!!.isBlank() || techOperation.workerCode == user) {
                    event(MainEvent.SelectTechOperation(techOperation))
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (techOperation.isUploadedThisUser) {
                yellow
            } else  if (techOperation.workerCode!!.isBlank() || techOperation.workerCode == user) {
                white
            } else lightGrey
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
            if (techOperation.workerCode!!.isBlank() || techOperation.workerCode == user) {
                Checkbox(
                    modifier = modifier.weight(1f),
                    checked = techOperation.workerCode == user,
                    onCheckedChange = {
                    event(MainEvent.SelectTechOperation(techOperation))
                    }
                )
                Spacer(modifier = modifier.width(2.dp))
                techOperation.techOperationNumber?.let {
                    Text(
//                        modifier = modifier.weight(1f),
                        text = it,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Black,
                        )
                    )
                }
                Spacer(modifier = modifier.width(2.dp))
                VerticalDivider(
                    modifier = modifier.fillMaxHeight(),
                    thickness = 2.dp,
                    color = Color.Gray
                )
                Spacer(modifier = modifier.width(2.dp))
                techOperation.techOperationName?.let {
                    Text(
                        modifier = modifier.weight(8f),
                        text = it,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Black,
                        )
                    )
                }
                Spacer(modifier = modifier.width(2.dp))
                VerticalDivider(
                    modifier = modifier.fillMaxHeight(),
                    thickness = 2.dp,
                    color = Color.Gray
                )
                Spacer(modifier = modifier.width(2.dp))
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = techOperation.quantity.toString(),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Black,
                        )
                    )
                    if (techOperation.unit!=null) {
                        Spacer(modifier = modifier.height(5.dp))
                        Text(
                            text = techOperation.unit,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                                color = Color.Black,
                            )
                        )
                    }
                }
            } else {
                Icon(
                    modifier = modifier.weight(1f),
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_lock_outline_24),
                    contentDescription = "",
                    tint = grey
                )
                Spacer(modifier = modifier.width(10.dp))
                Column(
                    modifier = modifier.weight(6f),
                ) {
                    techOperation.techOperationName?.let {
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
                        text = "${stringResource(R.string.worker)} ${techOperation.currentUserFIO}",
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
                        text = techOperation.quantity.toString(),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                            color = grey,
                        )
                    )
                    if (techOperation.unit!=null) {
                        Spacer(modifier = modifier.height(5.dp))
                        Text(
                            text = techOperation.unit,
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