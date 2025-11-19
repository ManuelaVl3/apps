package com.example.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.example.app.R
import com.example.app.model.Role
import com.example.app.model.User
import com.example.app.ui.components.DropdownMenu
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.Peach
import com.example.app.ui.components.InputText
import com.example.app.ui.components.OperationResultHandler
import com.example.app.viewmodel.UsersViewModel
import java.util.UUID

@Composable
fun RegisterForm(
    onRegister: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val userViewModel = UsersViewModel()

    val userResult by userViewModel.userResult.collectAsState()


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
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.register_welcome),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
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
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            InputText(
                value = name,
                onValueChange = { name = it },
                label = stringResource(R.string.name),
                supportingText = stringResource(R.string.name_invalid),
                onValidate = { name -> isValidName(name) }
            )

            Spacer(Modifier.height(20.dp))

            InputText(
                value = username,
                onValueChange = { username = it },
                label = stringResource(R.string.username),
                supportingText = stringResource(R.string.username),
            )

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(16.dp))

            DropdownMenu(
                value = selectedCity,
                onValueChange = { newCity ->
                    selectedCity = newCity
                },
                options = cities,
                label = stringResource(R.string.city),
                isError = isCityError,
                errorMessage = stringResource(R.string.city_invalid),
                onSelectionChange = { newSelectedCity ->
                    selectedCity = newSelectedCity
                    isCityError = newSelectedCity.isNotEmpty() && !isValidCity(newSelectedCity)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    Log.d("RegisterForm", "Register button clicked")
                    Log.d("RegisterForm", "Capturing city selection now")
                    Log.d("RegisterForm", "Final city value: '$selectedCity'")

                    val user = User(
                        userId = UUID.randomUUID().toString(),
                        name = name,
                        username = username,
                        email = email,
                        password = password,
                        city = selectedCity,
                        role = Role.USER
                    )

                    userViewModel.create(user)
                    
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

            OperationResultHandler(
                requestResult = userResult,
                onSuccess = {
                    onLoginClick()
                    userViewModel.resetOperationResult()
                },
                onFailure = {
                    userViewModel.resetOperationResult()
                }
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.register_already_registered),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
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
