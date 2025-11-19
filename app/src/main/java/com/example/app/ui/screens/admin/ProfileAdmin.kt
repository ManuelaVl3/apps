package com.example.app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.OrangeDeep

@Composable
fun ProfileAdmin(
    userName: String = "Administrador",
    userUsername: String = "moderador",
    userEmail: String = "moderador@email.com",
    userCity: String = "Armenia",
    onLogout: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
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
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil de moderador",
                        tint = OrangeDeep,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(Modifier.width(12.dp))
                    
                    Text(
                        text = "Perfil de Moderador",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily
                    )
                }
                
                Spacer(Modifier.height(40.dp))
                
                // Información del moderador (solo lectura)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Nombre
                        Column {
                            Text(
                                text = "Nombre",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontFamily = MontserratFamily
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = userName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = MontserratFamily
                            )
                        }
                        
                        HorizontalDivider(
                            color = Color.Gray.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        
                        // Username
                        Column {
                            Text(
                                text = "Usuario",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontFamily = MontserratFamily
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "@$userUsername",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = MontserratFamily
                            )
                        }
                        
                        HorizontalDivider(
                            color = Color.Gray.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        
                        // Email
                        Column {
                            Text(
                                text = "Correo electrónico",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontFamily = MontserratFamily
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = userEmail,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = MontserratFamily
                            )
                        }
                        
                        HorizontalDivider(
                            color = Color.Gray.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        
                        // Ciudad
                        Column {
                            Text(
                                text = "Ciudad",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontFamily = MontserratFamily
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = userCity,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = MontserratFamily
                            )
                        }
                        
                        HorizontalDivider(
                            color = Color.Gray.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        
                        // Rol
                        Column {
                            Text(
                                text = "Rol",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontFamily = MontserratFamily
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Moderador",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = OrangeDeep,
                                fontFamily = MontserratFamily
                            )
                        }
                    }
                }
            }
            
            // Botón de cerrar sesión
            Text(
                text = stringResource(R.string.edit_logout),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = MontserratFamily,
                modifier = Modifier.clickable { onLogout() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileAdminPreview() {
    AppTheme {
        ProfileAdmin()
    }
}

