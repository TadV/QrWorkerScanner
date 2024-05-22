package com.edurda77.qrworker.ui.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

@Preview
@Composable
private fun Sample1() {
    ItemTechOperation(
        user = "",
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
            techOperationName = "102 Транспортировка деталей в ячейку стеллажа для комплектования"
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
            techOperationName = "102 Транспортировка деталей в ячейку стеллажа для комплектования"
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
            techOperationName = "102 Транспортировка деталей в ячейку стеллажа для комплектования"
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
            containerColor = if (techOperation.codeUser.isBlank() || techOperation.codeUser == user) {
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
                    checked = techOperation.codeUser == user, onCheckedChange = {
                   event(MainEvent.SelectTechOperation(techOperation))
                })
                Spacer(modifier = modifier.width(10.dp))
                Text(
                    text = techOperation.techOperationName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color.Black,
                    )
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_lock_outline_24),
                    contentDescription = "",
                    tint = grey
                )
                Spacer(modifier = modifier.width(10.dp))
                Text(
                    text = techOperation.techOperationName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = grey,
                    )
                )
            }
        }
    }
}