package com.example.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.Peach
import com.example.app.ui.components.InputText

@Composable
fun LoginForm(
    onRegister: () -> Unit = {},
    onLoginSuccess: (String) -> Unit = {},
    onAdminLoginSuccess: (String) -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    val peachBorder = Color(0xFFFFCCBC)

    var email by rememberSaveable { mutableStateOf("moderador@email.com") }
    var password by rememberSaveable { mutableStateOf("0987654321!") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(50.dp))

            InputText(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email),
                supportingText = stringResource(R.string.invalid_email),
                onValidate = { email -> isValidEmail(email) }
            )

            Spacer(Modifier.height(16.dp))

            InputText(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.password),
                supportingText = stringResource(R.string.password_invalid),
                onValidate = { password -> isValidPassword(password) },
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordVisibilityToggle = { passwordVisible = !passwordVisible }
            )

            Spacer(Modifier.height(50.dp))

            Button(
                onClick = {
                    scope.launch {
                        if(email == "manuela@email.com" && password == "1234567890*"){
                            delay(500)
                            onLoginSuccess("1")
                        }else if((email == "moderador@mod.com" || email == "moderador@email.com") && password == "0987654321!"){
                            delay(500)
                            onAdminLoginSuccess("3")
                        }else if(email == "pascal@email.com" && password == "1234567890)"){
                            delay(500)
                            onLoginSuccess("2")
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
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable { onRegister() }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.login_forgot_password),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable { onForgotPassword() }
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