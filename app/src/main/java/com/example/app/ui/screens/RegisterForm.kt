package com.example.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.example.app.R
import com.example.app.ui.components.DropdownMenu
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.Peach
import com.example.app.ui.theme.TextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterForm(
    onRegister: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var isNameError by rememberSaveable { mutableStateOf(false) }
    var isEmailError by rememberSaveable { mutableStateOf(false) }
    var isPasswordError by rememberSaveable { mutableStateOf(false) }
    var isCityError by rememberSaveable { mutableStateOf(false) }
    var selectedCity by rememberSaveable { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    val successMessage = stringResource(R.string.register_success)
    
    val cities = listOf("Armenia", "Pereira", "Cartagena", "Medellín", "Barranquilla", "Bogotá")

    val peachBorder = Color(0xFFFFCCBC)

    fun isValidEmail(email: String): Boolean {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }
    
    fun isValidPassword(password: String): Boolean {
        if (password.length < 10) return false
        val specialCharRegex = "[!@#$%^&*(),.?\":{}|<>]".toRegex()
        return specialCharRegex.containsMatchIn(password)
    }
    
    fun isValidName(name: String): Boolean {
        return name.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+".toRegex())
    }
    
    fun isValidCity(city: String): Boolean {
        return city.isNotEmpty() && cities.contains(city)
    }

    val isNameValid = name.isNotBlank() && isValidName(name)
    val isEmailValid = email.isNotBlank() && isValidEmail(email)
    val isPasswordValid = password.isNotBlank() && isValidPassword(password)
    val isCityValid = selectedCity.isNotBlank() && isValidCity(selectedCity)
    val isFormValid = isNameValid && isEmailValid && isPasswordValid && isCityValid
    
    LaunchedEffect(isFormValid, isNameValid, isEmailValid, isPasswordValid, isCityValid) {
        Log.d("RegisterForm", "Validation status:")
        Log.d("RegisterForm", "  Name valid: $isNameValid (value: '$name')")
        Log.d("RegisterForm", "  Email valid: $isEmailValid (value: '$email')")
        Log.d("RegisterForm", "  Password valid: $isPasswordValid (value: '$password')")
        Log.d("RegisterForm", "  City valid: $isCityValid (value: '$selectedCity')")
        Log.d("RegisterForm", "  Form valid: $isFormValid")
        Log.d("RegisterForm", "  Button enabled: $isFormValid")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BackgroundBubbles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.register_welcome),
                fontSize = 20.sp,
                color = TextDark,
                textAlign = TextAlign.Center
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = OrangeDeep)) {
                        append("Uni")
                    }
                    withStyle(style = SpanStyle(color = ErrorColor)) {
                        append("Local")
                    }
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.register_subtitle),
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    isNameError = it.isNotEmpty() && !isValidName(it)
                },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangeDeep,
                    unfocusedBorderColor = peachBorder.copy(alpha = 0.6f)
                ),
                isError = isNameError,
                singleLine = true,
                trailingIcon = {
                    when {
                        name.isNotEmpty() && isValidName(name) -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Nombre válido",
                                tint = OrangeDeep,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        name.isNotEmpty() && !isValidName(name) -> {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Nombre inválido",
                                tint = ErrorColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                supportingText = {
                    if (isNameError) {
                        Text(stringResource(R.string.name_invalid), fontSize = 12.sp, color = ErrorColor)
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    isEmailError = it.isNotEmpty() && !isValidEmail(it)
                },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangeDeep,
                    unfocusedBorderColor = peachBorder.copy(alpha = 0.6f)
                ),
                singleLine = true,
                isError = isEmailError,
                trailingIcon = {
                    when {
                        email.isNotEmpty() && isValidEmail(email) -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Email válido",
                                tint = OrangeDeep,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        email.isNotEmpty() && !isValidEmail(email) -> {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Email inválido",
                                tint = ErrorColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                supportingText = {
                    if (isEmailError) {
                        Text(stringResource(R.string.invalid_email), fontSize = 12.sp, color = ErrorColor)
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    isPasswordError = it.isNotEmpty() && !isValidPassword(it)
                },
                label = { Text(stringResource(R.string.password)) },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangeDeep,
                    unfocusedBorderColor = peachBorder.copy(alpha = 0.6f)
                ),
                isError = isPasswordError,
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                supportingText = {
                    if (isPasswordError) {
                        val errorMessage = when {
                            password.length < 10 -> stringResource(R.string.password_too_short)
                            !password.matches("[!@#$%^&*(),.?\":{}|<>]".toRegex()) -> stringResource(R.string.password_no_special)
                            else -> ""
                        }
                        Text(errorMessage, fontSize = 12.sp, color = ErrorColor)
                    }
                },
                trailingIcon = {
                    val iconRes = if (passwordVisible) R.drawable.visible_password else R.drawable.password_encrypt
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password),
                            tint = Color.Unspecified,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            DropdownMenu(
                value = selectedCity,
                onValueChange = { newCity ->
                    Log.d("RegisterForm", "City onValueChange called with: '$newCity'")
                    selectedCity = newCity
                },
                options = cities,
                label = stringResource(R.string.city),
                isError = isCityError,
                errorMessage = stringResource(R.string.city_invalid),
                onSelectionChange = { newSelectedCity ->
                    Log.d("RegisterForm", "City selected: '$newSelectedCity'")
                    selectedCity = newSelectedCity
                    isCityError = newSelectedCity.isNotEmpty() && !isValidCity(newSelectedCity)
                    Log.d("RegisterForm", "Updated selectedCity to: '$selectedCity'")
                    Log.d("RegisterForm", "Is city valid: ${isValidCity(newSelectedCity)}")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    Log.d("RegisterForm", "Register button clicked")
                    Log.d("RegisterForm", "Capturing city selection now")
                    Log.d("RegisterForm", "Final city value: '$selectedCity'")
                    
                    city = selectedCity
                    
                    scope.launch {
                        snackbarHostState.showSnackbar(successMessage)
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) OrangeDeep else peachBorder,
                    contentColor = Color.White,
                    disabledContainerColor = peachBorder,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(stringResource(R.string.register), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.register_already_registered),
                fontSize = 16.sp,
                color = TextDark,
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbar = { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = Orange,
                    contentColor = Color.White,
                    actionColor = Color.White
                )
            }
        )
    }
}

@Composable
private fun BackgroundBubbles() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawCircle(
            color = OrangeDeep,
            radius = w * 0.36f,
            center = Offset(x = w * 0.03f, y = h * 0.0020f)
        )
        drawCircle(
            color = Peach,
            radius = w * 0.33f,
            center = Offset(x = w * 0.4f, y = h * -0.05f)
        )

        drawCircle(
            color = OrangeDeep,
            radius = w * 0.36f,
            center = Offset(x = w * 0.96f, y = h * 1.04f)
        )
        drawCircle(
            color = Peach,
            radius = w * 0.33f,
            center = Offset(x = w * 0.62f, y = h * 1.09f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewRegisterForm() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = OrangeDeep,
            secondary = Orange,
            tertiary = Peach
        )
    ) {
        RegisterForm()
    }
}
