package com.example.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.components.DropdownMenu
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.TextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateForm(
    onBack: () -> Unit = {},
    onCreateSuccess: () -> Unit = {}
) {
    var placeName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var openingHours by rememberSaveable { mutableStateOf("") }
    var contactPhone by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }

    val categories = listOf("Restaurantes", "Comidas rápidas", "Cafetería", "Museos", "Hoteles")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BackgroundBubbles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = OrangeDeep,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(Modifier.width(8.dp))
                
                Text(
                    text = stringResource(R.string.back),
                    fontSize = 16.sp,
                    color = OrangeDeep,
                    fontFamily = MontserratFamily
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.create_form_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                fontFamily = MontserratFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = placeName,
                onValueChange = { placeName = it },
                label = {
                    Text(
                        text = stringResource(R.string.create_place_name),
                        fontFamily = MontserratFamily
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = {
                    Text(
                        text = stringResource(R.string.create_description),
                        fontFamily = MontserratFamily
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                )
            )

            OutlinedTextField(
                value = openingHours,
                onValueChange = { openingHours = it },
                label = {
                    Text(
                        text = stringResource(R.string.create_opening_hours),
                        fontFamily = MontserratFamily
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = contactPhone,
                onValueChange = { contactPhone = it },
                label = {
                    Text(
                        text = stringResource(R.string.create_contact_phone),
                        fontFamily = MontserratFamily
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = {
                    Text(
                        text = stringResource(R.string.city),
                        fontFamily = MontserratFamily
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = {
                    Text(
                        text = stringResource(R.string.create_address),
                        fontFamily = MontserratFamily
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                ),
                singleLine = true
            )

            OutlinedCard(
                onClick = { /* TODO: Implementar selección de imagen */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = Color.Transparent,
                    contentColor = TextDark
                ),
                border = CardDefaults.outlinedCardBorder(
                    enabled = true
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.create_add_image),
                        fontFamily = MontserratFamily,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = stringResource(R.string.create_add_image_description),
                        tint = Orange
                    )
                }
            }

            DropdownMenu(
                value = category,
                onValueChange = { category = it },
                options = categories,
                label = stringResource(R.string.create_category),
                onSelectionChange = { category = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onCreateSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeDeep,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.create_save),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MontserratFamily
                )
            }
        }
    }
}

@Composable
private fun BackgroundBubbles() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
    }
}

@Preview(showBackground = true)
@Composable
fun CreateFormPreview() {
    AppTheme {
        CreateForm()
    }
}
