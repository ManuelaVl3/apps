package com.example.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.model.Schedule
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange

@Composable
fun ScheduleRow(
    schedule: Schedule,
    onScheduleChange: (Schedule) -> Unit,
    onDelete: () -> Unit,
    existingSchedules: List<Schedule> = emptyList(),
    modifier: Modifier = Modifier
) {
    val openDay = schedule.openDay
    val openTime = schedule.openTime
    val closeDay = schedule.closeDay
    val closeTime = schedule.closeTime
    
    val daysOfWeek = listOf(
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    )
    
    val hours24 = (0..23).map { hour ->
        String.format("%02d:00", hour)
    }
    
    fun dayTimeToMinutes(day: String, time: String): Int {
        val dayIndex = daysOfWeek.indexOf(day)
        val hour = time.split(":")[0].toIntOrNull() ?: 0
        return dayIndex * 24 * 60 + hour * 60
    }
    
    fun schedulesOverlap(schedule1: Schedule, schedule2: Schedule): Boolean {
        if (schedule1.openDay.isBlank() || schedule1.openTime.isBlank() || 
            schedule1.closeDay.isBlank() || schedule1.closeTime.isBlank() ||
            schedule2.openDay.isBlank() || schedule2.openTime.isBlank() || 
            schedule2.closeDay.isBlank() || schedule2.closeTime.isBlank()) {
            return false
        }
        
        val start1 = dayTimeToMinutes(schedule1.openDay, schedule1.openTime)
        val end1 = dayTimeToMinutes(schedule1.closeDay, schedule1.closeTime)
        val start2 = dayTimeToMinutes(schedule2.openDay, schedule2.openTime)
        val end2 = dayTimeToMinutes(schedule2.closeDay, schedule2.closeTime)
        
        return (start1 < end2) && (start2 < end1)
    }
    
    val otherSchedules = existingSchedules.filter { it != schedule }
    
    fun wouldOverlapWithExisting(proposedSchedule: Schedule): Boolean {
        return otherSchedules.any { existingSchedule ->
            schedulesOverlap(proposedSchedule, existingSchedule)
        }
    }
    
    val availableCloseDays = if (openDay.isNotBlank()) {
        val openDayIndex = daysOfWeek.indexOf(openDay)
        if (openDayIndex != -1) {
            daysOfWeek.drop(openDayIndex)
        } else {
            daysOfWeek
        }
    } else {
        daysOfWeek
    }
    
    val availableCloseHours = if (openTime.isNotBlank() && openDay.isNotBlank()) {
        if (closeDay.isNotBlank() && openDay == closeDay) {
            val openHour = openTime.split(":")[0].toIntOrNull() ?: 0
            val hours = ((openHour + 1)..23).map { hour ->
                String.format("%02d:00", hour)
            }
            println("DEBUG: Mismo día - openHour: $openHour, available hours: $hours")
            hours
        } else {
            println("DEBUG: Día diferente o sin día cierre - showing all hours")
            hours24
        }
    } else {
        println("DEBUG: Sin hora/día apertura - showing all hours")
        hours24
    }
    
    val isInvalidSchedule = if (openDay.isNotBlank() && openTime.isNotBlank() &&
                                closeDay.isNotBlank() && closeTime.isNotBlank()) {
        val openDayIndex = daysOfWeek.indexOf(openDay)
        val closeDayIndex = daysOfWeek.indexOf(closeDay)
        
        val hasTimeLogicError = if (openDayIndex > closeDayIndex) {
            true
        } else if (openDayIndex == closeDayIndex) {
            openTime >= closeTime || !availableCloseHours.contains(closeTime)
        } else {
            false
        }
        
        val hasOverlapError = wouldOverlapWithExisting(schedule)
        
        hasTimeLogicError || hasOverlapError
    } else {
        false
    }
    
    val hasOverlapWithExisting = if (openDay.isNotBlank() && openTime.isNotBlank() &&
                                     closeDay.isNotBlank() && closeTime.isNotBlank()) {
        wouldOverlapWithExisting(schedule)
    } else {
        false
    }
    
    LaunchedEffect(openDay) {
        if (closeDay.isNotBlank() && !availableCloseDays.contains(closeDay)) {
            onScheduleChange(schedule.copy(closeDay = "", closeTime = ""))
        }
    }
    
    LaunchedEffect(openTime, closeDay, openDay) {
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
            
            // Sección de Apertura
            Text(
                text = "Apertura",
                fontSize = 14.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                color = Orange,
                fontFamily = MontserratFamily
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DropdownMenu(
                    options = daysOfWeek,
                    value = openDay,
                    onValueChange = { newDay -> onScheduleChange(schedule.copy(openDay = newDay)) },
                    label = "Día",
                    modifier = Modifier.weight(1f)
                )
                
                DropdownMenu(
                    options = hours24,
                    value = openTime,
                    onValueChange = { newTime -> onScheduleChange(schedule.copy(openTime = newTime)) },
                    label = "Hora",
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Sección de Cierre
            Text(
                text = "Cierre",
                fontSize = 14.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                color = Orange,
                fontFamily = MontserratFamily
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DropdownMenu(
                    options = availableCloseDays,
                    value = closeDay,
                    onValueChange = { newDay -> onScheduleChange(schedule.copy(closeDay = newDay)) },
                    label = "Día",
                    modifier = Modifier.weight(1f)
                )
                
                DropdownMenu(
                    options = availableCloseHours,
                    value = closeTime,
                    onValueChange = { newTime -> onScheduleChange(schedule.copy(closeTime = newTime)) },
                    label = "Hora",
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (isInvalidSchedule) {
                Text(
                    text = if (hasOverlapWithExisting) {
                        "Este horario se superpone con otro horario existente"
                    } else {
                        "El horario de cierre debe ser posterior al de apertura"
                    },
                    fontSize = 12.sp,
                    color = ErrorColor,
                    fontFamily = MontserratFamily
                )
            }
        }
    }
}
