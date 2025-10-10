package com.example.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    label: String,
    isError: Boolean = false,
    errorMessage: String = "",
    onSelectionChange: (String) -> Unit = {},
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedItem by rememberSaveable { mutableStateOf(value) }

    fun isValidOption(option: String): Boolean {
        return option.isNotEmpty() && options.contains(option)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { newValue ->
                selectedItem = newValue
            },
            label = { Text(label) },
            readOnly = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange,
                unfocusedBorderColor = Orange.copy(alpha = 0.6f)
            ),
            isError = isError,
            singleLine = true,
            trailingIcon = {
                if (trailingIcon != null) {
                    trailingIcon()
                } else {
                    when {
                        selectedItem.isNotEmpty() && isValidOption(selectedItem) -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = stringResource(R.string.valid_option),
                                tint = OrangeDeep,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        selectedItem.isNotEmpty() && !isValidOption(selectedItem) -> {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = stringResource(R.string.invalid_option),
                                tint = ErrorColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (expanded) stringResource(R.string.close_menu) else stringResource(R.string.open_menu),
                                tint = OrangeDeep,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            },
            supportingText = {
                if (isError) {
                    Text(errorMessage, fontSize = 12.sp, color = ErrorColor)
                }
            }
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedItem = option
                        onSelectionChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
