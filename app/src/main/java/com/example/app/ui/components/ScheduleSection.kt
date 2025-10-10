package com.example.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.model.ScheduleData
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.Peach

@Composable
fun ScheduleSection(
    schedules: List<ScheduleData>,
    onSchedulesChange: (List<ScheduleData>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título de la sección
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = "Horarios de atención",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                fontFamily = MontserratFamily
            )
            
            // Botón para agregar horario
            Button(
                onClick = {
                    val newSchedule = ScheduleData(
                        day = "",
                        openTime = "",
                        closeTime = ""
                    )
                    onSchedulesChange(schedules + newSchedule)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar horario",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Agregar",
                    fontSize = 12.sp
                )
            }
        }
        
        // Lista de horarios
        if (schedules.isEmpty()) {
            Text(
                text = "No hay horarios configurados",
                fontSize = 14.sp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontFamily = MontserratFamily,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            schedules.forEachIndexed { index, schedule ->
                ScheduleRow(
                    schedule = schedule,
                    onScheduleChange = { updatedSchedule ->
                        val updatedSchedules = schedules.toMutableList().apply { 
                            this[index] = updatedSchedule 
                        }
                        onSchedulesChange(updatedSchedules)
                    },
                    onDelete = {
                        val updatedSchedules = schedules.toMutableList().apply { 
                            removeAt(index) 
                        }
                        onSchedulesChange(updatedSchedules)
                    }
                )
            }
        }
    }
}
