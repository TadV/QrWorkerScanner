package com.edurda77.qrworker.ui.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.qrworker.R
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.ui.MainEvent
import com.edurda77.qrworker.ui.theme.Pink80
import com.edurda77.qrworker.ui.theme.black
import com.edurda77.qrworker.ui.theme.grey
import com.edurda77.qrworker.ui.theme.lightGrey
import com.edurda77.qrworker.ui.theme.white
import com.edurda77.qrworker.ui.theme.yellow

@Preview
@Composable
private fun Sample1() {
    ItemTechOperation(
        user = "123123123",
        techOperation = TechOperation(
            codeUser = "123123124",
            createdAt = "2024-05-21T10:38:19.983Z",
            id = 1,
            orderData = "2023-10-28T00:00:00Z",
            orderNumber = "TRE00000908",
            product = "Поролон",
            productionDivision = "Раскройный Цех",
            productionReport = "5b712337-7d6d-11ee-96f7-9440c9fe75e0",
            techOperation = "102",
            techOperationData = "2024-05-21T11:14:00Z",
            isUploadedThisUser = false,
            techOperationName = "102 Транспортировка деталей в ячейку стеллажа для комплектования",
            currentUser = "123123124",
            quantity = 5,
            unit = "шт"
        ),
        event = {}
    )
}

@Preview
@Composable
private fun Sample2() {
    ItemTechOperation(
        user = "123123123",
        techOperation = TechOperation(
            codeUser = "123123123",
            createdAt = "2024-05-21T10:38:19.983Z",
            id = 1,
            orderData = "2023-10-28T00:00:00Z",
            orderNumber = "TRE00000908",
            product = "Поролон",
            productionDivision = "Раскройный Цех",
            productionReport = "5b712337-7d6d-11ee-96f7-9440c9fe75e0",
            techOperation = "102",
            techOperationData = "2024-05-21T11:14:00Z",
            isUploadedThisUser = true,
            techOperationName = "102 Транспортировка деталей в ячейку стеллажа для комплектования",
            currentUser = "123123123",
            quantity = 5,
            unit = "шт"
        ),
        event = {}
    )
}

@Preview
@Composable
private fun Sample3() {
    ItemTechOperation(
        user = "123123123",
        techOperation = TechOperation(
            codeUser = "",
            createdAt = "2024-05-21T10:38:19.983Z",
            id = 1,
            orderData = "2023-10-28T00:00:00Z",
            orderNumber = "TRE00000908",
            product = "Поролон",
            productionDivision = "Раскройный Цех",
            productionReport = "5b712337-7d6d-11ee-96f7-9440c9fe75e0",
            techOperation = "102",
            techOperationData = "2024-05-21T11:14:00Z",
            isUploadedThisUser = false,
            techOperationName = "102 Транспортировка деталей в ячейку стеллажа для комплектования",
            currentUser = "",
            quantity = 5,
            unit = "шт"
        ),
        event = {}
    )
}

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
                if (techOperation.codeUser.isBlank() || techOperation.codeUser == user) {
                    event(MainEvent.SelectTechOperation(techOperation))
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (techOperation.isUploadedThisUser) {
                yellow
            } else  if (techOperation.codeUser.isBlank() || techOperation.codeUser == user) {
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
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (techOperation.codeUser.isBlank() || techOperation.codeUser == user) {
                Checkbox(
                    modifier = modifier.weight(1f),
                    checked = techOperation.codeUser == user, onCheckedChange = {
                   event(MainEvent.SelectTechOperation(techOperation))
                })
                Spacer(modifier = modifier.width(10.dp))
                Text(
                    modifier = modifier.weight(6f),
                    text = techOperation.techOperationName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color.Black,
                    )
                )
                //Spacer(modifier = modifier.weight(1f))
                VerticalDivider(
                    modifier = modifier.height(40.dp),
                    thickness = 2.dp,
                    color = Color.Black
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
                            color = Color.Black,
                        )
                    )
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
                    Text(
                        text = techOperation.techOperationName,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            color = grey,
                        )
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    Text(
                        text = "${stringResource(R.string.worker)} ${techOperation.codeUser}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            color = grey,
                        )
                    )
                }
                VerticalDivider(
                    modifier = modifier.height(40.dp),
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