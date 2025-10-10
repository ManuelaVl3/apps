package com.example.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    supportingText: String = "",
    onValidate: (String) -> Boolean = { true },
    icon: ImageVector? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
) {
    var isError by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(false) }
    
    LaunchedEffect(value) {
        if (value.isNotEmpty()) {
            isValid = onValidate(value)
            isError = !isValid
        } else {
            isValid = false
            isError = false
        }
    }
    
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { 
            Text(
                text = label,
                fontFamily = MontserratFamily
            ) 
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Orange,
            unfocusedBorderColor = Orange.copy(alpha = 0.6f)
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        isError = isError,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        supportingText = {
            if (isError && supportingText.isNotEmpty()) {
                Text(
                    text = supportingText,
                    fontSize = 12.sp,
                    color = ErrorColor
                )
            }
        },
        trailingIcon = {
            when {
                isPassword && onPasswordVisibilityToggle != null -> {
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Orange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                value.isNotEmpty() && isValid -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(R.string.valid_option),
                        tint = OrangeDeep,
                        modifier = Modifier.size(20.dp)
                    )
                }
                value.isNotEmpty() && !isValid -> {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = stringResource(R.string.invalid_option),
                        tint = ErrorColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = Orange,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    )
}
