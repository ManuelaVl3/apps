package com.example.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.Peach
import com.example.app.ui.theme.TextDark

@Composable
fun LoginForm(
    onRegister: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    val peachBorder = Color(0xFFFFCCBC)

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    var isEmailError by rememberSaveable { mutableStateOf(false) }
    var isPasswordError by rememberSaveable { mutableStateOf(false) }
    
    val successMessage = stringResource(R.string.login_success)
    val errorMessage = stringResource(R.string.login_error)
    
    fun isValidEmail(email: String): Boolean {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }
    
    fun isValidPassword(password: String): Boolean {
        if (password.length < 10) return false
        val specialCharRegex = "[!@#$%^&*(),.?\":{}|<>]".toRegex()
        return specialCharRegex.containsMatchIn(password)
    }
    
    val isEmailValid = email.isNotBlank() && isValidEmail(email)
    val isPasswordValid = password.isNotBlank() && isValidPassword(password)
    val isFormValid = isEmailValid && isPasswordValid

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
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.logo_description),
                modifier = Modifier
                    .size(110.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.welcome),
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(50.dp))

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
                isError = isEmailError,
                singleLine = true,
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

            Spacer(Modifier.height(50.dp))

            Button(
                onClick = {
                    scope.launch {
                        if(email == "manuela@email.com" && password == "1234567890*"){
                            snackbarHostState.showSnackbar(successMessage)
                            delay(500)
                            onLoginSuccess()
                        }else{
                            snackbarHostState.showSnackbar(errorMessage)
                        }
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) OrangeDeep else peachBorder,
                    contentColor = Color.White,
                    disabledContainerColor = peachBorder,
                    disabledContentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.login), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(50.dp))

            Text(
                text = stringResource(R.string.login_register_link),
                color = Color(0xFF2E2E2E),
                modifier = Modifier.clickable { onRegister() }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.login_forgot_password),
                color = Color(0xFF2E2E2E)
            )

            Spacer(Modifier.height(16.dp))

        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbar = { snackbarData ->
                val isError = snackbarData.visuals.message == errorMessage
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = if (isError) ErrorColor else Orange,
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
            center = Offset(x = w * 0.96f, y = h * 0.97f)
        )
        drawCircle(
            color = Peach,
            radius = w * 0.33f,
            center = Offset(x = w * 0.62f, y = h * 1.04f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewLoginForm() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = OrangeDeep,
            secondary = Orange,
            tertiary = Peach
        )
    ) {
        LoginForm()
    }
}