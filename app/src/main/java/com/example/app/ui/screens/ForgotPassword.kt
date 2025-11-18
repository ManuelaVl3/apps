package com.example.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.components.InputText
import com.example.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ForgotPassword(
    onBack: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var currentStep by rememberSaveable { mutableStateOf(1) }
    var code by rememberSaveable { mutableStateOf(List(6) { "" }) }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
    val isCodeComplete = code.all { it.isNotEmpty() }
    val isPasswordValid = newPassword.isNotBlank() && isValidPassword(newPassword)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            BackgroundBubbles()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (currentStep == 3) "Restablecer contraseña" else "Recuperemos tu\ncontraseña ;)",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontFamily = MontserratFamily
                )

                Spacer(Modifier.height(60.dp))

                when (currentStep) {
                    1 -> {
                    Text(
                        text = "Confirma tu Email",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Start,
                        fontFamily = MontserratFamily,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    InputText(
                        value = email,
                        onValueChange = { email = it },
                        label = stringResource(R.string.email),
                        supportingText = stringResource(R.string.invalid_email),
                        onValidate = { isValidEmail(it) }
                    )

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = {
                            currentStep = 2
                        },
                        enabled = isEmailValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEmailValid) OrangeDeep else Color(0xFFFFCCBC),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFFFCCBC),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(
                            "Enviar código",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = MontserratFamily
                        )
                    }
                    }
                    
                    2 -> {
                    Text(
                        text = "Diligencia el código que te enviamos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        fontFamily = MontserratFamily,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(32.dp))

                    CodeInputRow(
                        code = code,
                        onCodeChange = { index, value ->
                            val newCode = code.toMutableList()
                            newCode[index] = value
                            code = newCode
                        }
                    )

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = {
                            currentStep = 3
                        },
                        enabled = isCodeComplete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCodeComplete) OrangeDeep else Color(0xFFFFCCBC),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFFFCCBC),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(
                            "Verificar código",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = MontserratFamily
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Enviar de nuevo",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily,
                        modifier = Modifier.clickable { 
                            code = List(6) { "" }
                        }
                    )
                    }
                    
                    3 -> {
                        Text(
                            text = "¡Ya casi! Asigna una nueva contraseña",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            fontFamily = MontserratFamily,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(32.dp))

                        InputText(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = stringResource(R.string.password),
                            supportingText = stringResource(R.string.password_invalid),
                            onValidate = { isValidPassword(it) },
                            isPassword = true,
                            passwordVisible = passwordVisible,
                            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible }
                        )

                        Spacer(Modifier.height(32.dp))

                        Button(
                            onClick = {
                                onBack()
                            },
                            enabled = isPasswordValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPasswordValid) OrangeDeep else Color(0xFFFFCCBC),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFFFCCBC),
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(
                                "Guardar",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = MontserratFamily
                            )
                        }
                    }
                }

                Spacer(Modifier.height(80.dp))

                Text(
                    text = "Ya la recordé 🤩",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily,
                    modifier = Modifier.clickable { onBack() }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CodeInputRow(
    code: List<String>,
    onCodeChange: (Int, String) -> Unit
) {
    val focusRequesters = remember { List(6) { FocusRequester() } }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        code.forEachIndexed { index, digit ->
            OutlinedTextField(
                value = digit,
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        onCodeChange(index, newValue)
                        if (newValue.isNotEmpty() && index < 5) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .width(50.dp)
                    .height(60.dp)
                    .focusRequester(focusRequesters[index]),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MontserratFamily
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = Orange.copy(alpha = 0.6f),
                    cursorColor = Orange
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
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
private fun PreviewForgotPassword() {
    AppTheme {
        ForgotPassword()
    }
}

