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
import com.example.app.model.Schedule
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.Peach

@Composable
fun ScheduleSection(
    schedules: List<Schedule>,
    onSchedulesChange: (List<Schedule>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
            
            Button(
                onClick = {
                    val newSchedule = Schedule(
                        openDay = "",
                        openTime = "",
                        closeDay = "",
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
                    },
                    existingSchedules = schedules
                )
            }
            
            val completeSchedules = schedules.filter {
                it.openDay.isNotBlank() && it.openTime.isNotBlank() && 
                it.closeDay.isNotBlank() && it.closeTime.isNotBlank() 
            }
            
            if (completeSchedules.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Orange.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Resumen de horarios",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange,
                            fontFamily = MontserratFamily
                        )
                        
                        completeSchedules.forEach { schedule ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (schedule.openDay == schedule.closeDay) {
                                        schedule.openDay
                                    } else {
                                        "${schedule.openDay} - ${schedule.closeDay}"
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = MontserratFamily
                                )
                                Text(
                                    text = "${schedule.openTime} - ${schedule.closeTime}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                    fontFamily = MontserratFamily
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
