package com.example.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.model.ScheduleData
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange

@Composable
fun ScheduleRow(
    schedule: ScheduleData,
    onScheduleChange: (ScheduleData) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val day = schedule.day
    val openTime = schedule.openTime
    val closeTime = schedule.closeTime
    val daysOfWeek = listOf(
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    )
    
    val hours24 = (0..23).map { hour ->
        String.format("%02d:00", hour)
    }
    
    val availableCloseHours = if (openTime.isNotBlank()) {
        val openHour = openTime.split(":")[0].toIntOrNull() ?: 0
        hours24.filter { hour ->
            val hourValue = hour.split(":")[0].toIntOrNull() ?: 0
            hourValue > openHour
        }
    } else {
        hours24
    }
    
    val isInvalidSchedule = openTime.isNotBlank() && closeTime.isNotBlank() && 
                           (openTime >= closeTime || !availableCloseHours.contains(closeTime))
    
    LaunchedEffect(openTime) {
        if (closeTime.isNotBlank() && !availableCloseHours.contains(closeTime)) {
            onScheduleChange(schedule.copy(closeTime = ""))
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isInvalidSchedule) ErrorColor.copy(alpha = 0.1f) else Color.Transparent
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp, 
            color = if (isInvalidSchedule) ErrorColor else Orange.copy(alpha = 0.6f)
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Horario",
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
                
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar horario",
                        tint = ErrorColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            DropdownMenu(
                options = daysOfWeek,
                value = day,
                onValueChange = { newDay -> onScheduleChange(schedule.copy(day = newDay)) },
                label = "Día",
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DropdownMenu(
                    options = hours24,
                    value = openTime,
                    onValueChange = { newOpenTime -> onScheduleChange(schedule.copy(openTime = newOpenTime)) },
                    label = "Hora de apertura",
                    modifier = Modifier.weight(1f)
                )
                
                DropdownMenu(
                    options = availableCloseHours,
                    value = closeTime,
                    onValueChange = { newCloseTime -> onScheduleChange(schedule.copy(closeTime = newCloseTime)) },
                    label = "Hora de cierre",
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (isInvalidSchedule) {
                Text(
                    text = "La hora de apertura debe ser anterior a la hora de cierre",
                    fontSize = 12.sp,
                    color = ErrorColor,
                    fontFamily = MontserratFamily
                )
            }
        }
    }
}
