package com.example.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.components.DropdownMenu
import com.example.app.ui.theme.ErrorColor
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.Peach
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    userName: String = "Manuela",
    userCity: String = "Armenia",
    onLogout: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf(userName) }
    var city by rememberSaveable { mutableStateOf(userCity) }
    var isEditingName by rememberSaveable { mutableStateOf(false) }
    var isEditingCity by rememberSaveable { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val successMessage = stringResource(R.string.edit_save_success)

    val cities = listOf("Armenia", "Pereira", "Cartagena", "Medellín", "Barranquilla", "Bogotá")
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil de usuario",
                        tint = OrangeDeep,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(Modifier.width(12.dp))
                    
                    Text(
                        text = stringResource(R.string.edit_my_profile),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily
                    )
                }
                
                Spacer(Modifier.height(40.dp))
                
                OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { 
                    Text(
                        text = stringResource(R.string.name),
                        fontFamily = MontserratFamily
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isEditingName) OrangeDeep else Orange,
                    unfocusedBorderColor = if (isEditingName) OrangeDeep else Orange.copy(alpha = 0.6f)
                ),
                enabled = isEditingName,
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = { isEditingName = !isEditingName }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ModeEdit,
                            contentDescription = if (isEditingName) stringResource(R.string.edit_save_name) else stringResource(R.string.edit_name),
                            tint = Orange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
            
            Spacer(Modifier.height(20.dp))
            
            DropdownMenu(
                value = city,
                onValueChange = { city = it },
                options = cities,
                label = stringResource(R.string.city),
                isError = false,
                errorMessage = "",
                enabled = isEditingCity,
                onSelectionChange = { },
                trailingIcon = {
                    IconButton(
                        onClick = { isEditingCity = !isEditingCity }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ModeEdit,
                            contentDescription = if (isEditingCity) stringResource(R.string.edit_save_city) else stringResource(R.string.edit_city),
                            tint = Orange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            if (isEditingName || isEditingCity) {
                Spacer(Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            isEditingName = false
                            isEditingCity = false
                            name = userName
                            city = userCity
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ErrorColor
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.edit_cancel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            fontFamily = MontserratFamily
                        )
                    }
                    
                    Button(
                        onClick = {
                            scope.launch {
                                isEditingName = false
                                isEditingCity = false
                                snackbarHostState.showSnackbar(successMessage)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeDeep
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.edit_save),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            fontFamily = MontserratFamily
                        )
                    }
                }
            }
            }
            
            Text(
                text = stringResource(R.string.edit_logout),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = MontserratFamily,
                modifier = Modifier.clickable { onLogout() }
            )
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbar = { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = OrangeDeep,
                    contentColor = Color.White,
                    actionColor = Color.White
                )
            }
        )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewEditProfile() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = OrangeDeep,
            secondary = Orange,
            tertiary = Peach
        )
    ) {
        EditProfile()
    }
}
