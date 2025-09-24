package com.example.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import com.example.app.R
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.Peach
import com.example.app.ui.theme.TextDark

@Composable
fun MainPage(
    onLogin: () -> Unit = {},
    onSignUp: () -> Unit = {},
) {
    var isLoginPressed by rememberSaveable { mutableStateOf(false) }
    var isSignUpPressed by rememberSaveable { mutableStateOf(false) }
    
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
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(R.string.logo_description),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(110.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.welcome),
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            OutlinedButton(
                onClick = {
                    isLoginPressed = true
                    onLogin()
                },
                shape = RoundedCornerShape(15.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 3.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (isLoginPressed) Color(0xFFFF7043) else Color(0xFFFFCCBC)
                    )
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isLoginPressed) Color(0xFFFF7043) else Color.Transparent,
                    contentColor = if (isLoginPressed) Color.White else TextDark,
                ),
                modifier = Modifier
                    .width(277.dp)
                    .height(51.dp)
            ) {
                Text(stringResource(R.string.login), fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    isSignUpPressed = true
                    onSignUp()
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSignUpPressed) Color(0xFFFFCCBC) else OrangeDeep,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(277.dp)
                    .height(51.dp)
            ) {
                Text(stringResource(R.string.register), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
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
private fun PreviewWelcome() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = OrangeDeep,
            secondary = Orange,
            tertiary = Peach
        )
    ) {
        MainPage()
    }
}